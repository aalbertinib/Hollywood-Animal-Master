#!/bin/bash
# Setup Script for Semantic Release
# Run this script to initialize semantic-release in your project

echo "🚀 Setting up Semantic Release..."
echo ""

# Check if Node.js is installed
echo "Checking Node.js installation..."
if command -v node &> /dev/null; then
    NODE_VERSION=$(node --version)
    echo "✅ Node.js $NODE_VERSION is installed"
else
    echo "❌ Node.js is not installed!"
    echo "Please install Node.js from https://nodejs.org/"
    exit 1
fi

# Check if npm is installed
if command -v npm &> /dev/null; then
    NPM_VERSION=$(npm --version)
    echo "✅ npm $NPM_VERSION is installed"
else
    echo "❌ npm is not installed!"
    exit 1
fi

echo ""
echo "Installing dependencies..."

# Install dependencies
if npm install; then
    echo "✅ Dependencies installed successfully"
else
    echo "❌ Failed to install dependencies"
    exit 1
fi

echo ""
echo "🎉 Semantic Release setup complete!"
echo ""
echo "Next steps:"
echo "1. Review the documentation: docs/SEMANTIC_RELEASE.md"
echo "2. Make a commit using conventional format:"
echo "   git commit -m 'feat: add new feature'"
echo "3. Push to main branch:"
echo "   git push origin main"
echo "4. Watch the GitHub Actions workflow create your release!"
echo ""
echo "Commit message examples:"
echo "  fix: resolve calculation bug"
echo "  feat: add dark mode"
echo "  feat!: breaking API change"
echo ""
echo "For more info, see: https://www.conventionalcommits.org/"
