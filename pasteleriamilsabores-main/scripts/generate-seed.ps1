Param()

Set-StrictMode -Version Latest

$base = Split-Path -Parent $MyInvocation.MyCommand.Definition
$projectRoot = Resolve-Path "$base\.." | Select-Object -ExpandProperty Path
$dataDir = Join-Path $projectRoot 'src\main\java\com\service\data'
$outFile = Join-Path $projectRoot 'target\seed.sql'

If (-Not (Test-Path $dataDir)) {
    Write-Error "Data directory not found: $dataDir"
    exit 1
}

New-Item -ItemType Directory -Force -Path (Split-Path $outFile) | Out-Null

function Esc([string]$s) {
    if ($null -eq $s) { return "" }
    return $s -replace "'", "''"
}

"-- SQL seed generated from JSON" | Out-File -FilePath $outFile -Encoding utf8
"SET NAMES utf8mb4;" | Out-File -FilePath $outFile -Encoding utf8 -Append
"USE pasteleria;" | Out-File -FilePath $outFile -Encoding utf8 -Append
"" | Out-File -FilePath $outFile -Encoding utf8 -Append

function Write-JsonInserts($jsonPath, $writer) {
    if (-Not (Test-Path $jsonPath)) { return }
    $content = Get-Content $jsonPath -Raw | ConvertFrom-Json
    return $content
}

# Blogs
$blogs = Write-JsonInserts (Join-Path $dataDir 'blogs.json')
foreach ($b in $blogs) {
    $id = Esc $b.id
    $titulo = Esc $b.titulo
    $resumen = Esc $b.resumen
    $imagen = Esc $b.imagen
    $fecha = Esc $b.fecha
    $autor = Esc $b.autor
    $link = Esc $b.link
    "INSERT INTO blogs (id,titulo,resumen,imagen,fecha,autor,link) VALUES ('$id','$titulo','$resumen','$imagen','$fecha','$autor','$link');" | Out-File -FilePath $outFile -Append -Encoding utf8
}

"" | Out-File -FilePath $outFile -Append -Encoding utf8

# Recetas
$recetas = Write-JsonInserts (Join-Path $dataDir 'recetas.json')
foreach ($r in $recetas) {
    $id = Esc $r.id
    $titulo = Esc $r.titulo
    $resumen = Esc $r.resumen
    $ingredientes = Esc $r.ingredientes
    $preparacion = Esc $r.preparacion
    $imagen = Esc $r.imagen
    $badge = Esc $r.badge
    $badgeClass = Esc $r.badgeClass
    "INSERT INTO recetas (id,titulo,resumen,ingredientes,preparacion,imagen,badge,badge_class) VALUES ('$id','$titulo','$resumen','$ingredientes','$preparacion','$imagen','$badge','$badgeClass');" | Out-File -FilePath $outFile -Append -Encoding utf8
}

"" | Out-File -FilePath $outFile -Append -Encoding utf8

# Regiones y comunas
$regionesPath = Join-Path $dataDir 'regionesComunas\regionesComunas.json'
if (Test-Path $regionesPath) {
    $regiones = Get-Content $regionesPath -Raw | ConvertFrom-Json
    foreach ($rc in $regiones) {
        $id = Esc $rc.id
        $region = Esc $rc.region
        "INSERT INTO regiones_comunas (id,region) VALUES ('$id','$region');" | Out-File -FilePath $outFile -Append -Encoding utf8
        if ($rc.comunas) {
            foreach ($c in $rc.comunas) {
                $comuna = Esc $c
                "INSERT INTO regiones_comunas_comunas (region_id,comuna) VALUES ('$id','$comuna');" | Out-File -FilePath $outFile -Append -Encoding utf8
            }
        }
    }
}

"" | Out-File -FilePath $outFile -Append -Encoding utf8

