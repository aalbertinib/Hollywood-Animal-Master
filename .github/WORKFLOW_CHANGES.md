# GitHub Workflows - Recent Updates

## Summary of Changes

The GitHub Actions workflows have been updated to improve the release and CI/CD process.

## Key Changes

### 1. Release Workflow - Tag-Based Releases ✅

**Before:**
- Triggered on pushes to `main`, `beta`, `alpha` branches
- Could cause unintended releases

**After:**
- **Only triggers on version tags**
- Supported tag patterns (with or without `v` prefix, case insensitive):
  - `v1.0.0` or `1.0.0` - Production release
  - `v1.0.0-alpha.1` or `1.0.0-ALPHA.1` - Alpha pre-release
  - `v1.0.0-beta.2` or `1.0.0-Beta.2` - Beta pre-release
  - `v1.0.0-rc.3` or `1.0.0-RC.3` - Release candidate

**Benefits:**
- ✅ No accidental releases from branch pushes
- ✅ Explicit versioning through tags
- ✅ Cleaner release history
- ✅ Better control over when releases happen

### 2. CI Workflow - Required PR Tests ✅

**Before:**
- Ran on PRs but not enforced

**After:**
- **Runs on all pull requests**
- **Unit tests must pass before merge** (when branch protection enabled)
- Triggers on: opened, synchronized, reopened PRs

**Benefits:**
- ✅ Ensures code quality
- ✅ Prevents broken code from being merged
- ✅ Automated testing on every PR
- ✅ Fast feedback for contributors

## How to Use

### Creating a Release

**Old Way (no longer works):**
```bash
git push origin main  # Would trigger release
```

**New Way:**
```bash
# Update version in gradle.properties first
# Use either format (with or without v prefix)
git tag v1.0.0   # or: git tag 1.0.0
git push origin v1.0.0  # Triggers release workflow

# Pre-releases (case insensitive)
git tag v1.0.0-beta.1  # or: 1.0.0-BETA.1
git push origin v1.0.0-beta.1
```

### Pull Request Workflow

1. Create feature branch
2. Make changes and push
3. Create PR on GitHub
4. **CI automatically runs unit tests**
5. **PR cannot merge until tests pass** (if branch protection enabled)
6. Merge when all checks are green ✅

## Required Setup

### Enable Branch Protection (Recommended)

To enforce PR testing requirements:

1. Go to **Settings** → **Branches**
2. Add rule for `main` branch
3. Enable: **Require status checks to pass before merging**
4. Select: **Unit Tests** as required check

See [PR_REQUIREMENTS.md](./PR_REQUIREMENTS.md) for detailed instructions.

## Migration Guide

If you have existing workflows or scripts that relied on the old behavior:

### Semantic Release (if using)

If you're using semantic-release, update your configuration to work with tags instead of branches.

### Automation Scripts

Update any scripts that assumed releases happen on branch pushes:

```bash
# Old
git push origin main

# New
VERSION="1.0.0"
git tag "v${VERSION}"
git push origin "v${VERSION}"
```

### Branch Strategy

Your branch workflow remains the same, just add the tag step:

```bash
# Git Flow example
git checkout -b release/1.0.0 develop
# ... make changes ...
git checkout main
git merge release/1.0.0
git tag v1.0.0              # ← Add this
git push origin main --tags # ← Push tags
```

## Files Modified

1. **`.github/workflows/release.yml`**
   - Changed trigger from branches to tags
   - Added tag pattern matching

2. **`.github/workflows/ci.yml`**
   - Updated PR triggers
   - Clarified test requirements

3. **`.github/RELEASE_GUIDE.md`**
   - Updated release instructions
   - Added PR workflow section
   - Added version tag examples

4. **`.github/PR_REQUIREMENTS.md`** (new)
   - Complete guide for setting up PR requirements
   - Branch protection configuration
   - Testing workflow

## Testing the Changes

### Test Release Workflow

```bash
# Create a test tag
git tag v0.0.1-test
git push origin v0.0.1-test

# Watch workflow in GitHub Actions
# Delete tag when done: git push --delete origin v0.0.1-test
```

### Test CI Workflow

```bash
# Create a test branch
git checkout -b test/ci-check
git push origin test/ci-check

# Create PR on GitHub
# Watch CI run automatically
```

## Rollback Plan

If issues arise, you can manually trigger workflows:

1. Go to **Actions** in GitHub
2. Select **Release** workflow
3. Click **Run workflow**
4. Select branch and run

## Support

For questions or issues:
1. Check [RELEASE_GUIDE.md](./RELEASE_GUIDE.md)
2. Check [PR_REQUIREMENTS.md](./PR_REQUIREMENTS.md)
3. Review [GitHub Actions logs](../../actions)
4. Open an issue if needed

---

**Updated:** 2025-10-11  
**Changes by:** Workflow automation update
