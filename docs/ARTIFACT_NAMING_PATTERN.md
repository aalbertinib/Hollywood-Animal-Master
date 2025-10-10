# Artifact Naming Pattern

## Overview

All release artifacts now follow a consistent naming pattern:

```
Hollywood-Animal-Master-{SEMANTIC_VERSION}-{PLATFORM}-{ARCH}.{extension}
```

## Platform Names

| Platform | Architecture | Naming | Example |
|----------|--------------|--------|---------|
| **Windows Desktop** | x64 | `Windows-x64` | `Hollywood-Animal-Master-1.2.3-Windows-x64.zip` |
| **macOS Desktop (Intel)** | x64 | `macOS-x64` | `Hollywood-Animal-Master-1.2.3-macOS-x64.zip` |
| **macOS Desktop (Apple Silicon)** | ARM64 | `macOS-ARM64` | `Hollywood-Animal-Master-1.2.3-macOS-ARM64.zip` |
| **Linux Desktop** | x64 | `Linux-x64` | `Hollywood-Animal-Master-1.2.3-Linux-x64.zip` |
| **Android** | Universal | `Android-Universal` | `Hollywood-Animal-Master-1.2.3-Android-Universal.apk` |
| **Web (WASM)** | Platform-independent | `Web` | `Hollywood-Animal-Master-1.2.3-Web.zip` |

## Version Format

