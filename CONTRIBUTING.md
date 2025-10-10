# 🛠️ Contributing to Hollywood Animals Master

Thank you for your interest in contributing! This document provides all the technical information you need to build, develop, and contribute to Hollywood Animals Master.

---

## 📋 Table of Contents

- [Development Setup](#development-setup)
- [Building from Source](#building-from-source)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [CI/CD & Releases](#cicd--releases)
- [Code Style](#code-style)
- [Testing](#testing)
- [Documentation](#documentation)
- [Pull Request Process](#pull-request-process)

---

## Development Setup

### Prerequisites

- **JDK 21 or higher** - [Download](https://adoptium.net/)
- **Android Studio** or **IntelliJ IDEA** - [Download](https://developer.android.com/studio)
- **Git** - [Download](https://git-scm.com/)

### Clone the Repository

```bash
git clone https://github.com/aalbertinib/Hollywood-Animal-Master.git
cd Hollywood-Animal-Master
```

### Open in IDE

1. Open Android Studio or IntelliJ IDEA
2. Select "Open" and navigate to the cloned directory
3. Wait for Gradle sync to complete
4. You're ready to develop!

---

## Building from Source

### Desktop (Windows/Mac/Linux)

```bash
# Run the desktop app
./gradlew :composeApp:run            # Mac/Linux
.\gradlew.bat :composeApp:run        # Windows

# Build desktop JAR
./gradlew :composeApp:jvmJar
```

### Android

```bash
# Build debug APK
./gradlew :composeApp:assembleDebug

# Build release APK
./gradlew :composeApp:assembleRelease

# Install to connected device
./gradlew :composeApp:installDebug
```

APK location: `composeApp/build/outputs/apk/`

### Web (WASM)

```bash
# Run development server
./gradlew :composeApp:wasmJsBrowserDevelopmentRun

# Build production bundle
./gradlew :composeApp:wasmJsBrowserDistribution
```

Open http://localhost:8080

### iOS

```bash
# Open Xcode project
open iosApp/iosApp.xcodeproj

# Or build from command line
cd iosApp
xcodebuild -scheme iosApp -configuration Debug
```

### All Platforms

```bash
# Compile everything
./gradlew build

# Run tests
./gradlew test

# Clean build
./gradlew clean
```

---

## Technology Stack

### Core Technologies

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose / Compose Multiplatform
- **Architecture:** MVVM with StateFlow
- **Dependency Injection:** Koin
- **Serialization:** kotlinx.serialization
- **Build System:** Gradle with Kotlin DSL

### Platform Targets

- **Android** - Min SDK 24, Target SDK 34
- **iOS** - iOS 15+
- **Desktop** - JVM 21 (Windows, macOS, Linux)
- **Web** - WASM/JS (modern browsers)

### Key Libraries

- `androidx.lifecycle` - ViewModel and lifecycle management
- `androidx.compose.material3` - Material Design 3 components
- `kotlinx.coroutines` - Asynchronous programming
- `koin` - Dependency injection
- `kotlinx.serialization` - JSON serialization for data persistence

---

## Project Structure

```
Hollywood-Animal-Master/
├── .github/                    # GitHub workflows and documentation
│   ├── workflows/
│   │   ├── ci.yml              # Continuous Integration
│   │   ├── release.yml         # Release builds
│   │   └── deploy-web.yml      # Web app deployment
│   ├── GITHUB_SETUP.md         # GitHub integration guide
│   ├── RELEASE_GUIDE.md        # Release process
│   └── ANDROID_SIGNING_GUIDE.md # Android signing
│
├── composeApp/                 # Main application code
│   └── src/
│       ├── commonMain/         # Cross-platform code
│       │   ├── kotlin/
│       │   │   └── org/aalbertini/ham/
│       │   │       ├── model/           # Data models
│       │   │       ├── ui/              # UI components & screens
│       │   │       ├── viewmodel/       # ViewModels
│       │   │       ├── storage/         # Data persistence
│       │   │       ├── preferences/     # Settings management
│       │   │       └── resources/       # UI constants
│       │   └── composeResources/        # Shared resources
│       │       └── drawable/            # Images and icons
│       │
│       ├── androidMain/        # Android-specific
│       ├── iosMain/            # iOS-specific
│       ├── jvmMain/            # Desktop-specific
│       └── wasmJsMain/         # Web-specific
│
├── iosApp/                     # iOS app entry point
├── design-assets/              # Design source files
│   └── icon/                   # Icon generation
├── docs/                       # Documentation and screenshots
│   └── images/                 # README images
│
├── build.gradle.kts            # Root build configuration
├── settings.gradle.kts         # Project settings
├── gradle.properties           # Gradle properties
└── README.md                   # User documentation
```

### Source Code Organization

#### Common Module (`commonMain`)

- **`model/`** - Data classes and enums
  - `MovieResult.kt` - Movie calculation data
  - `ThemePreset.kt` - Theme color schemes
  
- **`ui/`** - UI components
  - `components/` - Reusable UI components
  - `layout/` - Layout utilities
  - `screen/` - Main screens
  - `state/` - UI state and events
  - `theme/` - Theming system

- **`viewmodel/`** - Business logic
  - `MovieDistributionViewModel.kt` - Main calculator logic

- **`storage/`** - Data persistence (expect/actual pattern)

- **`preferences/`** - Settings management

#### Platform-Specific Modules

- **`androidMain/`** - Android-specific implementations
- **`iosMain/`** - iOS-specific implementations
- **`jvmMain/`** - Desktop entry point and specific code
- **`wasmJsMain/`** - Web-specific implementations

---

## CI/CD & Releases

### GitHub Actions Workflows

#### Continuous Integration (`ci.yml`)

Runs on every push and pull request:
- Builds JVM, Android, and Web targets
- Runs automated tests
- Uploads build artifacts (7-day retention)

#### Release (`release.yml`)

Triggers on GitHub Release:
- Builds for Windows, macOS, Linux (desktop)
- Creates signed Android APK (if keystore configured)
- Builds Web WASM bundle
- Uploads all artifacts to release

#### Web Deployment (`deploy-web.yml`)

Deploys to GitHub Pages:
- On release
- On push to `main` (when app code changes)
- Manual trigger

### Creating a Release

See [`.github/RELEASE_GUIDE.md`](./.github/RELEASE_GUIDE.md) for detailed instructions.

**Quick steps:**

```bash
# Create and push tag
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0

# Create GitHub Release from tag
# Builds start automatically
```

### Android Signing

See [`.github/ANDROID_SIGNING_GUIDE.md`](./.github/ANDROID_SIGNING_GUIDE.md) for complete setup.

---

## Code Style

### Kotlin Style Guide

Follow the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html):

- Use 4 spaces for indentation
- Maximum line length: 120 characters
- Use descriptive variable names
- Add KDoc comments for public APIs

### Compose Guidelines

- Extract composables when they exceed ~30 lines
- Use `remember` for computations
- Hoist state to ViewModels
- Use `Modifier` parameters for styling
- Follow Material Design 3 guidelines

### Architecture Principles

- **MVVM** - ViewModels handle business logic
- **Single source of truth** - StateFlow in ViewModel
- **Unidirectional data flow** - Events up, state down
- **Dependency injection** - Use Koin for dependencies
- **Separation of concerns** - UI, logic, data layers

### Example Code Style

```kotlin
/**
 * Calculates weekly screening distribution for a movie.
 * 
 * @param commercialScore The movie's commercial appeal (0-100)
 * @param totalScreenings Total screenings to distribute
 * @return List of screening counts per week
 */
fun calculateDistribution(
    commercialScore: Double,
    totalScreenings: Double
): List<Int> {
    // Implementation
}
```

---

## Testing

### Running Tests

```bash
# Run all tests
./gradlew test

# Run specific target tests
./gradlew :composeApp:jvmTest
./gradlew :composeApp:androidUnitTest
```

### Writing Tests

Tests go in `commonTest/`:

```kotlin
class MovieDistributionCalculatorTest {
    @Test
    fun testCalculation() {
        val result = calculateDistribution(
            commercialScore = 85.0,
            totalScreenings = 50000.0
        )
        
        assertTrue(result.isNotEmpty())
        assertEquals(50000, result.sum())
    }
}
```

### Test Coverage

Aim for:
- **Unit tests** - Business logic (ViewModels, calculators)
- **Integration tests** - Data persistence
- **UI tests** - Critical user flows (when feasible)

---

## Documentation

### Where to Document

| Type | Location |
|------|----------|
| User guide | `README.md` |
| Developer guide | `CONTRIBUTING.md` (this file) |
| Release process | `.github/RELEASE_GUIDE.md` |
| GitHub setup | `.github/GITHUB_SETUP.md` |
| Android signing | `.github/ANDROID_SIGNING_GUIDE.md` |
| Icon generation | `design-assets/icon/ICON_GENERATION_GUIDE.md` |
| Screenshots | `docs/images/README.md` |
| Code | KDoc comments in source files |

### Documentation Standards

- Keep README.md user-focused (no technical details)
- Use Markdown for all documentation
- Include code examples where helpful
- Keep documentation up-to-date with code changes

---

## Pull Request Process

### Before Submitting

1. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make your changes**
   - Follow code style guidelines
   - Add tests for new functionality
   - Update documentation if needed

3. **Test locally**
   ```bash
   ./gradlew build
   ./gradlew test
   ```

4. **Commit with meaningful messages**
   ```bash
   git commit -m "Add feature: screening distribution visualization"
   ```

5. **Push to your fork**
   ```bash
   git push origin feature/your-feature-name
   ```

### Pull Request Template

```markdown
## Description
Brief description of what this PR does

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
How has this been tested?

## Checklist
- [ ] Code follows project style guidelines
- [ ] Tests added/updated
- [ ] Documentation updated
- [ ] All tests passing
- [ ] No new warnings
```

### Review Process

1. Automated CI checks must pass
2. Code review by maintainer(s)
3. Address review comments
4. Merge when approved

---

## Design Assets

### Icon Generation

See `design-assets/icon/ICON_GENERATION_GUIDE.md` for:
- Generating platform-specific icons
- Icon specifications and sizes
- Design guidelines

### Screenshots

See `docs/images/README.md` for:
- Screenshot requirements
- Naming conventions
- Optimization guidelines

---

## Helpful Commands

### Gradle

```bash
# List all tasks
./gradlew tasks

# Check for dependency updates
./gradlew dependencyUpdates

# Generate documentation
./gradlew dokkaHtml

# Clean build cache
./gradlew clean cleanBuildCache
```

### Git

```bash
# Update from main
git pull origin main

# Rebase feature branch
git rebase main

# Interactive rebase (clean up commits)
git rebase -i HEAD~3
```

---

## Getting Help

### Resources

- **Kotlin Multiplatform:** https://www.jetbrains.com/help/kotlin-multiplatform-dev/
- **Compose Multiplatform:** https://github.com/JetBrains/compose-multiplatform
- **Material Design 3:** https://m3.material.io/
- **Koin:** https://insert-koin.io/

### Community

- **Issues:** [GitHub Issues](../../issues)
- **Discussions:** [GitHub Discussions](../../discussions)
- **Hollywood Animal Discord:** (if available)

---

## License

By contributing, you agree that your contributions will be licensed under the **GNU Affero General Public License v3.0** (AGPL-3.0).

This means any modifications deployed as a web service must have source code made available to users.

---

## Questions?

Feel free to open an issue or discussion if you have questions about:
- Development setup
- Architecture decisions
- Best practices
- Feature proposals

**Thank you for contributing! 🎬**
