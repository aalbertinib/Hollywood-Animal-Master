# GitHub Workflows - Simplified Structure

## Overview

The GitHub Actions workflows have been simplified based on KMP, Gradle, and GitHub Actions best practices. The new structure separates concerns and optimizes build times.

## Workflow Files

### 1. CI Workflow (`ci.yml`)
**Purpose**: Fast validation for PRs and pushes to main/develop branches

**Runs on**:
- Push to `main` or `develop` (excluding tags)
- Pull requests to `main` or `develop`

**Jobs**:
- **test**: Runs unit tests first (fail fast)
- **validate**: Parallel builds for Android, Desktop, and Web (matrix strategy)

**Key Features**:
- Tests run first for quick feedback
- Parallel validation builds reduce CI time
- Cache-read-only for PRs (doesn't write cache)
- Simplified package lock upgrade (only for web builds)
- No artifact retention beyond 3 days for test results
- Uses `GRADLE_OPTS` for consistent Gradle configuration

### 2. Release Workflow (`release.yml`)
**Purpose**: Full build and release creation on main branch pushes

**Runs on**:
- Push to `main`, `beta`, or `alpha` branches
- Manual dispatch

**Jobs**:
1. **test**: Unit tests (with package lock upgrade)
2. **build-desktop**: Parallel builds for Windows, macOS, Linux
3. **build-android**: Android APK (signed if keystore available)
4. **build-web**: Web WASM build
5. **release**: Semantic release (creates tag + GitHub release with all artifacts)
6. **deploy-web**: Deploys web app to GitHub Pages (only if release created)

**Key Features**:
- All KMP targets built in parallel
- Artifacts organized and uploaded to GitHub Releases
- Semantic versioning with conventional commits
- Version extracted from `gradle.properties` (single source of truth)
- Automatic web deployment after successful release
- Consistent artifact naming: `Hollywood-Animal-Master-{version}-{Platform}-{Arch}`

### 3. Deploy Web Workflow (`deploy-web.yml`)
**Purpose**: Deploy web app to GitHub Pages

**Runs on**:
- Called by release workflow
- Manual dispatch (for debugging)

**Jobs**:
- **deploy**: Build WASM and deploy to GitHub Pages

**Key Features**:
- Reusable workflow
- Package lock upgrade before build
- Clean and minimal implementation
- Proper concurrency control (cancels in-progress deployments)

## Key Improvements

### 1. **Separation of Concerns**
- **CI**: Fast feedback for developers (tests + basic builds)
- **Release**: Complete build + publish pipeline
- **Deploy**: Standalone web deployment

### 2. **Performance Optimizations**
- Parallel builds using matrix strategy
- Gradle caching via `gradle/actions/setup-gradle@v4`
- Cache-read-only for PRs to avoid cache pollution
- Removed redundant setup steps

### 3. **Best Practices Applied**
- Environment variables for common values (`JAVA_VERSION`, `GRADLE_OPTS`)
- Consistent Gradle configuration across workflows
- Proper artifact retention (3-7 days)
- Shell scripts with error handling (`2>/dev/null || true`)
- Conditional steps (keystore, package lock)

### 4. **Simplified Configuration**
- Removed redundant permissions grants for `gradlew`
- Removed `--no-daemon` (handled via `GRADLE_OPTS`)
- Consolidated step names (removed emojis, simplified)
- Better artifact naming conventions

### 5. **Release Process**
The release workflow now:
1. Runs tests first
2. Builds all artifacts in parallel (Desktop x3, Android, Web)
3. Downloads all artifacts
4. Runs semantic-release to create GitHub release
5. Deploys web app to GitHub Pages (if release created)

## Configuration Files

### `.releaserc.yml`
Semantic release configuration:
- Creates tags with format `v{version}`
- Updates `gradle.properties` with new version
- Syncs version to `package.json` and iOS config
- Publishes all artifacts to GitHub Releases
- Uses conventional commits preset

### `gradle.properties`
Single source of truth for version:
```properties
project.version=1.0.0
```

## Usage

### For Development
Push or create PR to trigger CI:
```bash
git push origin feature-branch
```

### For Releases
Use conventional commit messages on main:
```bash
git commit -m "feat: add new feature"  # Minor version bump
git commit -m "fix: resolve bug"       # Patch version bump
git commit -m "feat!: breaking change" # Major version bump
git push origin main
```

Semantic release will automatically:
- Determine version bump based on commit messages
- Update `gradle.properties`
- Build all artifacts
- Create GitHub release with artifacts
- Deploy web app to GitHub Pages

### Manual Web Deployment
Trigger deploy-web workflow manually from GitHub Actions UI if needed.

## Artifact Outputs

All artifacts follow the naming pattern: **`Hollywood-Animal-Master-{SEMANTIC_VERSION}-{PLATFORM}-{ARCH}`**

### Desktop
- `Hollywood-Animal-Master-{version}-Windows-x64.zip`
- `Hollywood-Animal-Master-{version}-macOS-x64.zip` (Intel)
- `Hollywood-Animal-Master-{version}-macOS-ARM64.zip` (Apple Silicon)
- `Hollywood-Animal-Master-{version}-Linux-x64.zip`

### Android
- `Hollywood-Animal-Master-{version}-Android-Universal.apk` (all architectures)

### Web
- `Hollywood-Animal-Master-{version}-Web.zip` (platform-independent)
- Also deployed to GitHub Pages automatically

## Migration Notes

### Changes from Previous Setup
1. **semantic-release.yml** → **release.yml** (renamed for clarity)
2. **Unified naming pattern**: All artifacts now use `Hollywood-Animal-Master-{version}-{Platform}-{Arch}` format with explicit architecture variants
3. Removed redundant job configurations
4. Simplified artifact organization
5. Better matrix strategy for desktop builds
6. Package lock commits removed from deploy-web (handled in release)
7. Cleaner step names and summaries
8. Fixed Kotlin/Wasm package lock upgrade (now includes both JS and WASM)

### Breaking Changes
**Artifact naming has changed** - if you have automation that depends on the old naming pattern, update it to:
- Old: `hollywood-animal-master-{version}-{platform}-x64.{ext}` or `*-signed.apk`
- New: `Hollywood-Animal-Master-{version}-{Platform}-{Arch}.{ext}`
- **macOS now has separate builds** for Intel (x64) and Apple Silicon (ARM64)
- **Android is now Universal** APK containing all architectures

## Future Enhancements

Potential improvements:
1. Add iOS builds to release workflow
2. Implement caching for Kotlin/JS node_modules
3. Add smoke tests after deployment
4. Implement rollback capability
5. Add build time metrics

## Troubleshooting

### Common Issues

**Issue**: Gradle daemon errors
**Solution**: `GRADLE_OPTS` now includes `-Dorg.gradle.daemon=false`

**Issue**: Cache not working
**Solution**: Ensure `gradle/actions/setup-gradle@v4` is used

**Issue**: Package lock changes / `kotlinWasmStorePackageLock` failed
**Solution**: Both `kotlinUpgradePackageLock` and `kotlinWasmUpgradePackageLock` tasks must be run together for WASM builds:
```bash
./gradlew kotlinUpgradePackageLock kotlinWasmUpgradePackageLock --no-build-cache --rerun-tasks
```
This is now automatically handled in all workflows.

**Issue**: Missing artifacts in release
**Solution**: Check artifact names match expected patterns in `.releaserc.yml`

## Resources

- [Gradle Actions Setup](https://github.com/gradle/actions)
- [Semantic Release](https://semantic-release.gitbook.io/)
- [GitHub Actions Best Practices](https://docs.github.com/en/actions/learn-github-actions/best-practices-for-github-actions)
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