# Categorias y Productos
$productosPath = Join-Path $dataDir 'products\productos.json'
if (Test-Path $productosPath) {
    $root = Get-Content $productosPath -Raw | ConvertFrom-Json
    $tempCatId = 1000
    foreach ($cat in $root.categorias) {
        $catId = $tempCatId
        $tempCatId++
        $nombreCat = Esc $cat.nombre_categoria
        "INSERT INTO categorias (id_categoria,nombre_categoria) VALUES ($catId,'$nombreCat');" | Out-File -FilePath $outFile -Append -Encoding utf8
        foreach ($p in $cat.productos) {
            $codigo = Esc $p.codigo_producto
            $nombre = Esc $p.nombre_producto
            $precio = if ($p.precio_producto -ne $null) { $p.precio_producto } else { 'NULL' }
            $descripcion = Esc $p.'descripción_producto'
            $imagen = Esc $p.imagen_producto
            $stock = if ($p.stock -ne $null) { $p.stock } else { 'NULL' }
            $stockCrit = if ($p.stock_critico -ne $null) { $p.stock_critico } else { 'NULL' }
            "INSERT INTO productos (codigo_producto,nombre_producto,precio_producto,descripcion_producto,imagen_producto,stock,stock_critico,categoria_id) VALUES ('$codigo','$nombre',$precio,'$descripcion','$imagen',$stock,$stockCrit,$catId);" | Out-File -FilePath $outFile -Append -Encoding utf8
        }
    }
}

"" | Out-File -FilePath $outFile -Append -Encoding utf8

# Usuarios y Direcciones (con auto-fill de campos faltantes)
$usersPath = Join-Path $dataDir 'users\users.json'
if (Test-Path $usersPath) {
    $users = Get-Content $usersPath -Raw | ConvertFrom-Json
    foreach ($u in $users) {
        $run = Esc $u.run
        $nombre = Esc $u.nombre
        $apellidos = Esc $u.apellidos
        $correo = Esc $u.correo
        $fechaNacimiento = Esc $u.fechaNacimiento
        $tipoUsuario = Esc $u.tipoUsuario
        $password = Esc $u.password

        # compute discountPercent by age >=50 if missing
        $discount = $null
        if ($u.discount_percent -ne $null) { $discount = $u.discount_percent }
        else {
            try {
                $dob = [datetime]::Parse($fechaNacimiento)
                $age = (Get-Date).Year - $dob.Year
                if ((Get-Date).Month -lt $dob.Month -or ((Get-Date).Month -eq $dob.Month -and (Get-Date).Day -lt $dob.Day)) { $age = $age - 1 }
                if ($age -ge 50) { $discount = 50 }
            } catch { $discount = $null }
        }

        # free cake eligibility based on email domain
        $freeCake = $null
        if ($u.free_cake_eligible -ne $null) { $freeCake = if ($u.free_cake_eligible) { 1 } else { 0 } }
        else { $freeCake = if ($correo -and $correo.ToLower().Contains('duoc.cl')) { 1 } else { 0 } }

        # lifetime discount from registration code
        $lifetime = $null
        if ($u.lifetime_discount_percent -ne $null) { $lifetime = $u.lifetime_discount_percent }
        elseif ($u.registration_code -ne $null -and $u.registration_code -match '^FELICES50$') { $lifetime = 10 }

        $regCode = if ($u.registration_code -ne $null) { Esc $u.registration_code } else { $null }
        $regDate = if ($u.registration_date -ne $null) { Esc $u.registration_date } else { $null }
        if ($regCode -ne $null -and (-not $regDate)) { $regDate = (Get-Date).ToString('yyyy-MM-dd') }

        $discountSql = if ($discount -ne $null) { $discount } else { 'NULL' }
        $lifetimeSql = if ($lifetime -ne $null) { $lifetime } else { 'NULL' }
        $freeCakeSql = if ($freeCake -ne $null) { $freeCake } else { 'NULL' }
        $regCodeSql = if ($regCode -ne $null) { "'$regCode'" } else { 'NULL' }
        $regDateSql = if ($regDate -ne $null) { "'$regDate'" } else { 'NULL' }

        "INSERT INTO usuarios (run,nombre,apellidos,correo,fecha_nacimiento,tipo_usuario,password,porcentaje_descuento,porcentaje_descuento_permanente,torta_gratis_elegible,codigo_registro,fecha_registro) VALUES ('$run','$nombre','$apellidos','$correo','$fechaNacimiento','$tipoUsuario','$password',$discountSql,$lifetimeSql,$freeCakeSql,$regCodeSql,$regDateSql);" | Out-File -FilePath $outFile -Append -Encoding utf8

        if ($u.addresses) {
            foreach ($a in $u.addresses) {
                $aid = Esc $a.id
                $addr = Esc $a.address
                $region = Esc $a.region
                $comuna = Esc $a.comuna
                "INSERT INTO direcciones (id,direccion,region,comuna,usuario_run) VALUES ('$aid','$addr','$region','$comuna','$run');" | Out-File -FilePath $outFile -Append -Encoding utf8
            }
        }
    }
}

