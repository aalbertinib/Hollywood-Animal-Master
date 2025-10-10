# 🚀 Release & CI/CD Guide

This document explains how to use the GitHub Actions CI/CD pipelines for Hollywood Animals Master.

## 📋 Table of Contents

- [Overview](#overview)
- [Version Management](#version-management)
- [Workflows](#workflows)
- [Creating a Release](#creating-a-release)
- [Setting Up Secrets](#setting-up-secrets)
- [GitHub Pages Setup](#github-pages-setup)
- [Manual Workflow Triggers](#manual-workflow-triggers)

---

## Version Management

**🎯 All versions managed from one place: `gradle.properties`**

```properties
project.version=1.0.0
```

**Quick Reference:**
- 📖 Full Guide: [VERSION_MANAGEMENT.md](./VERSION_MANAGEMENT.md)
- 📋 Quick Ref: [VERSION_QUICK_REFERENCE.md](./VERSION_QUICK_REFERENCE.md)

**Common Commands:**
```bash
# Print version
./gradlew :composeApp:printVersion

# Sync to iOS
./gradlew :composeApp:syncVersionToIOS
```

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
- **Pull requests** to any branch (required for merge)

**Jobs:**
- **Unit Tests** ⚡ **REQUIRED FOR PR MERGE** - Runs all test suites
- **Build Android** - Creates debug APK and validates build
- **Build Desktop** - Compiles Kotlin/JVM code
- **Build Web** - Compiles WASM build and validates

**Artifacts:**
- `test-results` - Test reports and results (3 days retention)

**✅ PR Requirements:**
- All pull requests **must pass unit tests** before merging
- See [PR_REQUIREMENTS.md](./PR_REQUIREMENTS.md) for branch protection setup

### 2. Release Workflow (`release.yml`)

**Triggers:**
- **Version tags** matching patterns (with or without `v` prefix, case insensitive):
  - `v1.0.0` or `1.0.0` - Production release
  - `v1.0.0-alpha.1` or `1.0.0-ALPHA.1` - Alpha pre-release
  - `v1.0.0-beta.2` or `1.0.0-Beta.2` - Beta pre-release
  - `v1.0.0-rc.3` or `1.0.0-RC.3` - Release candidate
- Manual workflow dispatch

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

> **📋 Version Management:** This project uses centralized version management. See [VERSION_MANAGEMENT.md](./VERSION_MANAGEMENT.md) for complete details.

### Using Git Flow

1. **Create a release branch:**
   ```bash
   git checkout -b release/1.0.0 develop
   ```

2. **Update version information:**
   
   **Edit `gradle.properties`:**
   ```properties
   project.version=1.0.0
   ```
   
   **Sync to iOS (if needed):**
   ```bash
   ./gradlew :composeApp:syncVersionToIOS
   ```
   
   **Verify version:**
   ```bash
   ./gradlew :composeApp:printVersion
   ```
   
   **Commit changes:**
   ```bash
   git add gradle.properties iosApp/Configuration/Config.xcconfig
   git commit -m "Bump version to 1.0.0"
   ```

3. **Merge to main:**
   ```bash
   git checkout main
   git merge --no-ff release/1.0.0
   # Tag with or without 'v' prefix (both work)
   git tag -a v1.0.0 -m "Release version 1.0.0"  # or: git tag 1.0.0
   git push origin main --tags
   ```

4. **Push the tag to trigger release:**
   ```bash
   git push origin v1.0.0  # or: git push origin 1.0.0
   ```
   
   **🚀 Release workflow automatically starts!**

5. **Automatic builds run:**
   - Desktop builds (Windows, macOS, Linux)
   - Android APK
   - Web WASM package
   - **GitHub Pages deployment** (runs in parallel)
   
6. **What happens automatically:**
   - All executables are built and attached to the release
   - Release notes are updated with download links
   - **GitHub Pages link is automatically included** with your repository's actual URL
   - Web app is deployed live within minutes
   - Action summaries provide direct links to deployments

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

**For Hollywood Animal players:** This tool helps you calculate optimal theater screening distribution for your movies!
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
  -alias hollywood-animal-key

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
- **URL:** `https://aalbertinib.github.io/Hollywood-Animal-Master/`
- **Custom Domain (Optional):** Configure in Pages settings

### Update README

Replace `aalbertinib` in README.md with your actual GitHub username:

```markdown
**Live Demo**: Available on [GitHub Pages](https://aalbertinib.github.io/Hollywood-Animal-Master/)
```

**Files to update:**
- Line 18: Badge link
- Line 41: Quick Start link
- Line 194: Links section

### How Releases and GitHub Pages Work Together

When you publish a release:

1. **Release Workflow** (`release.yml`) starts:
   - Builds all platform executables (Desktop, Android, Web)
   - Uploads them as release assets
   - Updates release notes with download links
   - **Automatically includes GitHub Pages URL** (dynamically generated from your repository)

2. **Deploy Web Workflow** (`deploy-web.yml`) triggers in parallel:
   - Builds fresh WASM bundle
   - Deploys to GitHub Pages
   - Updates live web app
   - Links back to release in deployment summary

**Result:** Users can download executables from the release page AND try the web app immediately via the included GitHub Pages link!

---

## Manual Workflow Triggers

### Trigger Release Build Manually

1. **Update version in `gradle.properties`** first:
   ```bash
   nano gradle.properties  # Edit project.version
   git add gradle.properties
   git commit -m "Update version to X.Y.Z"
   git push
   ```

2. **Go to:** `Actions > Release Build`
3. **Click:** "Run workflow"
4. **Select:** Branch (usually `main`)
5. **Click:** "Run workflow"

**Note:** Version is automatically read from `gradle.properties`, no manual input needed.

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
   - Windows: `hollywood-animal-master.bat`
   - macOS/Linux: `./hollywood-animal-master`

### Android

**Output:** APK file

**Types:**
- **Debug APK** - For testing (from CI builds)
- **Release APK** - For distribution (from releases)
  - Unsigned (if no keystore)
  - Signed (if keystore configured)

**Installation:**
```bash
adb install hollywood-animal-master-*.apk
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

- ✅ **All CI checks pass (required by GitHub)**
- ✅ **Unit tests pass (enforced for PRs)**
- ✅ Code reviewed (if team workflow)
- ✅ Tests added for new features
- ✅ Documentation updated

### Pull Request Workflow

1. **Create feature branch:**
   ```bash
   git checkout -b feature/my-feature
   ```

2. **Make changes and commit:**
   ```bash
   git add .
   git commit -m "feat: add new feature"
   git push origin feature/my-feature
   ```

3. **Create Pull Request on GitHub**
   - CI workflow runs automatically
   - **Unit Tests must pass** (required check)
   - Reviews can be added (optional, based on settings)

4. **Merge when green:**
   - All required checks pass ✅
   - Merge pull request

**Note:** See [PR_REQUIREMENTS.md](./PR_REQUIREMENTS.md) for configuring branch protection rules.

### Release Checklist

- [ ] Version number updated in `gradle.properties`
- [ ] CHANGELOG updated
- [ ] All tests passing locally: `./gradlew test`
- [ ] Desktop app tested locally
- [ ] Android app tested on device/emulator
- [ ] Web app tested in browser
- [ ] **Create and push version tag** (triggers release):
  ```bash
  git tag v1.0.0
  git push origin v1.0.0
  ```
- [ ] Monitor release workflow in GitHub Actions
- [ ] Verify all artifacts are uploaded to release

### Versioning Scheme

Follow [Semantic Versioning](https://semver.org/):
- **MAJOR.MINOR.PATCH** (e.g., `1.2.3`)
  - **MAJOR** - Breaking changes
  - **MINOR** - New features (backwards compatible)
  - **PATCH** - Bug fixes

---

## Support

For issues with CI/CD:
1. Check [Actions](../../actions) logs in your repository
2. Review this guide
3. Check [GitHub Actions documentation](https://docs.github.com/en/actions)
4. Open an [issue](../../issues) in your repository

---

**Built with ❤️ for Hollywood Animal players** 🎬
