# Hollywood Animal Master

A release planner for [Hollywood Animal](https://store.steampowered.com/app/2680550/Hollywood_Animal/): how many screenings to rent each week of a movie's run.

**[Open the web app](https://aalbertinib.github.io/Hollywood-Animal-Master/)** · [Downloads](../../releases/latest) · [Report a problem](../../issues)

## What it does

You enter the movie's commercial score (and artistic score if you have it) and how many screenings your own cinemas provide per week. The planner works out the audience for each week and how many screenings you still need to rent.

After a week has run, copy the game's **Screenings** and **Attendance** into that week. The weeks that follow are rescaled to what your movie actually drew, so you stop renting cinemas that stay empty.

There is no official formula. The planner offers the methods players have worked out on Steam, and links each one to the guide or discussion it comes from:

- **Fast decay** (default): average score × 2,000 in week 1, then roughly halving. Closest to recent attendance reports.
- **Steam guide**: the formula version 1 of this tool used (−20 % a week).
- **Two-thirds**: −1/3 a week.
- **Custom**: your own curve.

Other things it does:

- Saves movies, and keeps saving them as you edit.
- Compares 2 to 4 saved movies side by side, for example the same movie under two methods.
- Exports the whole library as text and imports it on another device.
- Copies a plan to the clipboard.
- Has a light and a dark theme, several color palettes, and wallpaper colors on Android 12+.

The in-app **Guide** explains the weekly routine and lists the patch notes that affect distribution.

## Download

The [latest release](../../releases/latest) has:

| Platform | File |
|---|---|
| Windows | `Hollywood-Animal-Master-<version>-Windows-x64.zip` |
| macOS (Apple Silicon) | `Hollywood-Animal-Master-<version>-macOS-ARM64.zip` |
| macOS (Intel) | `Hollywood-Animal-Master-<version>-macOS-x64.zip` |
| Linux | `Hollywood-Animal-Master-<version>-Linux-x64.zip` |
| Android | `Hollywood-Animal-Master-<version>-Android-Universal.apk` |

Desktop builds are zip files: extract and run. The web app needs no install. There is no iOS download; it can be built from source on a Mac.

## Your data

Everything stays on your device; nothing is sent anywhere. The web app keeps your movies in the browser's storage, which is erased if you clear the site's data. Use **Library → Export library** to keep a copy.

Saves from version 1 still open.

## Building from source

You need JDK 21 and, for Android, the Android SDK (API 37).

```bash
./gradlew :composeApp:run                          # desktop
./gradlew :composeApp:wasmJsBrowserDevelopmentRun  # web, on http://localhost:8080
./gradlew :androidApp:installDebug                 # Android device or emulator
./gradlew :composeApp:jvmTest                      # tests
```

For iOS, open `iosApp/iosApp.xcodeproj` in Xcode on a Mac.

The code is a Kotlin Multiplatform / Compose Multiplatform project:

- `composeApp`: all shared code (planner logic, screens, storage), plus the desktop, web and iOS entry points.
- `androidApp`: the Android app shell (activity, manifest, icons, signing).

Android Studio run configurations are included in `.run/`.

The version lives in `gradle.properties` (`project.version`). Releases are made by the Release workflow with semantic-release, which reads [Conventional Commits](https://www.conventionalcommits.org/) messages. See [CONTRIBUTING.md](CONTRIBUTING.md) and [.github/RELEASE_GUIDE.md](.github/RELEASE_GUIDE.md).

## License

[AGPL-3.0](LICENSE). Fan-made and not affiliated with Weappy Studio, the developer of Hollywood Animal.
