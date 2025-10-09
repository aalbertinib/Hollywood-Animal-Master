---
description: How to use Compose Hot Reload for live UI updates
---

# Compose Hot Reload Usage

Compose Hot Reload is now fully configured in your project. Here's how to use it:

## Running with Hot Reload

### Desktop (JVM)
1. Open `main.kt` in the editor
2. Click the Run icon in the gutter next to `fun main()`
3. Select **Run 'composeApp [hotRunJvm]' with Compose Hot Reload (Beta)**
4. Use the **pin icon** in the top-right corner to toggle "Always on Top" mode for easier development

### Alternative: Run via Gradle
```powershell
./gradlew composeApp:hotRunJvm
```

## Making Live Changes

1. **Run the application** using the hot reload task
2. **Edit any `@Composable` function** (e.g., `InputSection.kt`, `CalculatorScreen.kt`)
3. **Save the file** (Ctrl+S)
4. **Changes appear immediately** without restarting the app

## What Can Be Hot Reloaded

✅ **Supported (instant reload):**
- UI layout changes
- Composable function modifications
- Text, colors, and styling
- State updates
- Images and resources

❌ **Not Supported (requires restart):**
- Adding/removing dependencies
- Changing non-Composable functions
- Modifying data classes
- Business logic changes
- Gradle configuration changes

## Troubleshooting

- **Hot reload not working?** Ensure you're using the `hotRunJvm` task, not the regular `run` task
- **LinkageError or UnsupportedClassVersionError?** The project is configured to use Java 21 via `jvmToolchain(21)`. Run `./gradlew clean` and rebuild
- **Changes not appearing?** Make sure to save all files (Ctrl+S)

## Features

### Always on Top Toggle
- A **pin icon** button in the top-right corner (next to the theme toggle) allows you to keep the window always on top
- This feature is **only available on JVM/Desktop** platform
- The pin icon is filled when enabled, outlined when disabled
- Perfect for keeping the app visible while editing code in your IDE

## Configuration Files

The following files have been configured for Compose Hot Reload:
- `gradle/libs.versions.toml` - Plugin version declaration
- `build.gradle.kts` (root) - Plugin applied with `apply false`
- `composeApp/build.gradle.kts` - Plugin applied
- `settings.gradle.kts` - JBR toolchain resolver configured
- `composeApp/src/jvmMain/kotlin/org/aalbertini/ham/main.kt` - Dynamic `alwaysOnTop` state management
- `composeApp/src/commonMain/kotlin/org/aalbertini/ham/App.kt` - Props passed to CalculatorScreen
- `composeApp/src/commonMain/kotlin/org/aalbertini/ham/ui/screen/CalculatorScreen.kt` - Pin toggle button in top bar
