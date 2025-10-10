# Setup Script for Semantic Release
# Run this script to initialize semantic-release in your project

Write-Host "🚀 Setting up Semantic Release..." -ForegroundColor Cyan
Write-Host ""

# Check if Node.js is installed
Write-Host "Checking Node.js installation..." -ForegroundColor Yellow
try {
    $nodeVersion = node --version
    Write-Host "✅ Node.js $nodeVersion is installed" -ForegroundColor Green
} catch {
    Write-Host "❌ Node.js is not installed!" -ForegroundColor Red
    Write-Host "Please install Node.js from https://nodejs.org/" -ForegroundColor Red
    exit 1
}

# Check if npm is installed
try {
    $npmVersion = npm --version
    Write-Host "✅ npm $npmVersion is installed" -ForegroundColor Green
} catch {
    Write-Host "❌ npm is not installed!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Installing dependencies..." -ForegroundColor Yellow

# Install dependencies
try {
    npm install
    Write-Host "✅ Dependencies installed successfully" -ForegroundColor Green
} catch {
    Write-Host "❌ Failed to install dependencies" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "🎉 Semantic Release setup complete!" -ForegroundColor Green
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Cyan
Write-Host "1. Review the documentation: docs/SEMANTIC_RELEASE.md" -ForegroundColor White
Write-Host "2. Make a commit using conventional format:" -ForegroundColor White
Write-Host "   git commit -m 'feat: add new feature'" -ForegroundColor Gray
Write-Host "3. Push to main branch:" -ForegroundColor White
Write-Host "   git push origin main" -ForegroundColor Gray
Write-Host "4. Watch the GitHub Actions workflow create your release!" -ForegroundColor White
Write-Host ""
Write-Host "Commit message examples:" -ForegroundColor Cyan
Write-Host "  fix: resolve calculation bug" -ForegroundColor Gray
Write-Host "  feat: add dark mode" -ForegroundColor Gray
Write-Host "  feat!: breaking API change" -ForegroundColor Gray
Write-Host ""
Write-Host "For more info, see: https://www.conventionalcommits.org/" -ForegroundColor Cyan
