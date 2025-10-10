# 🚀 Version Management - Quick Reference

## Change Version

**1. Edit `gradle.properties`:**
```properties
project.version=1.2.3
```

**2. Sync & Verify:**
```bash
./gradlew :composeApp:syncVersionToIOS
./gradlew :composeApp:printVersion
```

**3. Commit:**
```bash
git add gradle.properties iosApp/Configuration/Config.xcconfig
git commit -m "Bump version to 1.2.3"
```

---

## Useful Commands

| Command | Purpose |
|---------|---------|
| `./gradlew :composeApp:printVersion` | Show current version |
| `./gradlew :composeApp:syncVersionToIOS` | Sync to iOS |
| `./gradlew :composeApp:exportVersion` | Export to file |
| `./scripts/get-version.sh` | Get version (bash) |
| `.\scripts\get-version.ps1` | Get version (PowerShell) |

---

## Where Versions Are Used

- ✅ **Android**: `versionName` & `versionCode` (auto)
- ✅ **Desktop**: `packageVersion` (auto)
- ✅ **iOS**: `MARKETING_VERSION` & `CURRENT_PROJECT_VERSION` (sync)
- ✅ **CI/CD**: Artifact names (auto)
- ✅ **Releases**: Tags & notes (auto)

---

## Version Code Formula

```
versionCode = (MAJOR × 10000) + (MINOR × 100) + PATCH

Examples:
1.0.0  → 10000
1.2.3  → 10203
2.0.0  → 20000
```

---

📖 **Full Guide:** [VERSION_MANAGEMENT.md](./VERSION_MANAGEMENT.md)
