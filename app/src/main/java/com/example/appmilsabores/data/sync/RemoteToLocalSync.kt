package com.example.appmilsabores.data.sync

import android.content.Context
import android.util.Log
import org.json.JSONObject
import java.io.File
import com.example.appmilsabores.AppMilSaboresApplication
import com.example.appmilsabores.R
import com.example.appmilsabores.data.local.AppDatabase
import com.example.appmilsabores.data.local.entity.ProductEntity
import com.example.appmilsabores.model.ProductoDto
import com.example.appmilsabores.network.ApiClient
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.Normalizer

object RemoteToLocalSync {
    /**
     * Sync products from remote backend into local Room DB.
     * Returns true if sync succeeded, false otherwise.
     */
    suspend fun syncProducts(db: AppDatabase, context: Context): Boolean {
        try {
            val productos: List<ProductoDto> = ApiClient.service.getProductos()
            Log.d("Sync", "Fetched ${'$'}{productos.size} products from remote API")

            val httpClient = OkHttpClient.Builder().build()

            productos.forEach { dto ->
                Log.d(
                    "Sync",
                    "Remote Producto - codigo:${'$'}{dto.codigoProducto} nombre:${'$'}{dto.nombreProducto} precio:${'$'}{dto.precioProducto} stock:${'$'}{dto.stock} imagen:${'$'}{dto.imagenProducto}"
                )

                // Quick HEAD check for the image URL so we can detect 404/403 issues early in logs
                try {
                    // Prefer explicit imagen_url returned by backend; otherwise derive
                    val imageUrl = when {
                        dto.imagenUrl?.isNotBlank() == true -> {
                            val p = dto.imagenUrl!!
                            if (p.startsWith("/")) "http://10.0.2.2:8080${p}" else p
                        }
                        dto.imagenProducto?.isNotBlank() == true -> {
                            val raw = dto.imagenProducto!!
                            if (raw.startsWith("/")) {
                                "http://10.0.2.2:8080${raw}"
                            } else {
                                // Build the images controller URL by base name; controller will resolve extension
                                val base = raw.substringAfterLast('/').substringBeforeLast('.')
                                "http://10.0.2.2:8080/images/products/${base}"
                            }
                        }
                        else -> null
                    }

                    if (!imageUrl.isNullOrBlank()) {
                        val headReq = Request.Builder().url(imageUrl).head().build()
                        val resp = httpClient.newCall(headReq).execute()
                        Log.d("Sync", "Image HEAD ${'$'}{dto.codigoProducto} -> ${'$'}{imageUrl} status=${'$'}{resp.code}")
                        resp.close()
                    }
                } catch (t: Throwable) {
                    Log.w("Sync", "Image HEAD check failed for codigo:${'$'}{dto.codigoProducto}", t)
                }
            }

            // Helper: sanitize product name into a drawable resource name candidate
            fun sanitizeDrawableName(input: String): String {
                var s = input.lowercase()
                // Remove accents
                s = Normalizer.normalize(s, Normalizer.Form.NFD).replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
                // Replace non-alphanumeric with underscore
                s = s.replace("[^a-z0-9]+".toRegex(), "_")
                // Trim underscores
                s = s.trim('_')
                // Collapse multiple underscores
                s = s.replace("_+".toRegex(), "_")
                return s
            }

            // Build a set of available drawable resource names for fuzzy matching
            val availableDrawables: Set<String> = try {
                R.drawable::class.java.fields.map { it.name }.toSet()
            } catch (t: Throwable) {
                emptySet()
            }

            // Build simplified map for available drawables (strip non-alnum and vowels) to increase matching chances
            fun simplifyForMatch(s: String): String {
                val noAccents = Normalizer.normalize(s, Normalizer.Form.NFD).replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
                val cleaned = noAccents.replace("[^a-z0-9]".toRegex(), "").lowercase()
                // remove vowels to make matching less strict (e.g., 'torta' -> 'trt')
                return cleaned.replace("[aeiou]".toRegex(), "")
            }

            val simplifiedDrawableIndex: Map<String, String> = try {
                availableDrawables.associateBy { d -> simplifyForMatch(d) }
            } catch (t: Throwable) {
                emptyMap()
            }

            // Helper: compute Levenshtein distance for approximate matching
            fun levenshtein(a: String, b: String): Int {
                if (a == b) return 0
                if (a.isEmpty()) return b.length
                if (b.isEmpty()) return a.length
                val dp = Array(a.length + 1) { IntArray(b.length + 1) }
                for (i in 0..a.length) dp[i][0] = i
                for (j in 0..b.length) dp[0][j] = j
                for (i in 1..a.length) {
                    for (j in 1..b.length) {
                        val cost = if (a[i-1] == b[j-1]) 0 else 1
                        dp[i][j] = minOf(dp[i-1][j] + 1, dp[i][j-1] + 1, dp[i-1][j-1] + cost)
                    }
                }
                return dp[a.length][b.length]
            }

            fun findBestApproximateDrawable(candidate: String): Pair<String, Int>? {
                if (candidate.isBlank() || availableDrawables.isEmpty()) return null
                val candClean = candidate.replace("[^a-z0-9]".toRegex(), "").lowercase()
                var best: String? = null
                var bestScore = Int.MAX_VALUE
                for (d in availableDrawables) {
                    val dClean = d.replace("[^a-z0-9]".toRegex(), "").lowercase()
                    val dist = levenshtein(candClean, dClean)
                    if (dist < bestScore) {
                        best = d
                        bestScore = dist
                    }
                }
                return if (best != null) Pair(best, bestScore) else null
            }

            // Load static mapping from assets (optional)
            val assetMap: Map<String, String> = try {
                val json = context.assets.open("drawable_map.json").bufferedReader().use { it.readText() }
                val jo = JSONObject(json)
                jo.keys().asSequence().associateWith { k -> jo.getString(k) }
            } catch (t: Throwable) {
                emptyMap()
            }

            // Load generated mapping persisted in internal storage (created after first successful fuzzy match)
            val generatedFile = File(context.filesDir, "drawable_map_generated.json")
            val generatedMap: MutableMap<String, String> = try {
                if (generatedFile.exists()) {
                    val txt = generatedFile.bufferedReader().use { it.readText() }
                    val jo = JSONObject(txt)
                    jo.keys().asSequence().associateWith { k -> jo.getString(k) }.toMutableMap()
                } else mutableMapOf()
            } catch (t: Throwable) {
                mutableMapOf()
            }

            val mapped = productos.map { dto ->
                val id = parseCodigoToId(dto.codigoProducto)
                val name = dto.nombreProducto ?: ""

                    // Build candidate drawable names to try (in order):
                    // 1) product code (raw), 2) image file basename (without extension), 3) sanitized product name
                    val candidates = mutableListOf<String>()
                    dto.codigoProducto?.let { candidates.add(it.lowercase().replace("[^a-z0-9]+".toRegex(), "_")) }
                    dto.imagenProducto?.let { img ->
                        // extract filename without extension and sanitize
                        val base = img.substringAfterLast('/').substringBeforeLast('.')
                        if (base.isNotBlank()) candidates.add(base.lowercase().replace("[^a-z0-9]+".toRegex(), "_"))
                    }
                    val nameCandidate = sanitizeDrawableName(name)
                    if (nameCandidate.isNotBlank()) candidates.add(nameCandidate)

                    var foundRes = 0
                    var matchedCandidate: String? = null

                    fun tryResolve(nameAttempt: String): Int {
                        if (nameAttempt.isBlank()) return 0
                        val cleaned = nameAttempt.trim().replace("_+".toRegex(), "_")
                        // direct resource lookup
                        val direct = context.resources.getIdentifier(cleaned, "drawable", context.packageName)
                        if (direct != 0) return direct
                        return 0
                    }

                    // 1) try exact candidates in order
                    for (candidateName in candidates) {
                        val attemptRes = tryResolve(candidateName)
                        Log.d("Sync", "Trying drawable candidate (exact) '$candidateName' -> resId=$attemptRes for product id=$id")
                        if (attemptRes != 0) {
                            foundRes = attemptRes
                            matchedCandidate = candidateName
                            break
                        }
                    }

                    // 2) fuzzy: try variations and substring matches against available drawables
                    if (foundRes == 0 && availableDrawables.isNotEmpty()) {
                        val altCandidates = candidates.flatMap { listOf(it, it.replace("_", ""), it.replace("_", "-")) }
                        // try any drawable that contains candidate substring
                        run loop@{
                            for (cand in altCandidates) {
                                if (cand.isBlank()) continue
                                val matches = availableDrawables.filter { it.contains(cand, ignoreCase = true) }
                                if (matches.isNotEmpty()) {
                                    val chosen = matches.first()
                                    val res = context.resources.getIdentifier(chosen, "drawable", context.packageName)
                                    if (res != 0) {
                                        foundRes = res
                                        matchedCandidate = chosen
                                        Log.d("Sync", "Fuzzy matched drawable '$chosen' for candidate '$cand' -> resId=$res")
                                        return@loop
                                    }
                                }
                            }
                        }
                        // 3) aggressive simplifed matching: remove vowels and non-alnum, then try index
                        if (foundRes == 0) {
                                                    // 4) approximate Levenshtein matching: find best drawable by edit distance
                                                    if (foundRes == 0) {
                                                        for (cand in candidates) {
                                                            val approx = findBestApproximateDrawable(cand)
                                                            if (approx != null) {
                                                                val (matchedName, score) = approx
                                                                // normalized threshold: allow match if score is reasonably small relative to length
                                                                val threshold = maxOf(1, (matchedName.length * 0.4).toInt())
                                                                Log.d("Sync", "Approx match attempt: candidate='$cand' matchedName='$matchedName' score=$score threshold=$threshold")
                                                                if (score <= threshold) {
                                                                    val res = context.resources.getIdentifier(matchedName, "drawable", context.packageName)
                                                                    if (res != 0) {
                                                                        foundRes = res
                                                                        matchedCandidate = matchedName
                                                                        Log.d("Sync", "Approx matched drawable '$matchedName' for candidate '$cand' -> resId=$res (score=$score)")
                                                                        break
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                            for (cand in candidates) {
                                val simple = simplifyForMatch(cand)
                                if (simple.isBlank()) continue
                                val matched = simplifiedDrawableIndex[simple]
                                if (matched != null) {
                                    val res = context.resources.getIdentifier(matched, "drawable", context.packageName)
                                    if (res != 0) {
                                        foundRes = res
                                        matchedCandidate = matched
                                        Log.d("Sync", "Simplified matched drawable '$matched' for candidate '$cand' (simple='$simple') -> resId=$res")
                                        break
                                    }
                                }
                            }
                        }
                    }

                    var finalImageRes = if (foundRes != 0) {
                        Log.d("Sync", "Matched drawable '${matchedCandidate}' -> resId=$foundRes for product id=$id name='$name'")
                        foundRes
                    } else {
                        Log.d("Sync", "No local drawable for product id=$id name='$name' (tried candidates: ${candidates.joinToString()}). Using placeholder")
                        R.drawable.avatar_placeholder
                    }

                    // If we didn't have an explicit match but assetMap or generatedMap contains an entry for this product, use it
                    if (finalImageRes == R.drawable.avatar_placeholder) {
                        // check keys in order: codigoProducto, imagenProducto, nombreProducto
                        val lookupKeys = listOf(dto.codigoProducto, dto.imagenProducto, dto.nombreProducto).mapNotNull { it }
                        var mappedResName: String? = null
                        for (lk in lookupKeys) {
                            if (assetMap.containsKey(lk)) { mappedResName = assetMap[lk]; break }
                            if (generatedMap.containsKey(lk)) { mappedResName = generatedMap[lk]; break }
                        }
                        if (mappedResName != null) {
                            val resFromMap = tryResolve(mappedResName)
                            if (resFromMap != 0) {
                                finalImageRes = resFromMap
                                Log.d("Sync", "Used mapped drawable '$mappedResName' -> resId=$resFromMap for product id=$id")
                            }
                        }
                    }

                    // If we have a fuzzy match and it wasn't already in generatedMap, persist it for next runs
                    if (foundRes != 0) {
                        // prefer codigoProducto as key, fallback to imagenProducto or nombre
                        val mapKey = dto.codigoProducto ?: dto.imagenProducto ?: dto.nombreProducto
                        if (mapKey != null && !generatedMap.containsKey(mapKey)) {
                            generatedMap[mapKey] = (matchedCandidate ?: "")
                            try {
                                val jo = JSONObject()
                                for ((k, v) in generatedMap) jo.put(k, v)
                                generatedFile.bufferedWriter().use { it.write(jo.toString()) }
                                Log.d("Sync", "Persisted generated drawable map entry: $mapKey -> ${matchedCandidate}")
                            } catch (t: Throwable) {
                                Log.w("Sync", "Failed to persist generated drawable map", t)
                            }
                        }
                    }

                // Determine if we should store a remote image URL when no local drawable was matched.
                val rawImageForEntity = dto.imagenProducto
                val computedImageUrl = rawImageForEntity?.let { path -> if (path.startsWith("/")) "http://10.0.2.2:8080${path}" else path }
                // If we resolved to placeholder but a remote URL exists, store the URL and set imageRes to 0
                val imageUrlToStore: String? = if (finalImageRes == R.drawable.avatar_placeholder) computedImageUrl else null
                val imageResToStore: Int = if (imageUrlToStore != null) 0 else finalImageRes

                ProductEntity(
                    id = id,
                    // Persist server product code so remote updates can identify the server record
                    codigo = dto.codigoProducto,
                    name = name,
                    price = dto.precioProducto?.toDouble() ?: 0.0,
                    oldPrice = null,
                    rating = 4.5f,
                    reviews = 0,
                    imageRes = imageResToStore,
                    imageUrl = imageUrlToStore,
                    stock = dto.stock ?: 0,
                    category = dto.categoriaNombre ?: "General",
                    description = dto.descripcionProducto
                )
            }

            // Clear local products first to remove any previous seed data, then upsert remote products
            db.productDao().clearProducts()
            db.productDao().upsertProducts(mapped)
            Log.d("Sync", "Synced ${'$'}{mapped.size} products from remote to local DB (replaced local seed)")
            return true
        } catch (e: Exception) {
            Log.e("Sync", "Failed to sync products", e)
            return false
        }
    }

    private fun parseCodigoToId(codigo: String?): Int {
        if (codigo == null) return generateFallbackId(codigo)
        return try {
            codigo.toInt()
        } catch (t: Throwable) {
            generateFallbackId(codigo)
        }
    }

    private fun generateFallbackId(codigo: String?): Int {
        val base = codigo?.hashCode() ?: System.currentTimeMillis().toInt()
        return kotlin.math.abs(base)
    }
}