Write-Output "Seed SQL generated: $outFile"
# Generate SQL seed from JSON files (PowerShell)
# Usage: pwsh .\scripts\generate-seed.ps1

Set-StrictMode -Version Latest

$root = Join-Path $PSScriptRoot ".." | Resolve-Path | Select-Object -ExpandProperty Path
$dataDir = Join-Path $root "src\main\java\com\service\data"
$outFile = Join-Path $root "target\seed.sql"

if (-not (Test-Path $dataDir)) {
    Write-Error "Data directory not found: $dataDir"
    exit 1
}

New-Item -ItemType Directory -Force -Path (Split-Path $outFile) | Out-Null

function Esc([string]$s) {
    if ($null -eq $s) { return "" }
    return $s -replace "'","''"
}

$out = New-Object System.Text.StringBuilder
$out.AppendLine('-- SQL seed generated from JSON') | Out-Null
$out.AppendLine('SET NAMES utf8mb4;') | Out-Null
$out.AppendLine('USE pasteleria;') | Out-Null
$out.AppendLine('') | Out-Null

# Blogs
$blogsFile = Join-Path $dataDir 'blogs.json'
if (Test-Path $blogsFile) {
    $blogs = Get-Content $blogsFile -Raw | ConvertFrom-Json
    foreach ($b in $blogs) {
        $id = Esc($b.id)
        $titulo = Esc($b.titulo)
        $resumen = Esc($b.resumen)
        $imagen = Esc($b.imagen)
        $fecha = Esc($b.fecha)
        $autor = Esc($b.autor)
        $link = Esc($b.link)
        $out.AppendLine("INSERT INTO blogs (id,titulo,resumen,imagen,fecha,autor,link) VALUES ('$id','$titulo','$resumen','$imagen','$fecha','$autor','$link');") | Out-Null
    }
    $out.AppendLine('') | Out-Null
}

# Recetas
$recetasFile = Join-Path $dataDir 'recetas.json'
if (Test-Path $recetasFile) {
    $recetas = Get-Content $recetasFile -Raw | ConvertFrom-Json
    foreach ($r in $recetas) {
        $id = Esc($r.id)
        $titulo = Esc($r.titulo)
        $resumen = Esc($r.resumen)
        $ingredientes = Esc($r.ingredientes)
        $preparacion = Esc($r.preparacion)
        $imagen = Esc($r.imagen)
        $badge = Esc($r.badge)
        $badgeClass = Esc($r.badgeClass)
        $out.AppendLine("INSERT INTO recetas (id,titulo,resumen,ingredientes,preparacion,imagen,badge,badge_class) VALUES ('$id','$titulo','$resumen','$ingredientes','$preparacion','$imagen','$badge','$badgeClass');") | Out-Null
    }
    $out.AppendLine('') | Out-Null
}

# Regiones y comunas
$regFile = Join-Path $dataDir 'regionesComunas\regionesComunas.json'
if (Test-Path $regFile) {
    $regs = Get-Content $regFile -Raw | ConvertFrom-Json
    foreach ($rc in $regs) {
        $id = Esc($rc.id)
        $region = Esc($rc.region)
        $out.AppendLine("INSERT INTO regiones_comunas (id,region) VALUES ('$id','$region');") | Out-Null
        if ($rc.comunas) {
            foreach ($c in $rc.comunas) {
                $com = Esc($c)
                $out.AppendLine("INSERT INTO regiones_comunas_comunas (region_id,comuna) VALUES ('$id','$com');") | Out-Null
            }
        }
    }
    $out.AppendLine('') | Out-Null
}

# Categorias y productos
$prodFile = Join-Path $dataDir 'products\productos.json'
if (Test-Path $prodFile) {
    $rootJson = Get-Content $prodFile -Raw | ConvertFrom-Json
    $tempCatId = 1000
    if ($rootJson.categorias) {
        foreach ($cat in $rootJson.categorias) {
            $catId = $tempCatId
            $tempCatId += 1
            $nombreCat = Esc($cat.nombre_categoria)
            $out.AppendLine("INSERT INTO categorias (id_categoria,nombre_categoria) VALUES ($catId,'$nombreCat');") | Out-Null
            if ($cat.productos) {
                foreach ($p in $cat.productos) {
                    $codigo = Esc($p.codigo_producto)
                    $nombre = Esc($p.nombre_producto)
                    $precio = if ($p.precio_producto -ne $null) { $p.precio_producto } else { 'NULL' }
                    $descripcion = Esc($p.'descripción_producto')
                    $imagen = Esc($p.imagen_producto)
                    $stock = if ($p.stock -ne $null) { $p.stock } else { 'NULL' }
                    $stockCrit = if ($p.stock_critico -ne $null) { $p.stock_critico } else { 'NULL' }
                    $out.AppendLine("INSERT INTO productos (codigo_producto,nombre_producto,precio_producto,descripcion_producto,imagen_producto,stock,stock_critico,categoria_id) VALUES ('$codigo','$nombre',$precio,'$descripcion','$imagen',$stock,$stockCrit,$catId);") | Out-Null
                }
            }
        }
    }
    $out.AppendLine('') | Out-Null
}

