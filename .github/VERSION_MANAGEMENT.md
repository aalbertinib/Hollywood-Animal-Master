# 📋 Version Management Guide

This document explains how versioning works across all platforms in Hollywood Animals Master.

## 🎯 Single Source of Truth

**All platform versions are managed from one place: `gradle.properties`**

```properties
project.version=1.0.0
```

This single property controls:
- ✅ Android `versionName` and `versionCode`
- ✅ Desktop package version
- ✅ iOS `MARKETING_VERSION` and `CURRENT_PROJECT_VERSION`
- ✅ CI/CD build artifacts
- ✅ Release tags and notes

---

## 🔢 Version Format

Follow [Semantic Versioning](https://semver.org/): **MAJOR.MINOR.PATCH**

```
1.0.0
│ │ │
│ │ └── PATCH: Bug fixes
│ └──── MINOR: New features (backwards compatible)
└────── MAJOR: Breaking changes
```

### Version Code Calculation

Android `versionCode` is automatically calculated from the semantic version:

```
versionCode = (MAJOR * 10000) + (MINOR * 100) + PATCH

Examples:
1.0.0  → 10000
1.2.3  → 10203
2.0.0  → 20000
3.14.5 → 31405
```

This ensures:
- Each release has a unique, incrementing code
- Supports up to version 999.99.99 (max 9,999,999)
- Google Play Store accepts version updates correctly

---

## 🔄 Updating the Version

### 1. Update gradle.properties

Edit the version in `gradle.properties`:

```properties
project.version=1.1.0
```

### 2. Sync to iOS (Optional)

If developing for iOS, sync the version:

```bash
./gradlew :composeApp:syncVersionToIOS
```

This updates `iosApp/Configuration/Config.xcconfig` automatically.

### 3. Verify the Version

Check that all platforms use the correct version:

```bash
./gradlew :composeApp:printVersion
```

Output:
```
============================================================
Project Version Information
============================================================
Version: 1.1.0
Android Version Code: 10100
Android Version Name: 1.1.0
Desktop Package Version: 1.1.0
iOS Marketing Version: 1.1.0
============================================================
```

### 4. Commit Changes

```bash
git add gradle.properties iosApp/Configuration/Config.xcconfig
git commit -m "Bump version to 1.1.0"
git push origin main
```

---

## 🛠️ Gradle Tasks

### Available Tasks

| Task | Description | Command |
|------|-------------|---------|
| `printVersion` | Display current version info | `./gradlew :composeApp:printVersion` |
| `exportVersion` | Export version to file for CI | `./gradlew :composeApp:exportVersion` |
| `syncVersionToIOS` | Sync version to iOS config | `./gradlew :composeApp:syncVersionToIOS` |

### Usage Examples

```bash
# Print current version
./gradlew :composeApp:printVersion

# Sync version to iOS before building
./gradlew :composeApp:syncVersionToIOS

# Export for scripts
./gradlew :composeApp:exportVersion
cat build/version.txt
```

---

## 🤖 CI/CD Integration

### Automatic Version Extraction

All CI workflows automatically extract the version from `gradle.properties`:

```yaml
- name: Extract version from gradle.properties
  id: version
  run: |
    VERSION=$(grep "^project.version=" gradle.properties | cut -d'=' -f2)
    echo "app_version=$VERSION" >> $GITHUB_OUTPUT
    echo "📦 Building version: $VERSION"
```

### Release Artifacts

When building releases, artifacts are automatically named with the version:

```
hollywood-animal-master-1.0.0-windows-x64.zip
hollywood-animal-master-1.0.0-macos-x64.zip
hollywood-animal-master-1.0.0-linux-x64.zip
hollywood-animal-master-1.0.0-release.apk
hollywood-animal-master-1.0.0-web.zip
```

---

## 📜 Version Scripts

### Bash Script (Linux/macOS/CI)

```bash
# Get version
./scripts/get-version.sh

# Use in script
VERSION=$(./scripts/get-version.sh)
echo "Current version: $VERSION"
```

### PowerShell Script (Windows)

```powershell
# Get version
.\scripts\get-version.ps1

# Use in script
$version = .\scripts\get-version.ps1
Write-Host "Current version: $version"
```

---

## 🎯 Platform-Specific Details

### Android

**Automatic Configuration:**
- `versionName`: Read directly from `project.version`
- `versionCode`: Calculated from semantic version

**Location:** `composeApp/build.gradle.kts`

```kotlin
val appVersion = project.findProperty("project.version") as String? ?: "1.0.0"
val appVersionCode = /* calculated from version */

android {
    defaultConfig {
        versionCode = appVersionCode
        versionName = appVersion
    }
}
```

### Desktop (JVM)

**Automatic Configuration:**
- `packageVersion`: Read directly from `project.version`

**Location:** `composeApp/build.gradle.kts`

```kotlin
compose.desktop {
    application {
        nativeDistributions {
            packageVersion = appVersion
        }
    }
}
```

### iOS

**Semi-Automatic Configuration:**
- `MARKETING_VERSION`: Synced via `syncVersionToIOS` task
- `CURRENT_PROJECT_VERSION`: Synced as build number

**Location:** `iosApp/Configuration/Config.xcconfig`

**Manual Sync:**
```bash
./gradlew :composeApp:syncVersionToIOS
```

**Auto-Sync:** Run before each iOS build

### Web (WASM)

**Automatic Configuration:**
- Version embedded in artifacts
- No platform-specific version configuration needed

---

## 🚀 Release Workflow

### Standard Release Process

1. **Update Version**
   ```bash
   # Edit gradle.properties
   nano gradle.properties  # Change project.version=1.1.0
   ```

2. **Sync iOS** (if needed)
   ```bash
   ./gradlew :composeApp:syncVersionToIOS
   ```

3. **Verify**
   ```bash
   ./gradlew :composeApp:printVersion
   ```

4. **Commit & Tag**
   ```bash
   git add gradle.properties iosApp/Configuration/Config.xcconfig
   git commit -m "Release version 1.1.0"
   git tag -a v1.1.0 -m "Release version 1.1.0"
   git push origin main --tags
   ```

5. **Create GitHub Release**
   - Go to GitHub Releases
   - Click "Create new release"
   - Select tag `v1.1.0`
   - Add release notes
   - Publish release

6. **Automatic Builds**
   - CI automatically extracts version from `gradle.properties`
   - All artifacts named with correct version
   - Release notes updated with version info

---

## ⚠️ Important Notes

### DO:
- ✅ Always update `gradle.properties` first
- ✅ Use semantic versioning format
- ✅ Run `syncVersionToIOS` before iOS builds
- ✅ Verify with `printVersion` before releasing
- ✅ Commit version changes before tagging

### DON'T:
- ❌ Don't hardcode versions in build files
- ❌ Don't manually edit iOS Config.xcconfig (use sync task)
- ❌ Don't skip version bumps between releases
- ❌ Don't use non-semantic version formats

---

## 🔍 Troubleshooting

### Version Not Updating in Android

**Solution:**
```bash
./gradlew clean
./gradlew :composeApp:assembleDebug
```

### iOS Version Out of Sync

**Solution:**
```bash
./gradlew :composeApp:syncVersionToIOS
```

### CI Using Wrong Version

**Check:**
1. Ensure `gradle.properties` is committed
2. Verify format: `project.version=X.Y.Z`
3. Check CI logs for "Building version" message

---

## 📚 Additional Resources

- [Semantic Versioning Spec](https://semver.org/)
- [Android Versioning Guide](https://developer.android.com/studio/publish/versioning)
- [Release Guide](./RELEASE_GUIDE.md)

---

**Last Updated:** 2025-10-10  
**Maintainer:** @aalbertinib
