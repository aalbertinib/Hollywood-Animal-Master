# Hollywood Animals Icon Generator
# Requires ImageMagick: https://imagemagick.org/script/download.php

$ErrorActionPreference = "Stop"

Write-Host "🎬 Hollywood Animals Icon Generator" -ForegroundColor Cyan
Write-Host ""

# Check if ImageMagick is installed
try {
    $magickVersion = magick -version 2>$null
    if ($LASTEXITCODE -ne 0) { throw }
    Write-Host "✓ ImageMagick found" -ForegroundColor Green
} catch {
    Write-Host "✗ ImageMagick not found!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please install ImageMagick from: https://imagemagick.org/script/download.php" -ForegroundColor Yellow
    Write-Host "Or use online tool: https://www.appicon.co/" -ForegroundColor Yellow
    exit 1
}

$svgFile = "hollywood-animals-icon.svg"
if (-not (Test-Path $svgFile)) {
    Write-Host "✗ SVG file not found: $svgFile" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Generating icons from: $svgFile" -ForegroundColor Cyan
Write-Host ""

# Android Icons
Write-Host "📱 Generating Android icons..." -ForegroundColor Yellow

$androidSizes = @{
    "mdpi" = 48
    "hdpi" = 72
    "xhdpi" = 96
    "xxhdpi" = 144
    "xxxhdpi" = 192
}

foreach ($density in $androidSizes.Keys) {
    $size = $androidSizes[$density]
    $output = "..\composeApp\src\androidMain\res\mipmap-$density\ic_launcher.png"
    $outputRound = "..\composeApp\src\androidMain\res\mipmap-$density\ic_launcher_round.png"
    
    New-Item -ItemType Directory -Force -Path (Split-Path $output) | Out-Null
    
    Write-Host "  Creating $density (${size}x$size)..." -NoNewline
    magick $svgFile -resize "${size}x$size" $output 2>$null
    Copy-Item $output $outputRound -Force
    Write-Host " ✓" -ForegroundColor Green
}

# iOS Icon
Write-Host ""
Write-Host "🍎 Generating iOS icon..." -ForegroundColor Yellow
$iosOutput = "..\iosApp\iosApp\Assets.xcassets\AppIcon.appiconset\app-icon-1024.png"
New-Item -ItemType Directory -Force -Path (Split-Path $iosOutput) | Out-Null
Write-Host "  Creating 1024x1024..." -NoNewline
magick $svgFile -resize "1024x1024" $iosOutput 2>$null
Write-Host " ✓" -ForegroundColor Green

# Desktop Icons
Write-Host ""
Write-Host "🖥️  Generating Desktop icons..." -ForegroundColor Yellow
$jvmResources = "..\composeApp\src\jvmMain\resources"
New-Item -ItemType Directory -Force -Path $jvmResources | Out-Null

Write-Host "  Creating app-icon.png (256x256)..." -NoNewline
magick $svgFile -resize "256x256" "$jvmResources\app-icon.png" 2>$null
Write-Host " ✓" -ForegroundColor Green

Write-Host "  Creating app-icon.ico (Windows)..." -NoNewline
magick $svgFile -define icon:auto-resize=256,128,96,64,48,32,16 "$jvmResources\app-icon.ico" 2>$null
Write-Host " ✓" -ForegroundColor Green

# Web Icons
Write-Host ""
Write-Host "🌐 Generating Web icons..." -ForegroundColor Yellow
$webResources = "..\composeApp\src\webMain\resources"
New-Item -ItemType Directory -Force -Path $webResources | Out-Null

Write-Host "  Creating favicon.ico..." -NoNewline
magick $svgFile -define icon:auto-resize=32,16 "$webResources\favicon.ico" 2>$null
Write-Host " ✓" -ForegroundColor Green

Write-Host "  Creating apple-touch-icon.png (180x180)..." -NoNewline
magick $svgFile -resize "180x180" "$webResources\apple-touch-icon.png" 2>$null
Write-Host " ✓" -ForegroundColor Green

Write-Host "  Creating web-icon-192.png..." -NoNewline
magick $svgFile -resize "192x192" "$webResources\web-icon-192.png" 2>$null
Write-Host " ✓" -ForegroundColor Green

Write-Host "  Creating web-icon-512.png..." -NoNewline
magick $svgFile -resize "512x512" "$webResources\web-icon-512.png" 2>$null
Write-Host " ✓" -ForegroundColor Green

Write-Host ""
Write-Host "✅ All icons generated successfully!" -ForegroundColor Green
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Cyan
Write-Host "  1. Review generated icons in their respective folders"
Write-Host "  2. Rebuild your project to apply changes"
Write-Host "  3. Test on each platform"
Write-Host ""