The version follows [Semantic Versioning](https://semver.org/):
- **Format**: `MAJOR.MINOR.PATCH` (e.g., `1.2.3`)
- **Automatic**: Determined by [Conventional Commits](https://www.conventionalcommits.org/)

### Version Bumping

| Commit Type | Version Bump | Example |
|-------------|--------------|---------|
| `fix:` | Patch | `1.2.3` → `1.2.4` |
| `feat:` | Minor | `1.2.3` → `1.3.0` |
| `feat!:` or `BREAKING CHANGE:` | Major | `1.2.3` → `2.0.0` |

## GitHub Release Assets

When a release is created, all artifacts are automatically uploaded to GitHub Releases with the following labels:

| Artifact | Label |
|----------|-------|
| `Hollywood-Animal-Master-{version}-Windows-x64.zip` | Windows (x64) |
| `Hollywood-Animal-Master-{version}-macOS-x64.zip` | macOS Intel (x64) |
| `Hollywood-Animal-Master-{version}-macOS-ARM64.zip` | macOS Apple Silicon (ARM64) |
| `Hollywood-Animal-Master-{version}-Linux-x64.zip` | Linux (x64) |
| `Hollywood-Animal-Master-{version}-Android-Universal.apk` | Android (Universal APK) |
| `Hollywood-Animal-Master-{version}-Web.zip` | Web (WASM) |

## Example Release

For version **1.5.0**, the following assets would be created:

```
Hollywood-Animal-Master-1.5.0-Windows-x64.zip
Hollywood-Animal-Master-1.5.0-macOS-x64.zip
Hollywood-Animal-Master-1.5.0-macOS-ARM64.zip
Hollywood-Animal-Master-1.5.0-Linux-x64.zip
Hollywood-Animal-Master-1.5.0-Android-Universal.apk
Hollywood-Animal-Master-1.5.0-Web.zip
```

## Configuration Files

### 1. Release Workflow (`release.yml`)

The workflow builds all artifacts with the correct naming:

```yaml
env:
  APP_NAME: Hollywood-Animal-Master

# Desktop naming (with architecture)
PLATFORM="${{ matrix.platform }}"  # Windows, macOS, or Linux
ARCH="${{ matrix.arch }}"  # x64 or ARM64
zip -r "${{ env.APP_NAME }}-${VERSION}-${PLATFORM}-${ARCH}.zip" *

# Android naming (Universal APK)
mv *.apk "${{ env.APP_NAME }}-${VERSION}-Android-Universal.apk"

# Web naming (platform-independent)
zip -r "${{ env.APP_NAME }}-${VERSION}-Web.zip" *
```

### 2. Semantic Release Config (`.releaserc.yml`)

Asset paths match the naming pattern:

```yaml
assets:
  - path: "composeApp/build/compose/binaries/main/Hollywood-Animal-Master-*-Windows-x64.zip"
    label: "Windows (x64)"
  - path: "composeApp/build/compose/binaries/main/Hollywood-Animal-Master-*-macOS-x64.zip"
    label: "macOS Intel (x64)"
  - path: "composeApp/build/compose/binaries/main/Hollywood-Animal-Master-*-macOS-ARM64.zip"
    label: "macOS Apple Silicon (ARM64)"
  - path: "composeApp/build/compose/binaries/main/Hollywood-Animal-Master-*-Linux-x64.zip"
    label: "Linux (x64)"
  - path: "composeApp/build/outputs/apk/release/Hollywood-Animal-Master-*-Android-Universal.apk"
    label: "Android (Universal APK)"
  - path: "composeApp/build/dist/wasmJs/productionExecutable/Hollywood-Animal-Master-*-Web.zip"
    label: "Web (WASM)"
```

## Automation

If you have scripts that download or process releases, use this pattern to find artifacts:

### Using GitHub CLI
```bash
# Download all assets for a release
gh release download v1.5.0

# Download specific platform and architecture
gh release download v1.5.0 -p "Hollywood-Animal-Master-*-Windows-x64.zip"
gh release download v1.5.0 -p "Hollywood-Animal-Master-*-macOS-ARM64.zip"
```

### Using GitHub API
```bash
# Get release by tag
curl -H "Authorization: token $GITHUB_TOKEN" \
  https://api.github.com/repos/aalbertinib/Hollywood-Animal-Master/releases/tags/v1.5.0

# Download artifact by name pattern
curl -L -H "Authorization: token $GITHUB_TOKEN" \
  https://github.com/aalbertinib/Hollywood-Animal-Master/releases/download/v1.5.0/Hollywood-Animal-Master-1.5.0-Windows-x64.zip \
  -o download.zip
```

### Parsing Pattern in Scripts
```bash
# Extract version, platform, and architecture from filename
FILENAME="Hollywood-Animal-Master-1.5.0-Windows-x64.zip"
VERSION=$(echo $FILENAME | sed -n 's/Hollywood-Animal-Master-\(.*\)-.*-.*\.zip/\1/p')
PLATFORM=$(echo $FILENAME | sed -n 's/Hollywood-Animal-Master-.*-\(.*\)-.*\.zip/\1/p')
ARCH=$(echo $FILENAME | sed -n 's/Hollywood-Animal-Master-.*-.*-\(.*\)\.zip/\1/p')

echo "Version: $VERSION"   # Output: 1.5.0
echo "Platform: $PLATFORM" # Output: Windows
echo "Architecture: $ARCH"  # Output: x64
```

## Migration from Old Pattern

### Old Pattern (before)
```
hollywood-animal-master-1.5.0-windows-x64.zip
hollywood-animal-master-1.5.0-macos-x64.zip
hollywood-animal-master-1.5.0-linux-x64.zip
hollywood-animal-master-1.5.0-signed.apk
hollywood-animal-master-1.5.0-web.zip
```

### New Pattern (current)
```
Hollywood-Animal-Master-1.5.0-Windows-x64.zip
Hollywood-Animal-Master-1.5.0-macOS-x64.zip
Hollywood-Animal-Master-1.5.0-macOS-ARM64.zip
Hollywood-Animal-Master-1.5.0-Linux-x64.zip
Hollywood-Animal-Master-1.5.0-Android-Universal.apk
Hollywood-Animal-Master-1.5.0-Web.zip
```

### Key Differences
1. **App name**: `hollywood-animal-master` → `Hollywood-Animal-Master` (kebab-case)
2. **Architecture explicit**: Now includes `-x64` or `-ARM64` for desktop platforms
3. **macOS Apple Silicon**: Separate ARM64 build for M1/M2/M3 Macs
4. **Android**: `signed`/`unsigned` → `Universal` (includes all Android architectures)
5. **Consistent format**: All platforms follow `{Name}-{Version}-{Platform}-{Arch}` pattern

## Benefits

1. **Consistency** - All platforms follow the same pattern
2. **Readability** - Cleaner, more professional naming
3. **Simplicity** - Easier to parse and automate
4. **Clarity** - Platform name is immediately obvious
5. **Maintainability** - Easier to update and manage

## Testing

To test the naming pattern locally:

```bash
# Build all artifacts
./gradlew packageDistributionForCurrentOS
./gradlew :composeApp:assembleRelease
./gradlew :composeApp:wasmJsBrowserDistribution

# Check artifact names (they should be created with current version from gradle.properties)
find composeApp/build -name "Hollywood-Animal-Master-*"
```

## References

- [Semantic Versioning](https://semver.org/)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [GitHub Releases](https://docs.github.com/en/repositories/releasing-projects-on-github)
- [Semantic Release](https://semantic-release.gitbook.io/)
