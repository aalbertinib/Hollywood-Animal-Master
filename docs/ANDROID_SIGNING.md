# 🔐 Android Signing Configuration Guide

This guide explains how to set up Android app signing for both local development and CI/CD (GitHub Actions).

## 📋 Table of Contents

1. [Overview](#overview)
2. [Local Development Setup](#local-development-setup)
3. [CI/CD Setup (GitHub Actions)](#cicd-setup-github-actions)
4. [Security Best Practices](#security-best-practices)
5. [Troubleshooting](#troubleshooting)

---

## Overview

The Android app uses a **secure signing configuration** that:
- ✅ Supports both local development and CI/CD builds
- ✅ Never commits sensitive data to Git
- ✅ Uses environment variables for CI/CD
- ✅ Falls back gracefully when no keystore is configured

### How It Works

The `build.gradle.kts` automatically:
1. **Checks for `keystore.properties`** (local development)
2. **Checks for environment variables** (CI/CD)
3. **Configures signing** if credentials are found
4. **Builds unsigned APK** if no credentials (debug builds)

---

## Local Development Setup

### Step 1: Create a Keystore

If you don't have a keystore yet, create one:

```bash
keytool -genkey -v \
  -keystore hollywood-animal-master.jks \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -alias ham-release
```

**Important prompts:**
- **Keystore password**: Choose a strong password (save in password manager!)
- **Key password**: Can be same as keystore password or different
- **Your name**: Your name or organization
- **Organizational unit**: Optional
- **Organization**: Optional
- **City/Locality**: Optional
- **State/Province**: Optional
- **Country code**: Two-letter country code (e.g., US, FR, CA)

### Step 2: Store the Keystore Securely

⚠️ **CRITICAL**: Your keystore is the KEY to signing your app!

**Backup locations (choose multiple):**
1. 📀 **USB drive** (encrypted)
2. 🔒 **Password manager** (some support file attachments)
3. ☁️ **Encrypted cloud storage** (e.g., encrypted folder in OneDrive/Google Drive)
4. 💾 **External hard drive** (encrypted)

**Never:**
- ❌ Commit to Git
- ❌ Share via email
- ❌ Store in unencrypted cloud storage
- ❌ Leave only one copy

### Step 3: Create keystore.properties

1. Copy the template:
   ```bash
   cp keystore.properties.template keystore.properties
   ```

2. Edit `keystore.properties` with your values:
   ```properties
   storeFile=./keystore/hollywood-animal-master.jks
   storePassword=your_keystore_password
   keyAlias=ham-release
   keyPassword=your_key_password
   ```

3. Move your keystore to a secure location:
   ```bash
   mkdir keystore
   mv hollywood-animal-master.jks keystore/
   ```

### Step 4: Test Local Signing

Build a signed release APK:

```bash
./gradlew :composeApp:assembleRelease
```

The signed APK will be in:
```
composeApp/build/outputs/apk/release/composeApp-release.apk
```

---

## CI/CD Setup (GitHub Actions)

### Step 1: Encode Your Keystore

Encode your keystore file to Base64:

**On macOS/Linux:**
```bash
base64 -i hollywood-animal-master.jks | pbcopy  # macOS (copies to clipboard)
base64 hollywood-animal-master.jks              # Linux (prints to terminal)
```

**On Windows (PowerShell):**
```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("hollywood-animal-master.jks")) | Set-Clipboard
```

### Step 2: Add GitHub Secrets

1. Go to your repository on GitHub
2. Navigate to: **Settings** → **Secrets and variables** → **Actions**
3. Click **"New repository secret"**
4. Add these four secrets:

| Secret Name | Description | Example |
|-------------|-------------|---------|
| `KEYSTORE_BASE64` | Base64-encoded keystore file | `MIIEowIBA...` (very long) |
| `KEYSTORE_PASSWORD` | Keystore password | `mySecurePassword123` |
| `KEY_ALIAS` | Key alias | `ham-release` |
| `KEY_PASSWORD` | Key password | `mySecurePassword123` |

### Step 3: Verify GitHub Actions

1. Push a commit or create a release
2. Go to **Actions** tab
3. Watch the **"Release Build"** workflow
4. Check the Android build logs:
   - ✅ Should see: "🔑 Keystore found, decoding..."
   - ✅ Should see: "✅ Keystore configured for signing"
   - ✅ APK should be named: `hollywood-animal-master-X.X.X-release-signed.apk`

---

## Security Best Practices

### ✅ DO

- ✅ Use **strong passwords** for keystore and key (16+ characters, mixed case, numbers, symbols)
- ✅ Store keystore in **multiple secure locations** (encrypted backups)
- ✅ Keep keystore password in **password manager**
- ✅ Restrict access to GitHub repository secrets
- ✅ Use different keystores for debug and release builds
- ✅ Rotate keystore if compromised (publish new app version)
- ✅ Document where keystore backups are stored (for your team)

### ❌ DON'T

- ❌ **NEVER** commit keystore files to Git
- ❌ **NEVER** commit `keystore.properties` to Git
- ❌ **NEVER** share keystore via email or chat
- ❌ **NEVER** hardcode passwords in code
- ❌ **NEVER** reuse keystore passwords for other services
- ❌ **NEVER** store keystore in project directory (use separate secure location)

### 🔍 Pre-Commit Checklist

Before every commit, verify:
```bash
git status
```

Ensure you **DON'T see:**
- `keystore.properties`
- `*.jks`
- `*.keystore`

If you see these files, **STOP** and remove them:
```bash
git reset HEAD keystore.properties
git checkout -- keystore.properties
```

---

## Troubleshooting

### Problem: "Keystore file not found"

**Cause**: The path in `keystore.properties` is incorrect.

**Solution**:
```properties
# Use relative path from project root
storeFile=./keystore/hollywood-animal-master.jks

# OR use absolute path
storeFile=/Users/yourname/secure/hollywood-animal-master.jks
```

### Problem: "Cannot recover key"

**Cause**: Wrong key password.

**Solution**:
1. Verify the password in your password manager
2. Update `keystore.properties` with correct password
3. If password is lost, you'll need to create a new keystore (this means publishing as a new app)

### Problem: APK is unsigned in CI/CD

**Cause**: GitHub secrets not configured.

**Solution**:
1. Verify all 4 secrets are set in GitHub repository settings
2. Check the Actions log for error messages
3. Re-encode the keystore if needed

### Problem: "keystore.properties not found" warning

**Cause**: File doesn't exist (expected for CI/CD builds).

**This is normal for:**
- ✅ Fresh clones of the repository
- ✅ CI/CD builds (uses environment variables)
- ✅ Debug builds (signing not required)

**Action needed only if:**
- ❌ You're trying to build a signed release APK locally

### Problem: Build fails with signing error in CI/CD

**Debugging steps:**
1. Check GitHub Actions logs for exact error
2. Verify Base64 encoding is correct:
   ```bash
   # Decode and verify
   echo "YOUR_BASE64_STRING" | base64 -d > test-keystore.jks
   keytool -list -v -keystore test-keystore.jks
   ```
3. Ensure secret names exactly match:
   - `KEYSTORE_BASE64` (not `KEYSTORE_BASE_64`)
   - `KEYSTORE_PASSWORD` (not `KEYSTORE_PASS`)
   - `KEY_ALIAS` (not `KEYALIAS`)
   - `KEY_PASSWORD` (not `KEY_PASS`)

---

## Additional Resources

- [Android Developers - Sign your app](https://developer.android.com/studio/publish/app-signing)
- [Kotlin Multiplatform Documentation](https://kotlinlang.org/docs/multiplatform.html)
- [GitHub Actions Encrypted Secrets](https://docs.github.com/en/actions/security-guides/encrypted-secrets)

---

## Quick Reference

### Local Build Commands

```bash
# Build debug (no signing needed)
./gradlew :composeApp:assembleDebug

# Build signed release
./gradlew :composeApp:assembleRelease

# Build and install on device
./gradlew :composeApp:installRelease
```

### Files Created

```
project-root/
├── keystore.properties              # Local signing config (NOT in Git)
├── keystore.properties.template     # Template (safe in Git)
└── keystore/                        # Your keystore location
    └── hollywood-animal-master.jks # Keystore file (NOT in Git)
```

### Environment Variables (CI/CD)

```bash
KEYSTORE_FILE=/path/to/keystore.jks
KEYSTORE_PASSWORD=your_password
KEY_ALIAS=ham-release
KEY_PASSWORD=your_key_password
```

---

**Remember**: Your keystore is irreplaceable! Losing it means you cannot update your published app. 🔐
