package com.example.appmilsabores.network

import okhttp3.Interceptor
import okhttp3.Response
import com.example.appmilsabores.data.prefs.SessionPreferencesDataSource

/**
 * Adds Authorization header when an in-memory token is available.
 * Reads token from SessionPreferencesDataSource.TOKEN to avoid suspending calls.
 */
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = SessionPreferencesDataSource.TOKEN
        return if (!token.isNullOrBlank()) {
            val newReq = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newReq)
        } else {
            chain.proceed(request)
        }
    }
}