# Usuarios y direcciones (calcular campos faltantes)
$usersFile = Join-Path $dataDir 'users\users.json'
if (Test-Path $usersFile) {
    $users = Get-Content $usersFile -Raw | ConvertFrom-Json
    foreach ($u in $users) {
        $run = Esc($u.run)
        $nombre = Esc($u.nombre)
        $apellidos = Esc($u.apellidos)
        $correo = Esc($u.correo)
        $fechaNacimiento = Esc($u.fechaNacimiento)
        $tipoUsuario = Esc($u.tipoUsuario)
        $password = Esc($u.password)

        $discount = $null
        if ($u.discount_percent -ne $null) { $discount = [int]$u.discount_percent }
        $lifetime = $null
        if ($u.lifetime_discount_percent -ne $null) { $lifetime = [int]$u.lifetime_discount_percent }
        $freeCake = $null
        if ($u.free_cake_eligible -ne $null) { $freeCake = [bool]$u.free_cake_eligible }
        $regCode = if ($u.registration_code -ne $null) { $u.registration_code } else { $null }
        $regDate = if ($u.registration_date -ne $null) { $u.registration_date } else { $null }

        # Auto-fill
        if ($discount -eq $null -and $fechaNacimiento) {
            try {
                $dob = [datetime]::Parse($fechaNacimiento)
                $age = ((Get-Date) - $dob).Days / 365
                if ($age -ge 50) { $discount = 50 }
            } catch { }
        }
        if ($freeCake -eq $null -and $correo -and $correo.ToLower().Contains('duoc.cl')) { $freeCake = $true }
        if ($lifetime -eq $null -and $regCode -and $regCode.Trim().ToUpper() -eq 'FELICES50') { $lifetime = 10 }
        if ($regCode -and (-not $regDate)) { $regDate = (Get-Date).ToString('yyyy-MM-dd') }

        if ($discount -eq $null) { $discountSql = 'NULL' } else { $discountSql = $discount }
        if ($lifetime -eq $null) { $lifetimeSql = 'NULL' } else { $lifetimeSql = $lifetime }
        if ($freeCake -eq $null) { $freeCakeSql = 'NULL' } elseif ($freeCake) { $freeCakeSql = 1 } else { $freeCakeSql = 0 }
        $regCodeSql = 'NULL'
        if ($regCode -ne $null) { $regCodeSql = "'" + (Esc $regCode) + "'" }
        $regDateSql = 'NULL'
        if ($regDate -ne $null) { $regDateSql = "'" + (Esc $regDate) + "'" }

        $out.AppendLine("INSERT INTO usuarios (run,nombre,apellidos,correo,fecha_nacimiento,tipo_usuario,password,porcentaje_descuento,porcentaje_descuento_permanente,torta_gratis_elegible,codigo_registro,fecha_registro) VALUES ('$run','$nombre','$apellidos','$correo','$fechaNacimiento','$tipoUsuario','$password',$discountSql,$lifetimeSql,$freeCakeSql,$regCodeSql,$regDateSql);") | Out-Null

        if ($u.addresses) {
            foreach ($a in $u.addresses) {
                $id = Esc($a.id)
                $address = Esc($a.address)
                $region = Esc($a.region)
                $comuna = Esc($a.comuna)
                $out.AppendLine("INSERT INTO direcciones (id,direccion,region,comuna,usuario_run) VALUES ('$id','$address','$region','$comuna','$run');") | Out-Null
            }
        }
    }
    $out.AppendLine('') | Out-Null
}

# Save file
[System.IO.File]::WriteAllText($outFile, $out.ToString(), [System.Text.Encoding]::UTF8)
Write-Output "Seed SQL generated: $outFile"

# Show tail
Get-Content $outFile -Tail 100 | ForEach-Object { Write-Output $_ }
