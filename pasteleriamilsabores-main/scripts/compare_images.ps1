<#
PowerShell script to compare product image base names returned by the local backend
with files present in `src/main/resources/static/images/products`.

Usage (from repo root):
  powershell -ExecutionPolicy Bypass -File .\scripts\compare_images.ps1

You can override API url and static folder with parameters.
#>
param(
    [string]$ApiUrl = 'http://localhost:8080/api/productos',
    [string]$StaticDir = '.\\src\\main\\resources\\static\\images\\products'
)

Write-Host "Fetching products from: $ApiUrl" -ForegroundColor Cyan
try {
    $products = Invoke-RestMethod -Uri $ApiUrl -UseBasicParsing
} catch {
    Write-Error "Failed to fetch $ApiUrl - $_"
    exit 2
}

if (-not $products) {
    Write-Error "No products returned from API"
    exit 3
}

# Normalize products into a list of base names (imagen_producto) when available;
# if not present, try to extract from imagen_url or imagen_producto-like strings.
function Get-BaseFromRaw([string]$raw) {
    if (-not $raw) { return $null }
    $s = $raw.Trim()
    # if contains '/', take last segment
    $idx = $s.LastIndexOf('/')
    if ($idx -ge 0 -and $idx -lt $s.Length - 1) { $s = $s.Substring($idx + 1) }
    # remove extension
    $dot = $s.LastIndexOf('.')
    if ($dot -gt 0) { $s = $s.Substring(0, $dot) }
    return $s
}

# Collect base names from API
$apiBases = @{}

foreach ($p in $products) {
    # prefer imagen_producto property if present
    $base = $null
    if ($p.imagen_producto) { $base = Get-BaseFromRaw($p.imagen_producto) }
    if ((-not $base) -and $p.imagen_url) { $base = Get-BaseFromRaw($p.imagen_url) }
    if ((-not $base) -and $p.imagenProducto) { $base = Get-BaseFromRaw($p.imagenProducto) }
    if ($base) {
        $apiBases[$base] = $p
    }
}

Write-Host "Found $($apiBases.Keys.Count) distinct image base names in API response." -ForegroundColor Green

# List files in static dir
if (-not (Test-Path $StaticDir)) {
    Write-Error "Static directory not found: $StaticDir"
    exit 4
}

$files = Get-ChildItem -Path $StaticDir -File -Recurse | Where-Object { $_.Length -gt 0 }
$staticBases = $files | ForEach-Object { $_.BaseName } | Sort-Object -Unique
Write-Host "Found $($staticBases.Count) image files in static folder." -ForegroundColor Green

# Compute missing: API base names that don't exist as files
$missing = @()
foreach ($b in $apiBases.Keys) {
    if (-not ($staticBases -contains $b)) { $missing += $b }
}

if ($missing.Count -eq 0) {
    Write-Host "All API image base names have matching files in the static folder." -ForegroundColor Green
} else {
    Write-Host "== MISSING FILES for API image bases ($($missing.Count)) ==" -ForegroundColor Yellow
    foreach ($m in $missing) {
        $product = $apiBases[$m]
        Write-Host "- $m    -> product code: $($product.codigo_producto)   name: $($product.nombre_producto)" -ForegroundColor Yellow
    }
}

# Files present but not referenced by API
$unused = @()
foreach ($f in $staticBases) {
    if (-not ($apiBases.Keys -contains $f)) { $unused += $f }
}

Write-Host "\nFound $($unused.Count) files in static folder not referenced by API." -ForegroundColor Cyan
if ($unused.Count -gt 0) {
    Write-Host "Examples (up to 50):" -ForegroundColor Cyan
    $unused[0..([Math]::Min(49, $unused.Count - 1))] | ForEach-Object { Write-Host "- $_" }
}

# Suggest sample curl/webrequest commands for the first few API bases
Write-Host "\nSample checks (use emulator host http://10.0.2.2:8080 when testing from Android emulator):" -ForegroundColor Magenta
$count = 0
foreach ($b in $apiBases.Keys) {
    if ($count -ge 5) { break }
    Write-Host "GET http://localhost:8080/images/products/$b  -> check status code" -ForegroundColor Magenta
    Write-Host "curl -I http://localhost:8080/images/products/$b" -ForegroundColor DarkMagenta
    $count++
}

Write-Host "\nScript finished." -ForegroundColor Green
