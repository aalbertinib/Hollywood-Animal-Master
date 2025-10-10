# 🔐 Android Keystore & Signing Guide

**Complete guide to generating an Android keystore and setting up automatic APK signing with GitHub Actions**

---

## 📋 Table of Contents

1. [What is a Keystore?](#what-is-a-keystore)
2. [Prerequisites](#prerequisites)
3. [Step 1: Generate Your Keystore](#step-1-generate-your-keystore)
4. [Step 2: Backup Your Keystore](#step-2-backup-your-keystore)
5. [Step 3: Convert to Base64](#step-3-convert-to-base64)
6. [Step 4: Add Secrets to GitHub](#step-4-add-secrets-to-github)
7. [Step 5: Verify Workflow Integration](#step-5-verify-workflow-integration)
8. [Testing Your Setup](#testing-your-setup)
9. [Troubleshooting](#troubleshooting)
10. [Security Best Practices](#security-best-practices)

---

## What is a Keystore?

A **keystore** is a binary file that contains your app's private signing key. It's used to:
- ✅ **Sign your Android app** - Proves you're the legitimate developer
- ✅ **Publish to Play Store** - Google requires signed APKs
- ✅ **Update your app** - Only you can publish updates (with the same keystore)

**⚠️ CRITICAL:** If you lose your keystore, you **cannot** update your app on Google Play. You'd have to create a new app listing!

---

## Prerequisites

### Required Software

You need **Java Development Kit (JDK)** installed. Check if you have it:

```bash
# Windows (PowerShell)
java -version

# If not installed, download from:
# https://adoptium.net/ (recommended)
# or
# https://www.oracle.com/java/technologies/downloads/
```

You should see output like:
```
java version "17.0.x" 2023-xx-xx LTS
```

---

## Step 1: Generate Your Keystore

### Open Terminal/Command Prompt

**Windows:**
- Press `Win + R`
- Type `cmd` or `powershell`
- Press Enter

**Mac/Linux:**
- Open Terminal app

### Navigate to a Safe Location

```bash
# Create a secure directory for keystore generation
# Windows
cd C:\Users\YourUsername\Documents
mkdir AndroidKeystores
cd AndroidKeystores

# Mac/Linux
cd ~/Documents
mkdir AndroidKeystores
cd AndroidKeystores
```

### Generate the Keystore

Run this command (copy the entire command):

```bash
keytool -genkey -v -keystore hollywood-animals-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias hollywood-animals-key
```

**Breaking down the command:**
- `hollywood-animals-release.jks` - Your keystore filename
- `-keyalg RSA` - Encryption algorithm (RSA)
- `-keysize 2048` - Key size (2048 bits is standard)
- `-validity 10000` - Valid for ~27 years
- `hollywood-animals-key` - Alias (name of the key inside the keystore)

### Fill in the Information

You'll be prompted for information. **Save all these values!**

#### 1. Enter keystore password

```
Enter keystore password:
Re-enter new password:
```

**Requirements:**
- ✅ At least 6 characters
- ✅ Use a strong password (mix letters, numbers, symbols)
- ✅ **WRITE THIS DOWN** - You'll need it for GitHub

**Example (DON'T USE THIS!):** `MySecure2024!Pass`

#### 2. Enter key password

```
Enter key password for <hollywood-animals-key>
    (RETURN if same as keystore password):
```

**Recommendation:** Press **ENTER** to use the same password as keystore.  
If you want different passwords, enter a new one here.

#### 3. Enter your information

```
What is your first and last name?
  [Unknown]:  John Doe

What is the name of your organizational unit?
  [Unknown]:  Development

What is the name of your organization?
  [Unknown]:  Your Company Name

What is the name of your City or Locality?
  [Unknown]:  Your City

What is the name of your State or Province?
  [Unknown]:  Your State

What is the two-letter country code for this unit?
  [Unknown]:  US
```

**Note:** This information will be embedded in the certificate. It's not critical for personal apps.

#### 4. Confirm

```
Is CN=John Doe, OU=Development, O=Your Company Name, L=Your City, ST=Your State, C=US correct?
  [no]:  yes
```

Type `yes` and press Enter.

### Success!

You should see:
```
Generating 2,048 bit RSA key pair and self-signed certificate (SHA256withRSA) with a validity of 10,000 days
        for: CN=John Doe, OU=Development, O=Your Company Name, L=Your City, ST=Your State, C=US
[Storing hollywood-animals-release.jks]
```

**Your keystore file `hollywood-animals-release.jks` is created!**

---

## Step 2: Backup Your Keystore

**⚠️ THIS IS CRITICAL - DO THIS NOW!**

### Create an Information File

Create a text file with all your keystore details:

```text
ANDROID KEYSTORE INFORMATION
============================
App: Hollywood Animals Master
Keystore File: hollywood-animals-release.jks
Keystore Password: [YOUR_PASSWORD_HERE]
Key Alias: hollywood-animals-key
Key Password: [SAME_AS_KEYSTORE or YOUR_KEY_PASSWORD]
Created: [TODAY'S_DATE]

IMPORTANT: Keep this file and the .jks file in a SAFE place!
If lost, you cannot update your app on Google Play Store!
```

**Save as:** `keystore-info.txt`

### Backup Methods (Do ALL of these!)

1. **USB Drive/External Hard Drive**
   ```
   Copy both files:
   - hollywood-animals-release.jks
   - keystore-info.txt
   
   Store USB drive in a safe place (fireproof safe, safety deposit box)
   ```

2. **Password Manager** (Recommended: 1Password, Bitwarden, LastPass)
   - Create a secure note titled "Hollywood Animals Android Keystore"
   - Paste the information from `keystore-info.txt`
   - Attach the `hollywood-animals-release.jks` file if supported
   - **Most secure option!**

3. **Cloud Storage** (Encrypted!)
   - Upload to encrypted cloud storage (Dropbox, Google Drive, OneDrive)
   - **IMPORTANT:** Encrypt the file first or use a password-protected ZIP
   - Example:
     ```bash
     # Windows (7-Zip)
     7z a -p hollywood-animals-keystore.zip hollywood-animals-release.jks keystore-info.txt
     
     # Mac (zip with password)
     zip -e hollywood-animals-keystore.zip hollywood-animals-release.jks keystore-info.txt
     ```

**⚠️ WARNING:** Never store the keystore password in plain text on your computer or in git!

---

## Step 3: Convert to Base64

GitHub Secrets require the keystore to be in Base64 format (text) rather than binary.

### Windows (PowerShell)

```powershell
# Navigate to where your keystore is
cd C:\Users\YourUsername\Documents\AndroidKeystores

# Convert to Base64
certutil -encode hollywood-animals-release.jks keystore-base64.txt

# Clean up the header/footer
(Get-Content keystore-base64.txt | Where-Object { $_ -notmatch "^-" }) -join "" | Set-Content keystore-base64-clean.txt
```

**Result:** `keystore-base64-clean.txt` contains the Base64 string

### Mac/Linux (Terminal)

```bash
# Navigate to where your keystore is
cd ~/Documents/AndroidKeystores

# Convert to Base64 (single line, no wrapping)
base64 -i hollywood-animals-release.jks -o keystore-base64.txt

# For Linux, if you need unwrapped version:
base64 -w 0 hollywood-animals-release.jks > keystore-base64.txt
```

**Result:** `keystore-base64.txt` contains the Base64 string

### Verify the Base64 File

Open `keystore-base64.txt` (or `keystore-base64-clean.txt` on Windows):
- Should contain a **long string of letters and numbers**
- Example start: `MIIJqwIBAzCCCWwGCSqGSIb3DQEHAaC...`
- **This entire content** goes into GitHub Secret

---

## Step 4: Add Secrets to GitHub

### Navigate to GitHub Repository

1. **Go to:** `https://github.com/aalbertinib/Hollywood-Animals-Master`
2. **Click:** `Settings` (top menu bar)
3. **Click:** `Secrets and variables` → `Actions` (left sidebar)
4. **Click:** Green `New repository secret` button

### Add Secret 1: KEYSTORE_BASE64

1. **Name:** `KEYSTORE_BASE64` (exactly like this, case-sensitive!)
2. **Secret:** 
   - Open `keystore-base64.txt` (or `keystore-base64-clean.txt`)
   - Select **ALL** content (Ctrl+A / Cmd+A)
   - Copy (Ctrl+C / Cmd+C)
   - Paste into the "Secret" field
3. **Click:** "Add secret"

### Add Secret 2: KEYSTORE_PASSWORD

1. **Click:** Green `New repository secret` button
2. **Name:** `KEYSTORE_PASSWORD`
3. **Secret:** Your keystore password (the one you entered during `keytool`)
   - Example: `MySecure2024!Pass`
4. **Click:** "Add secret"

### Add Secret 3: KEY_ALIAS

1. **Click:** Green `New repository secret` button
2. **Name:** `KEY_ALIAS`
3. **Secret:** `hollywood-animals-key` (the alias from the keytool command)
4. **Click:** "Add secret"

### Add Secret 4: KEY_PASSWORD

1. **Click:** Green `New repository secret` button
2. **Name:** `KEY_PASSWORD`
3. **Secret:** Your key password
   - If you pressed ENTER during keytool, use the **same as KEYSTORE_PASSWORD**
   - Otherwise, use the different password you entered
4. **Click:** "Add secret"

### Verify All Secrets Added

You should now see **4 secrets** in the list:
```
KEYSTORE_BASE64     Updated X seconds ago
KEYSTORE_PASSWORD   Updated X seconds ago
KEY_ALIAS           Updated X seconds ago
KEY_PASSWORD        Updated X seconds ago
```

**Note:** You cannot view the values again (this is normal and secure!)

---

## Step 5: Verify Workflow Integration

The GitHub workflow is already configured! Let's verify it's set up correctly.

### Check the Release Workflow

The file `.github/workflows/release.yml` already contains:

```yaml
- name: Sign APK (if keystore available)
  id: sign_apk
  continue-on-error: true
  run: |
    if [ -n "${{ secrets.KEYSTORE_BASE64 }}" ]; then
      echo "${{ secrets.KEYSTORE_BASE64 }}" | base64 -d > keystore.jks
      ./gradlew :composeApp:assembleRelease \
        -Pandroid.injected.signing.store.file=$(pwd)/keystore.jks \
        -Pandroid.injected.signing.store.password="${{ secrets.KEYSTORE_PASSWORD }}" \
        -Pandroid.injected.signing.key.alias="${{ secrets.KEY_ALIAS }}" \
        -Pandroid.injected.signing.key.password="${{ secrets.KEY_PASSWORD }}"
      echo "signed=true" >> $GITHUB_OUTPUT
    else
      echo "signed=false" >> $GITHUB_OUTPUT
    fi
```

**How it works:**
1. Checks if `KEYSTORE_BASE64` secret exists
2. If yes, decodes Base64 back to binary `.jks` file
3. Uses the secrets to sign the APK
4. Sets output flag `signed=true`

### Security Features

✅ **Secrets never appear in logs** - GitHub redacts them  
✅ **Keystore file is temporary** - Created and deleted in same build  
✅ **Encoded transmission** - Base64 prevents binary issues  
✅ **Fallback to unsigned** - Build continues even if signing fails  

---

## Testing Your Setup

### Create a Test Release

1. **Create a test tag:**
   ```bash
   git tag -a v0.1.0-test -m "Test release for signing"
   git push origin v0.1.0-test
   ```

2. **Create GitHub Release:**
   - Go to: `https://github.com/aalbertinib/Hollywood-Animals-Master/releases/new`
   - Choose tag: `v0.1.0-test`
   - Title: `Test Release`
   - Description: `Testing Android APK signing`
   - Check: "This is a pre-release"
   - Click: "Publish release"

3. **Monitor the Workflow:**
   - Go to: `Actions` tab
   - Click on the running workflow
   - Expand: "Build Android APK" job
   - Expand: "Sign APK" step

### Verify Successful Signing

**Success indicators:**
```
✓ Sign APK (if keystore available)
  signed=true
```

**In the "Rename APK" step, you should see:**
```
mv *.apk hollywood-animals-master-0.1.0-test-release-signed.apk
```

**Download the APK from release assets and verify:**
```bash
# Windows
sigcheck -a hollywood-animals-master-*.apk

# Mac/Linux (need apksigner from Android SDK)
apksigner verify --verbose hollywood-animals-master-*.apk
```

---

## Troubleshooting

### Problem: "Sign APK" step fails

**Check:**
1. All 4 secrets are added correctly
2. Secret names are **exact** (case-sensitive)
3. No extra spaces in secret values
4. `KEYSTORE_BASE64` contains the entire Base64 string

**Fix:**
- Re-generate Base64 file
- Re-add the `KEYSTORE_BASE64` secret
- Make sure to copy **all** content

### Problem: "incorrect password" error

**Cause:** `KEYSTORE_PASSWORD` or `KEY_PASSWORD` is wrong

**Fix:**
1. Try to open keystore locally to verify password:
   ```bash
   keytool -list -v -keystore hollywood-animals-release.jks
   ```
2. If that works, re-add the `KEYSTORE_PASSWORD` secret on GitHub
3. Make sure `KEY_PASSWORD` matches (same as keystore if you pressed ENTER)

### Problem: "keystore not found" or "cannot decode"

**Cause:** Base64 encoding issue

**Fix:**
1. Ensure entire Base64 string is copied (no truncation)
2. On Windows, use the "clean" version without header/footer
3. Verify no line breaks in the middle of the string

### Problem: APK is unsigned (but no errors)

**Check workflow logs:**
- If `KEYSTORE_BASE64` is empty/not found, signing is skipped
- Verify secret is added in **repository** settings (not organization)
- Secret name must be exactly `KEYSTORE_BASE64`

---

## Security Best Practices

### ✅ DO

- ✅ Store keystore in **multiple secure locations**
- ✅ Use **strong passwords** (12+ characters, mixed)
- ✅ Keep keystore info in a **password manager**
- ✅ Treat keystore like a **passport** - irreplaceable!
- ✅ Use different keystores for **different apps**
- ✅ Review GitHub Actions logs for **suspicious activity**

### ❌ DON'T

- ❌ **Never** commit keystore to git
- ❌ **Never** share keystore password in email/chat
- ❌ **Never** use weak passwords (e.g., "password123")
- ❌ **Never** store keystore in public cloud without encryption
- ❌ **Never** use the same keystore for debug/release
- ❌ **Never** delete backups after uploading to GitHub

### 🔐 Additional Security

1. **Enable 2FA on GitHub** - Protects your secrets
2. **Limit repository access** - Only trusted collaborators
3. **Audit GitHub Actions** - Review logs periodically
4. **Rotate keystores** - For major version updates (new keystore = new app listing)
5. **Use Play App Signing** - Google manages keystore (recommended for Play Store)

---

## Quick Reference

### Secrets Required

| Secret Name | Example Value | Where to Find |
|-------------|---------------|---------------|
| `KEYSTORE_BASE64` | `MIIJqwIBAzCCC...` | Output of base64 encoding |
| `KEYSTORE_PASSWORD` | `MySecure2024!Pass` | Password you entered in keytool |
| `KEY_ALIAS` | `hollywood-animals-key` | From `-alias` in keytool command |
| `KEY_PASSWORD` | `MySecure2024!Pass` | Same as keystore (if you pressed ENTER) |

### Commands Cheat Sheet

```bash
# Generate keystore
keytool -genkey -v -keystore hollywood-animals-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias hollywood-animals-key

# List keystore contents (verify password)
keytool -list -v -keystore hollywood-animals-release.jks

# Convert to Base64 (Windows)
certutil -encode hollywood-animals-release.jks keystore-base64.txt

# Convert to Base64 (Mac/Linux)
base64 hollywood-animals-release.jks > keystore-base64.txt

# Verify APK signature (Mac/Linux)
apksigner verify --verbose your-app.apk
```

---

## What Happens During GitHub Actions Release

```
1. Workflow triggered (GitHub Release created)
   ↓
2. Check if KEYSTORE_BASE64 exists
   ↓
3. Decode Base64 → keystore.jks
   ↓
4. Build release APK
   ↓
5. Sign APK with keystore + passwords
   ↓
6. Rename: hollywood-animals-master-VERSION-release-signed.apk
   ↓
7. Upload to GitHub Release
   ↓
8. Delete temporary keystore.jks
```

**Total time:** ~10-15 minutes for all platforms

---

## Summary Checklist

Before creating your first release:

- [ ] Keystore generated (`hollywood-animals-release.jks`)
- [ ] Keystore backed up (USB + password manager + cloud)
- [ ] Keystore info documented (`keystore-info.txt`)
- [ ] Converted to Base64 (`keystore-base64.txt`)
- [ ] All 4 secrets added to GitHub
- [ ] Secrets verified (appear in Settings → Secrets)
- [ ] Test release created and signed successfully
- [ ] Signed APK downloaded and verified

**You're ready to ship signed Android releases! 🎉**

---

## Need Help?

- **GitHub Actions Issues:** Check workflow logs in Actions tab
- **Keystore Problems:** Review [Android signing documentation](https://developer.android.com/studio/publish/app-signing)
- **Play Store:** See [Google Play App Signing](https://support.google.com/googleplay/android-developer/answer/9842756)

---

**🎬 Remember:** Your keystore is the **key** to your app's identity. Protect it like a treasure!
