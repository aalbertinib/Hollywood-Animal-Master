# PowerShell script to extract project version from gradle.properties
# Usage: .\scripts\get-version.ps1

$gradlePropsPath = Join-Path $PSScriptRoot "..\gradle.properties"

if (-not (Test-Path $gradlePropsPath)) {
    Write-Error "gradle.properties not found at: $gradlePropsPath"
    exit 1
}

$content = Get-Content $gradlePropsPath
$versionLine = $content | Where-Object { $_ -match "^project\.version=" }

if ($versionLine) {
    $version = $versionLine -replace "^project\.version=", ""
    Write-Output $version
} else {
    Write-Error "Could not find project.version in gradle.properties"
    exit 1
}
