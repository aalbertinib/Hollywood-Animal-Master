# Pull Request Requirements

This document explains how to configure GitHub repository settings to enforce PR requirements.

## Required Status Checks for Pull Requests

To ensure code quality, unit tests must pass before PRs can be merged.

### Configure Branch Protection Rules

1. Go to **Settings** → **Branches** in your GitHub repository
2. Click **Add branch protection rule** (or edit existing rule for `main`)
3. Configure the following settings:

#### Branch name pattern
```
main
```

#### Protection settings

✅ **Require a pull request before merging**
- Require approvals: `1` (optional, adjust as needed)

✅ **Require status checks to pass before merging**
- Require branches to be up to date before merging: ✅ (recommended)
- Status checks that are required:
  - `Unit Tests` (from CI workflow)
  - `Build Android` (optional, for comprehensive checks)
  - `Build Desktop` (optional, for comprehensive checks)
  - `Build Web` (optional, for comprehensive checks)

✅ **Do not allow bypassing the above settings** (recommended for team repos)

### Additional Recommended Settings

- ✅ **Require linear history** - Prevents merge commits
- ✅ **Include administrators** - Apply rules to admins too
- ✅ **Restrict who can push to matching branches** - Control who can push directly

## Workflow Triggers

### CI Workflow (`ci.yml`)
Runs on:
- **Pull Requests**: All PRs (opened, synchronized, reopened)
- **Pushes**: To `main` and `develop` branches

**Jobs:**
1. **Unit Tests** (required) - Runs all test suites
2. **Build Validation** - Validates Android, Desktop, and Web builds

### Release Workflow (`release.yml`)
Runs on:
- **Version Tags**: With or without `v` prefix (e.g., `v1.0.0` or `1.0.0`)
- **Manual Trigger**: Via workflow_dispatch

**Supported Tag Patterns (case insensitive):**
- `v1.0.0` or `1.0.0` - Production release
- `v1.0.0-alpha.1` or `1.0.0-ALPHA.1` - Alpha pre-release
- `v1.0.0-beta.2` or `1.0.0-Beta.2` - Beta pre-release  
- `v1.0.0-rc.3` or `1.0.0-RC.3` - Release candidate

## Creating a Release

To trigger a release build:

```bash
# Create and push a version tag (with or without 'v' prefix)
git tag v1.0.0      # or: git tag 1.0.0
git push origin v1.0.0

# Or for pre-release (case insensitive)
git tag v1.0.0-beta.1   # or: 1.0.0-BETA.1
git push origin v1.0.0-beta.1
```

The release workflow will:
1. ✅ Run all unit tests
2. 🔨 Build all platform artifacts (Desktop, Android, Web)
3. 📦 Create GitHub release with artifacts
4. 🌐 Deploy web app to GitHub Pages (if configured)

## Testing Locally

Before pushing, test locally:

```bash
# Run unit tests
./gradlew :composeApp:testReleaseUnitTest

# Build all platforms (requires platform-specific setup)
./gradlew :composeApp:assembleDebug
./gradlew :composeApp:compileKotlinJvm
./gradlew :composeApp:wasmJsBrowserDistribution
```

## FAQ

**Q: Why didn't my push to `main` trigger a release?**  
A: Releases are now triggered only by version tags, not branch pushes. This prevents accidental releases.

**Q: Can I still create releases from branches?**  
A: Yes, use the "Workflow dispatch" button in GitHub Actions to manually trigger a release build.

**Q: What happens if tests fail on a PR?**  
A: The PR cannot be merged until the "Unit Tests" status check passes (if branch protection is enabled).

**Q: How do I skip CI on a commit?**  
A: Not recommended, but you can add `[skip ci]` to your commit message. This bypasses required checks.
