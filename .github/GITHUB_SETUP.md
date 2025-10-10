# 🔐 Safe GitHub Setup Guide

**Complete guide to using GitHub Actions with Hollywood Animals Master**

> ⚠️ **Security First:** This guide prioritizes keeping your secrets safe!

---

## 📋 Table of Contents

1. [Initial GitHub Setup](#initial-github-setup)
2. [Security Best Practices](#security-best-practices)
3. [Pushing Your Code](#pushing-your-code)
4. [Setting Up Secrets (Safely)](#setting-up-secrets-safely)
5. [Creating Releases](#creating-releases)
6. [Monitoring Builds](#monitoring-builds)
7. [Troubleshooting](#troubleshooting)

---

## Initial GitHub Setup

### Step 1: Create a New Repository

1. **Go to GitHub:** https://github.com/new
2. **Fill in details:**
   - **Repository name:** `Hollywood-Animals-Master` (or your preferred name)
   - **Description:** `Movie Distribution Calculator for Hollywood Animal game`
   - **Visibility:** 
     - ✅ **Public** (recommended for open source)
     - 🔒 **Private** (if you want to keep it private)
   - **Initialize:** ❌ **DO NOT** initialize with README/gitignore (we already have these)
3. **Click:** "Create repository"

### Step 2: Note Your Repository Info

GitHub will show you commands like:
```bash
git remote add origin https://github.com/aalbertinib/Hollywood-Animals-Master.git
```

**IMPORTANT:** Replace `aalbertinib` with your actual GitHub username!

---

## Security Best Practices

### ✅ Safe Files (OK to commit)

These files are **safe** and should be in your repository:
- ✅ Source code (`.kt`, `.swift` files)
- ✅ Build scripts (`build.gradle.kts`)
- ✅ GitHub Actions workflows (`.github/workflows/*.yml`)
- ✅ Documentation (`README.md`, `*.md` files)
- ✅ Resources (images, icons, strings)
- ✅ `.gitignore` file

### ⛔ NEVER Commit These

**THESE WILL COMPROMISE YOUR SECURITY:**
- ⛔ **Keystore files** (`.jks`, `.keystore`) - Used for signing apps
- ⛔ **API keys** - Any authentication credentials
- ⛔ **Passwords** - In any form
- ⛔ **`secrets.properties`** - Contains sensitive data
- ⛔ **`local.properties`** - Contains local paths (already in `.gitignore`)
- ⛔ **`.env` files** - Environment variables with secrets
- ⛔ **Personal tokens** - GitHub tokens, access tokens

### 🔍 How to Check Before Committing

```bash
# Check what will be committed
git status

# See actual changes
git diff

# If you see any passwords/keys - STOP! Don't commit!
```

### 🚨 If You Accidentally Committed a Secret

**ACT IMMEDIATELY:**

1. **Rotate the secret** (change password, regenerate key)
2. **Remove from git history:**
   ```bash
   # Install BFG Repo-Cleaner
   # Download from: https://rtyley.github.io/bfg-repo-cleaner/
   
   # Remove the file from history
   java -jar bfg.jar --delete-files secrets.properties
   
   # Clean up
   git reflog expire --expire=now --all
   git gc --prune=now --aggressive
   
   # Force push (WARNING: Rewrites history!)
   git push --force
   ```
3. **Consider the secret compromised** - change it everywhere!

---

## Pushing Your Code

### First Time Setup

```bash
# Navigate to your project directory
cd C:\Users\Adrien\Documents\Git\Perso\Hollywood-Animals-Master

# Initialize git (if not already done)
git init

# Add all files (respects .gitignore)
git add .

# Check what will be committed (IMPORTANT!)
git status

# Verify no secrets are staged
git diff --cached

# Create first commit
git commit -m "Initial commit: Hollywood Animals Master calculator"

# Add GitHub as remote (REPLACE aalbertinib!)
git remote add origin https://github.com/aalbertinib/Hollywood-Animals-Master.git

# Push to GitHub
git push -u origin main
```

### Regular Updates

```bash
# Stage your changes
git add .

# Check what changed
git status

# Commit with meaningful message
git commit -m "Add feature: theme animation fixes"

# Push to GitHub
git push
```

---

## Setting Up Secrets (Safely)

### What Are GitHub Secrets?

GitHub Secrets are **encrypted environment variables** that:
- ✅ Are stored securely on GitHub's servers
- ✅ Are never shown in logs
- ✅ Can be used in workflows without exposing values
- ✅ Are encrypted at rest

### Required Secrets for Android Signing (Optional)

**You only need these if you want signed Android releases.**

#### Option 1: No Signing (Easiest - Recommended for Testing)

**Skip this section entirely!** Your release workflow will create unsigned APKs that work perfectly for personal use and testing.

#### Option 2: With Signing (For Production/Distribution)

**📚 Complete Guide Available:**  
For detailed step-by-step instructions with troubleshooting, see:  
**[Android Signing Guide](./ANDROID_SIGNING_GUIDE.md)**

##### Quick Summary:

1. **Generate keystore:**
   ```bash
   keytool -genkey -v -keystore hollywood-animals-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias hollywood-animals-key
   ```

2. **Backup keystore** (CRITICAL!)
   - USB drive + password manager + encrypted cloud

3. **Convert to Base64:**
   ```bash
   # Windows
   certutil -encode hollywood-animals-release.jks keystore-base64.txt
   
   # Mac/Linux
   base64 hollywood-animals-release.jks > keystore-base64.txt
   ```

##### Add Secrets to GitHub

1. **Go to your GitHub repository**
2. **Click:** `Settings` (top menu)
3. **Click:** `Secrets and variables` → `Actions` (left sidebar)
4. **Click:** `New repository secret` button

**Add these 4 secrets:**

| Secret Name | Value | Where to Find |
|-------------|-------|---------------|
| `KEYSTORE_BASE64` | Content of `keystore.base64.txt` | Open file, copy ALL text |
| `KEYSTORE_PASSWORD` | Your keystore password | What you entered during `keytool` |
| `KEY_ALIAS` | `hollywood-animals-key` | The `-alias` value from `keytool` |
| `KEY_PASSWORD` | Your key password | What you entered during `keytool` |

**For each secret:**
1. Click "New repository secret"
2. Name: `KEYSTORE_BASE64` (exactly as shown)
3. Value: Paste the value
4. Click "Add secret"

##### Step 4: Verify Secrets

After adding:
- ✅ You should see 4 secrets listed
- ✅ Values are hidden (show as `***`)
- ✅ You **cannot** view them again (this is normal!)

##### Step 5: Clean Up Local Files

```bash
# DELETE these files from your computer (they contain secrets!)
rm release-keystore.jks      # Or delete manually
rm keystore.base64.txt       # Or delete manually

# These files should NEVER exist in your project folder!
```

---

## Creating Releases

### Automated Release Process

#### Step 1: Prepare Your Code

```bash
# Make sure everything is committed
git status

# Should show: "nothing to commit, working tree clean"
```

#### Step 2: Create a Git Tag

```bash
# Create and annotate a tag
git tag -a v1.0.0 -m "Release version 1.0.0"

# Push the tag to GitHub
git push origin v1.0.0
```

#### Step 3: Create GitHub Release

1. **Go to:** `https://github.com/aalbertinib/Hollywood-Animals-Master/releases/new`
2. **Choose tag:** Select `v1.0.0` from dropdown
3. **Release title:** `Hollywood Animals Master v1.0.0`
4. **Description:** Add release notes:

```markdown
## 🎬 Hollywood Animals Master v1.0.0

Initial release of the Movie Distribution Calculator for Hollywood Animal!

### ✨ Features
- Calculate seat distribution across weeks
- Save and load multiple movies
- 5 beautiful Art Deco themes
- Dark/Light mode with smooth animations
- Cross-platform: Desktop, Android, iOS, Web

### 📥 Downloads
See assets below for platform-specific downloads.

### 🐛 Known Issues
None yet!

---

For Hollywood Animal players - enjoy optimizing your distributions! 🎭
```

5. **Click:** "Publish release"

#### Step 4: Automatic Build Process

**GitHub Actions will automatically:**
1. ⏳ Start building (check the "Actions" tab)
2. 🖥️ Build Windows, macOS, Linux executables
3. 🤖 Build Android APK (signed if keystore configured)
4. 🌐 Build Web WASM bundle
5. 📤 Upload all builds to the release
6. 🌍 Deploy web app to GitHub Pages
7. ✅ Complete in 10-20 minutes

#### Step 5: Download Your Builds

1. **Go to:** `https://github.com/aalbertinib/Hollywood-Animals-Master/releases`
2. **Find:** Your release (v1.0.0)
3. **Download:** Under "Assets" section

---

## Monitoring Builds

### View Build Status

1. **Go to:** `Actions` tab in your repository
2. **See:** All running and completed workflows
3. **Click:** Any workflow to see details
4. **Expand:** Job steps to see logs

### Build Failed - What to Do?

1. **Click** the failed workflow
2. **Read** the error message in red
3. **Common issues:**
   - **Build errors:** Check your Kotlin code compiles locally first
   - **Signing errors:** Verify all 4 secrets are set correctly
   - **Timeout:** Retry the workflow (sometimes GitHub is slow)

### Retry a Failed Build

1. **Go to** the failed workflow run
2. **Click** "Re-run jobs" (top right)
3. **Select** "Re-run failed jobs"

---

## Enable GitHub Pages

### One-Time Setup

1. **Go to:** `Settings` → `Pages` (left sidebar)
2. **Source:** Select "GitHub Actions"
3. **Save**

### Access Your Web App

After first deployment:
- **URL:** `https://aalbertinib.github.io/Hollywood-Animals-Master/`
- **Wait:** 2-5 minutes for first deployment
- **Check:** "Actions" tab for deployment status

### Custom Domain (Optional)

If you own a domain:
1. **Add** CNAME record pointing to `aalbertinib.github.io`
2. **Enter** domain in Pages settings
3. **Enable** "Enforce HTTPS"

---

## Security Checklist

### Before Every Commit

- [ ] Run `git status` to see what will be committed
- [ ] Run `git diff` to see actual changes
- [ ] Verify no `.jks`, `.keystore`, or secret files
- [ ] Check no passwords or API keys in code
- [ ] Ensure `.gitignore` is working

### Before Every Release

- [ ] Test builds locally
- [ ] Update version numbers
- [ ] Update CHANGELOG/release notes
- [ ] Tag version follows semantic versioning
- [ ] All tests pass

### Periodic Security Checks

- [ ] Review who has access to repository (Settings → Collaborators)
- [ ] Check GitHub Actions logs for anomalies
- [ ] Rotate secrets annually (generate new keystore for major versions)
- [ ] Keep dependencies updated

---

## What Gets Built Where

### On Every Push/PR to `main` or `develop`:
- ✅ JVM build compiled
- ✅ Android debug APK created
- ✅ Web WASM bundle created
- ✅ Tests run
- ✅ Artifacts available for 7 days

### On GitHub Release:
- ✅ Windows executable (.zip)
- ✅ macOS executable (.zip)
- ✅ Linux executable (.zip)
- ✅ Android APK (signed if configured)
- ✅ Web WASM bundle (.zip)
- ✅ All uploaded to release
- ✅ Web app deployed to GitHub Pages
- ✅ Artifacts retained for 30 days

### On Push to `main` (App Changes):
- ✅ Web app rebuilt and deployed to GitHub Pages

---

## Quick Reference

### Essential Commands

```bash
# Check repository status
git status

# See what changed
git diff

# Stage all changes
git add .

# Commit with message
git commit -m "Your descriptive message"

# Push to GitHub
git push

# Create release tag
git tag -a v1.0.0 -m "Release 1.0.0"
git push origin v1.0.0

# View remote URL
git remote -v
```

### Important URLs (Replace aalbertinib)

- **Repository:** `https://github.com/aalbertinib/Hollywood-Animals-Master`
- **Actions:** `https://github.com/aalbertinib/Hollywood-Animals-Master/actions`
- **Releases:** `https://github.com/aalbertinib/Hollywood-Animals-Master/releases`
- **Settings:** `https://github.com/aalbertinib/Hollywood-Animals-Master/settings`
- **Web App:** `https://aalbertinib.github.io/Hollywood-Animals-Master/`

---

## Getting Help

### GitHub Actions Issues

1. Check the [Release Guide](./.github/RELEASE_GUIDE.md)
2. Review workflow logs in Actions tab
3. Search GitHub Actions documentation
4. Open an issue in repository

### Security Concerns

If you think you've exposed a secret:
1. **Immediately** rotate/change the secret
2. Update GitHub secrets with new values
3. Check commit history for exposure
4. Consider the old secret compromised

### General Questions

- **GitHub Docs:** https://docs.github.com
- **GitHub Actions:** https://docs.github.com/en/actions
- **Android Signing:** https://developer.android.com/studio/publish/app-signing

---

## Summary

### ✅ You're Ready When:

- [ ] Repository created on GitHub
- [ ] Code pushed to `main` branch
- [ ] `.gitignore` is working (no secrets committed)
- [ ] GitHub Actions enabled (automatic)
- [ ] Secrets configured (if using Android signing)
- [ ] GitHub Pages enabled (for web app)
- [ ] First release created

### 🎬 Next Steps:

1. **Test:** Create a test release (v0.1.0)
2. **Verify:** All builds complete successfully
3. **Download:** Test each platform's build
4. **Share:** Your web app URL with others!

---

**🎬 Remember:** Security first, then automation! Never commit secrets to git.

**Questions?** Review this guide or check the [Release Guide](./RELEASE_GUIDE.md) for more details.
