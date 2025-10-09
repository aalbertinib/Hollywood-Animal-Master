# 🚀 Release & CI/CD Guide

This document explains how to use the GitHub Actions CI/CD pipelines for Hollywood Animals Master.

## 📋 Table of Contents

- [Overview](#overview)
- [Workflows](#workflows)
- [Creating a Release](#creating-a-release)
- [Setting Up Secrets](#setting-up-secrets)
- [GitHub Pages Setup](#github-pages-setup)
- [Manual Workflow Triggers](#manual-workflow-triggers)

---

## Overview

The project uses GitHub Actions for:
- ✅ **Continuous Integration (CI)** - Automated testing on every push/PR
- 📦 **Release Builds** - Multi-platform builds on release creation
- 🌐 **Web Deployment** - Automatic deployment to GitHub Pages

---

## Workflows

### 1. CI Workflow (`ci.yml`)

**Triggers:**
- Push to `main` or `develop` branches
- Pull requests to `main` or `develop`

**Jobs:**
- **Build Desktop (JVM)** - Compiles Kotlin/JVM code and runs tests
- **Build Android** - Creates debug APK and uploads as artifact
- **Build Web** - Compiles WASM build and uploads as artifact
- **Code Quality** - Runs linting and quality checks

**Artifacts:**
- `android-debug-apk` - Android debug APK (7 days retention)
- `web-wasm-build` - Web WASM build (7 days retention)

### 2. Release Workflow (`release.yml`)

**Triggers:**
- GitHub Release published
- Manual workflow dispatch (with version input)

**Jobs:**
- **Build Desktop** - Creates packages for Windows, macOS, and Linux
- **Build Android** - Creates release APK (signed if keystore provided)
- **Build Web** - Creates WASM production build
- **Create Release Notes** - Updates release with download information

**Artifacts:**
- All builds uploaded as release assets
- 30 days retention for artifacts

### 3. Deploy Web Workflow (`deploy-web.yml`)

**Triggers:**
- GitHub Release published
- Push to `main` branch (when app code changes)
- Manual workflow dispatch

**Jobs:**
- Builds WASM production bundle
- Deploys to GitHub Pages
- Creates index.html with auto-redirect
- Creates custom 404 page

---

## Creating a Release

### Using Git Flow

1. **Create a release branch:**
   ```bash
   git checkout -b release/1.0.0 develop
   ```

2. **Update version information:**
   - Update version in `gradle.properties` or version file
   - Update `README.md` if needed
   - Commit changes:
     ```bash
     git commit -am "Bump version to 1.0.0"
     ```

3. **Merge to main:**
   ```bash
   git checkout main
   git merge --no-ff release/1.0.0
   git tag -a v1.0.0 -m "Release version 1.0.0"
   git push origin main --tags
   ```

4. **Create GitHub Release:**
   - Go to: `https://github.com/<username>/Hollywood-Animals-Master/releases/new`
   - Select tag: `v1.0.0`
   - Release title: `Hollywood Animals Master v1.0.0`
   - Add release notes describing changes
   - Click "Publish release"

5. **Automatic builds start:**
   - Desktop builds (Windows, macOS, Linux)
   - Android APK
   - Web WASM
   - GitHub Pages deployment

### Release Notes Template

```markdown
## 🎬 Hollywood Animals Master v1.0.0

### ✨ New Features
- Feature 1 description
- Feature 2 description

### 🐛 Bug Fixes
- Fix 1 description
- Fix 2 description

### 🎨 Improvements
- Improvement 1
- Improvement 2

### 📚 Documentation
- Updated documentation
- Added examples

---

**For Hollywood Animal players:** This tool helps you calculate optimal theater seat distribution for your movies!
```

---

## Setting Up Secrets

For signed Android releases, add these secrets to your GitHub repository:

### Repository Secrets

1. **Go to:** `Settings > Secrets and variables > Actions`
2. **Add the following secrets:**

#### Android Signing (Optional but Recommended)

| Secret Name | Description | How to Get |
|------------|-------------|------------|
| `KEYSTORE_BASE64` | Base64-encoded keystore file | `base64 -w 0 your-keystore.jks` |
| `KEYSTORE_PASSWORD` | Keystore password | Your keystore password |
| `KEY_ALIAS` | Key alias | Your key alias |
| `KEY_PASSWORD` | Key password | Your key password |

#### Creating a Keystore (First Time)

```bash
# Generate keystore
keytool -genkey -v -keystore release-keystore.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias hollywood-animals-key

# Convert to base64 for GitHub secret
base64 -w 0 release-keystore.jks > keystore.base64.txt

# Copy content of keystore.base64.txt to KEYSTORE_BASE64 secret
```

**⚠️ Important:** 
- Keep your keystore file safe and backed up!
- Never commit keystore files to git
- Use strong passwords

---

## GitHub Pages Setup

### Enable GitHub Pages

1. **Go to:** `Settings > Pages`
2. **Source:** Select "GitHub Actions"
3. **Save**

### Access Your Web App

After the first successful deployment:
- **URL:** `https://<username>.github.io/Hollywood-Animals-Master/`
- **Custom Domain (Optional):** Configure in Pages settings

### Update README

Replace `<username>` in README.md with your actual GitHub username:

```markdown
**Live Demo**: Available on [GitHub Pages](https://YOUR_USERNAME.github.io/Hollywood-Animals-Master/)
```

---

## Manual Workflow Triggers

### Trigger Release Build Manually

1. **Go to:** `Actions > Release Build`
2. **Click:** "Run workflow"
3. **Enter:** Version number (e.g., `1.0.0`)
4. **Click:** "Run workflow"

This creates all platform builds without creating a GitHub release.

### Trigger Web Deployment Manually

1. **Go to:** `Actions > Deploy Web App to GitHub Pages`
2. **Click:** "Run workflow"
3. **Select:** Branch (usually `main`)
4. **Click:** "Run workflow"

This redeploys the web app to GitHub Pages.

---

## Platform-Specific Notes

### Desktop (Windows, macOS, Linux)

**Output:** ZIP files containing executable JAR and launcher scripts

**Distribution:**
1. Extract ZIP file
2. Run the platform-specific launcher:
   - Windows: `hollywood-animals-master.bat`
   - macOS/Linux: `./hollywood-animals-master`

### Android

**Output:** APK file

**Types:**
- **Debug APK** - For testing (from CI builds)
- **Release APK** - For distribution (from releases)
  - Unsigned (if no keystore)
  - Signed (if keystore configured)

**Installation:**
```bash
adb install hollywood-animals-master-*.apk
```

### Web (WASM)

**Output:** 
- ZIP with static files (for self-hosting)
- Auto-deployed to GitHub Pages

**Self-Hosting:**
1. Extract ZIP
2. Serve with any static file server:
   ```bash
   # Python
   python -m http.server 8080
   
   # Node.js
   npx serve
   
   # Nginx/Apache - copy files to web root
   ```

---

## Troubleshooting

### Build Fails on CI

**Check:**
1. Gradle build works locally: `./gradlew build`
2. All dependencies are in repositories
3. No platform-specific code in common module
4. Check Actions logs for specific errors

### Android Signing Fails

**Solutions:**
- Verify all 4 secrets are set correctly
- Check keystore password is correct
- Ensure base64 encoding has no line breaks (`-w 0`)
- Build will continue with unsigned APK if signing fails

### GitHub Pages Not Working

**Solutions:**
1. Ensure Pages is enabled in repository settings
2. Check workflow completed successfully
3. Wait 5-10 minutes for propagation
4. Clear browser cache
5. Check Pages build/deployment logs

### Desktop Build Doesn't Run

**Solutions:**
- Ensure Java 17+ is installed
- Check file permissions (Unix: `chmod +x launcher.sh`)
- Run JAR directly: `java -jar composeApp.jar`

---

## CI/CD Best Practices

### Before Merging to Main

- ✅ All CI checks pass
- ✅ Code reviewed
- ✅ Tests added for new features
- ✅ Documentation updated

### Release Checklist

- [ ] Version number updated
- [ ] CHANGELOG updated
- [ ] All tests passing
- [ ] Desktop app tested locally
- [ ] Android app tested on device/emulator
- [ ] Web app tested in browser
- [ ] Release notes prepared
- [ ] Tag created and pushed

### Versioning Scheme

Follow [Semantic Versioning](https://semver.org/):
- **MAJOR.MINOR.PATCH** (e.g., `1.2.3`)
  - **MAJOR** - Breaking changes
  - **MINOR** - New features (backwards compatible)
  - **PATCH** - Bug fixes

---

## Support

For issues with CI/CD:
1. Check [Actions](https://github.com/<username>/Hollywood-Animals-Master/actions) logs
2. Review this guide
3. Check [GitHub Actions documentation](https://docs.github.com/en/actions)
4. Open an issue

---

**Built with ❤️ for Hollywood Animal players** 🎬
