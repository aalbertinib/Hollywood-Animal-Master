# 🎬 Hollywood Animals Master (HAM)

**A Movie Distribution Calculator for Hollywood Animal**

> *"Making the right moves, one theater at a time"*

<p align="center">
  <img src="docs/images/app-hero.png" alt="Hollywood Animals Master App" width="800"/>
</p>

<p align="center">
  <a href="https://store.steampowered.com/app/2680550/Hollywood_Animal/">
    <img src="https://img.shields.io/badge/For-Hollywood%20Animal-gold?style=for-the-badge" alt="For Hollywood Animal"/>
  </a>
  <a href="#-download--install">
    <img src="https://img.shields.io/badge/Platform-Windows%20|%20macOS%20|%20Linux%20|%20Android%20|%20iOS%20|%20Web-blue?style=for-the-badge" alt="Platforms"/>
  </a>
  <a href="LICENSE">
    <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" alt="License"/>
  </a>
</p>

---

## 🎯 What is This Tool?

**Hollywood Animals Master** is a companion calculator designed to help players of [**Hollywood Animal**](https://store.steampowered.com/app/2680550/Hollywood_Animal/) — the critically acclaimed movie studio management game by Weappy Studio — make smarter distribution decisions for their films.

In Hollywood Animal, you run a movie studio from the 1920s onwards, managing everything from film production to theater distribution. One of the game's strategic challenges is deciding **how many seats to buy each week** when distributing your movies to theaters. This tool does the complex math for you!

### 🤔 The Problem It Solves

When releasing a movie in Hollywood Animal, you need to calculate the optimal number of theater seats to purchase across multiple weeks. The calculation involves:
- Your movie's **Commercial Score** (how popular/marketable it is)
- The total **Number of Seats** you want to fill
- A complex distribution algorithm across weeks

Doing this math manually is tedious and error-prone. **Hollywood Animals Master** automates this entirely!

### ✨ How It Works

Simply enter:
1. **Your movie's Commercial Score** (from the game)
2. **How many seats you want to buy** (your distribution budget)
3. Click **Calculate**

The tool instantly shows you:
- 📊 **Week-by-week breakdown** of how many seats to purchase
- 💾 **Save multiple movies** for quick comparison
- 📋 **Copy results** to paste into your notes
- 🎨 **Beautiful interface** with multiple vintage Hollywood themes

<p align="center">
  <img src="docs/images/calculator-demo.gif" alt="Calculator in Action" width="700"/>
  <br>
  <em>See the calculator in action with live calculations and smooth animations</em>
</p>

---

## 🎮 About Hollywood Animal

[**Hollywood Animal**](https://store.steampowered.com/app/2680550/Hollywood_Animal/) is a strategic tycoon game where you build and manage a Hollywood movie studio from the dawn of sound cinema through decades of creative achievements and industry evolution.

**Key features of the game:**
- 🎭 Make movies with deep creative control
- 🏢 Build your studio empire your way
- 📽️ Manage actors, directors, and staff
- 💰 Balance budgets and box office success
- 🎪 Navigate censorship, wars, and industry upheaval
- 🎯 Choose between blockbusters or art-house films

**Developer:** [Weappy Studio](https://weappy-studio.com/hollywood-animal/)  
**Platform:** Steam (Windows)  
**Genre:** Strategy, Tycoon, Management

---

## 🚀 Features

### Core Calculator
- ⚡ **Instant calculations** for movie distribution
- 📊 **Week-by-week seat allocation** display
- 🔢 **Smart rounding** to whole numbers
- ✅ **Input validation** with helpful error messages

### Data Management
- 💾 **Save unlimited movies** with custom titles
- 📝 **Edit saved calculations** anytime
- 🗑️ **Delete individual entries** or clear all
- 💿 **Persistent storage** across sessions
- 🔄 **Load previous calculations** instantly

### User Experience
- 🎨 **5 Beautiful Themes** (Art Deco inspired)
  - Hollywood Classic (Gold & Sepia)
  - Midnight Purple (Deep purples)
  - Ocean Breeze (Cool blues)
  - Forest Whisper (Natural greens)
  - Sunset Glow (Warm oranges)
- 🌓 **Dark/Light mode** with smooth transitions
- 🎬 **Circular reveal animations** for theme changes
- 📱 **Responsive design** for all screen sizes
- ♿ **Accessible UI** with tooltips and clear labels
- 📋 **Copy to clipboard** functionality

<details>
<summary><b>🎨 View All Themes</b></summary>

<table>
  <tr>
    <td align="center">
      <img src="docs/images/theme-hollywood-classic-light.png" width="350" alt="Hollywood Classic Light"/>
      <br>
      <b>Hollywood Classic (Light)</b>
    </td>
    <td align="center">
      <img src="docs/images/theme-hollywood-classic-dark.png" width="350" alt="Hollywood Classic Dark"/>
      <br>
      <b>Hollywood Classic (Dark)</b>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="docs/images/theme-midnight-purple-light.png" width="350" alt="Midnight Purple Light"/>
      <br>
      <b>Midnight Purple (Light)</b>
    </td>
    <td align="center">
      <img src="docs/images/theme-midnight-purple-dark.png" width="350" alt="Midnight Purple Dark"/>
      <br>
      <b>Midnight Purple (Dark)</b>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="docs/images/theme-ocean-breeze.png" width="350" alt="Ocean Breeze"/>
      <br>
      <b>Ocean Breeze</b>
    </td>
    <td align="center">
      <img src="docs/images/theme-sunset-glow.png" width="350" alt="Sunset Glow"/>
      <br>
      <b>Sunset Glow</b>
    </td>
  </tr>
</table>

</details>

### Cross-Platform
- 🖥️ **Windows Desktop** (JVM)
- 🤖 **Android** phones & tablets
- 🍎 **iOS** iPhone & iPad
- 📺 **Android TV**
- 🌐 **Web Browser** (WASM)

<p align="center">
  <img src="docs/images/platforms-showcase.png" alt="Cross-Platform Support" width="800"/>
  <br>
  <em>One codebase, runs everywhere - Desktop, Mobile, Tablet, TV, and Web</em>
</p>

---

## 🎨 Design Philosophy

The app features an authentic **1920s Art Deco aesthetic** to match Hollywood Animal's golden age setting:

- **Typography:** Bold HAM lettering inspired by vintage Hollywood Boulevard signage
- **Colors:** Rich sepia browns, golden gradients, and classic Hollywood glamour
- **Patterns:** Geometric Art Deco designs and film reel elements
- **Themes:** Each color scheme maintains the period aesthetic while offering variety

All visual elements are designed to evoke the elegance and drama of classic Hollywood cinema.

---

## 📥 Download & Install

### Desktop (Windows/Mac/Linux)
```bash
# Clone the repository
git clone https://github.com/yourusername/Hollywood-Animals-Master.git

# Run on Windows
.\gradlew.bat :composeApp:run

# Run on Mac/Linux
./gradlew :composeApp:run
```

### Android
Build the APK:
```bash
.\gradlew.bat :composeApp:assembleDebug
```
Install the APK from `composeApp/build/outputs/apk/`

### Web Browser
```bash
.\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
```
Then open http://localhost:8080

### iOS
Open `iosApp/iosApp.xcodeproj` in Xcode and run

---

## 🛠️ Technology Stack

This is a **Kotlin Multiplatform** project built with modern technologies:

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose / Compose Multiplatform
- **Architecture:** MVVM with StateFlow
- **Platforms:** Android, iOS, Desktop (JVM), Web (WASM/JS), TV
- **Storage:** Platform-specific persistent storage
- **Testing:** Kotlin Test framework

### Project Structure

```
Hollywood-Animals-Master/
├── composeApp/                 # Shared application code
│   └── src/
│       ├── commonMain/         # Cross-platform code
│       ├── androidMain/        # Android-specific
│       ├── iosMain/            # iOS-specific
│       ├── jvmMain/            # Desktop-specific
│       └── wasmJsMain/         # Web-specific
├── iosApp/                     # iOS app entry point
├── design-assets/              # Source design files
│   └── icon/                   # App icon & generation tools
└── gradle/                     # Build configuration
```

---

## 🎯 How to Use

### Quick Start

1. **Launch the app** on your preferred platform
2. **Enter your movie's Commercial Score** (found in Hollywood Animal)
3. **Enter your total Number of Seats** budget
4. **Click Calculate** to see the week-by-week breakdown
5. **Optional:** Save your calculation with a movie title

<table>
  <tr>
    <td width="50%">
      <img src="docs/images/screenshot-parameters.png" alt="Enter Parameters"/>
      <br>
      <b>Step 1: Enter Movie Parameters</b>
      <br>
      Input your Commercial Score and Number of Seats from the game
    </td>
    <td width="50%">
      <img src="docs/images/screenshot-results.png" alt="View Results"/>
      <br>
      <b>Step 2: View Week-by-Week Results</b>
      <br>
      See exactly how many seats to buy each week
    </td>
  </tr>
</table>

### Saving Movies

Click the **Save** button (💾) to store your calculation:
- Add a memorable title for your movie
- Access it later from the "Saved Movies" section
- Compare different distribution strategies

<p align="center">
  <img src="docs/images/screenshot-saved-movies.png" alt="Saved Movies Section" width="600"/>
  <br>
  <em>Manage all your saved calculations in one place</em>
</p>

### Copying Results

Click the **Copy** button (📋) to copy results to clipboard:
```
Results
Week 1: 1,250
Week 2: 980
Week 3: 750
...
```
Perfect for pasting into your game notes!

### Changing Themes

- Click the **Settings** button (⚙️) to open theme selection
- Choose from 5 Art Deco-inspired themes
- Toggle between Light and Dark modes
- Watch the smooth circular reveal animation!

<p align="center">
  <img src="docs/images/theme-animation.gif" alt="Theme Change Animation" width="600"/>
  <br>
  <em>Smooth circular reveal animation when changing themes</em>
</p>

---

## 🤝 Contributing

Contributions are welcome! This project is built with:
- **Best practices:** Clean architecture, SOLID principles
- **Type safety:** Full Kotlin type system
- **Modern UI:** Material Design 3
- **Accessibility:** WCAG compliant color contrasts

### Development Setup

1. Install [Android Studio](https://developer.android.com/studio) or [IntelliJ IDEA](https://www.jetbrains.com/idea/)
2. Install JDK 17 or higher
3. Clone the repository
4. Open in IDE and sync Gradle
5. Run on your target platform

### Build Commands

```bash
# Compile all platforms
.\gradlew.bat build

# Run desktop app
.\gradlew.bat :composeApp:run

# Run tests
.\gradlew.bat test

# Generate icons (requires ImageMagick)
cd design-assets/icon
.\generate-icons.ps1
```

---

## 📚 Documentation

- **Design Assets:** See [design-assets/README.md](./design-assets/README.md)
- **Icon Generation:** See [design-assets/icon/ICON_GENERATION_GUIDE.md](./design-assets/icon/ICON_GENERATION_GUIDE.md)
- **Screenshots Guide:** See [docs/images/README.md](./docs/images/README.md)
- **KMP Documentation:** [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- **Compose Multiplatform:** [Official Docs](https://github.com/JetBrains/compose-multiplatform)

### 📸 Adding Screenshots

This README references several screenshots that showcase the app's features. To add screenshots:

1. Review the screenshot requirements in [docs/images/README.md](./docs/images/README.md)
2. Take screenshots following the guidelines
3. Optimize images for web (< 500KB for PNGs, < 2MB for GIFs)
4. Save with the exact filenames specified
5. Place in the `docs/images/` directory
6. Verify all images display correctly on GitHub

**Pro tip:** Use `@Preview` composables in Android Studio to easily capture clean screenshots!

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Credits

- **Game:** [Hollywood Animal](https://store.steampowered.com/app/2680550/Hollywood_Animal/) by [Weappy Studio](https://weappy-studio.com/)
- **Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) & [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- **Icon Design:** Custom 1920s Art Deco design
- **Fonts:** System fonts with Material Design 3 typography

---

## 🔗 Links

- **Hollywood Animal on Steam:** https://store.steampowered.com/app/2680550/Hollywood_Animal/
- **Weappy Studio:** https://weappy-studio.com/
- **Kotlin Multiplatform:** https://kotlinlang.org/docs/multiplatform.html
- **Compose Multiplatform:** https://www.jetbrains.com/lp/compose-multiplatform/

---

<p align="center">
  <b>🎬 Made with ❤️ for Hollywood Animal players 🎬</b>
  <br>
  <i>"In Hollywood, nobody knows anything... except the math."</i>
</p>
