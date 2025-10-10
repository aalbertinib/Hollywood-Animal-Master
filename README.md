# 🎬 Hollywood Animals Master

**Movie Distribution Calculator for Hollywood Animal**

> *Calculate the perfect screening distribution for your movies - one week at a time*

<p align="center">
  <a href="https://store.steampowered.com/app/2680550/Hollywood_Animal/">
    <img src="https://img.shields.io/badge/For-Hollywood%20Animal-gold?style=for-the-badge" alt="For Hollywood Animal"/>
  </a>
  <a href="../../releases/latest">
    <img src="https://img.shields.io/badge/Download-Latest%20Release-blue?style=for-the-badge" alt="Download"/>
  </a>
  <a href="https://aalbertinib.github.io/Hollywood-Animal-Master/">
    <img src="https://img.shields.io/badge/Try-Web%20App-green?style=for-the-badge" alt="Web App"/>
  </a>
</p>

---

## 🎯 What is This?

**Hollywood Animals Master** helps players of [**Hollywood Animal**](https://store.steampowered.com/app/2680550/Hollywood_Animal/) calculate the optimal theater screening distribution for their movies.

When releasing a movie in Hollywood Animal, you need to decide how many screenings to buy each week. This tool does the complex math instantly, showing you exactly how many screenings to purchase week-by-week based on your movie's Commercial Score and your total screening budget.

---

## ⚡ Quick Start

### Try It Now - No Installation!

**🌐 [Open Web App](https://aalbertinib.github.io/Hollywood-Animal-Master/)** - Works in any modern browser

### Or Download for Your Platform

**[📥 Download Latest Release](../../releases/latest)**

| Platform | Download | Size |
|----------|----------|------|
| 🖥️ **Windows** | `Hollywood-Animal-Master-*-Windows-x64.zip` | ~50 MB |
| 🍎 **macOS Intel** | `Hollywood-Animal-Master-*-macOS-x64.zip` | ~50 MB |
| 🍎 **macOS Apple Silicon** | `Hollywood-Animal-Master-*-macOS-ARM64.zip` | ~50 MB |
| 🐧 **Linux** | `Hollywood-Animal-Master-*-Linux-x64.zip` | ~50 MB |
| 🤖 **Android** | `Hollywood-Animal-Master-*-Android-Universal.apk` | ~20 MB |

**Installation:**
1. Download the file for your platform
2. Extract the ZIP file (desktop) or install APK (Android)
3. Run the app and start calculating!

---

## 🎨 Features

### 🧮 Smart Calculator
- ⚡ Instant screening distribution calculations
- 📊 Week-by-week breakdown display
- 🔢 Automatic rounding to whole numbers
- ✅ Input validation with helpful hints

### 💾 Save & Manage
- Save unlimited movie calculations
- Quick load for comparisons
- Edit saved movies anytime
- Copy results to clipboard

### 🎭 Beautiful Themes
Choose from **5 stunning Art Deco themes**:
- **Hollywood Classic** - Authentic game colors with golden accents
- **Midnight Purple** - Deep purples and lavender
- **Ocean Breeze** - Cool blues and aqua
- **Forest Whisper** - Natural greens and earth tones
- **Sunset Glow** - Warm oranges and coral

Each theme supports both **light and dark mode** with smooth animated transitions!

### 📱 Cross-Platform
- 🖥️ **Desktop** - Windows, macOS, Linux
- 🤖 **Android** - Phones & Tablets
- 🍎 **iOS** - iPhone & iPad
- 🌐 **Web** - Any modern browser

---

## 📖 How to Use

### Step 1: Enter Your Movie Data

1. Open the app
2. Enter your **Movie Name** (optional, for saving)
3. Enter **Commercial Score** (found in Hollywood Animal)
4. Enter **Number of Screenings** (your total distribution budget)

### Step 2: Calculate & View Results

Click **Calculate** to see your week-by-week screening distribution:
- Week 1: 1,250 screenings
- Week 2: 980 screenings
- Week 3: 750 screenings
- And so on...

### Step 3: Save or Copy (Optional)

- 💾 **Save** - Click save button to store for later comparison
- 📋 **Copy** - Click copy to paste results into your notes

---

## 🎮 About Hollywood Animal

[**Hollywood Animal**](https://store.steampowered.com/app/2680550/Hollywood_Animal/) is a strategic tycoon game where you build and manage a Hollywood movie studio from the 1920s onwards.

**Key Features:**
- 🎭 Make movies with deep creative control
- 🏢 Build your studio empire
- 📽️ Manage actors, directors, and staff
- 💰 Balance budgets and box office success
- 🎯 Navigate censorship, wars, and industry changes

**Developer:** [Weappy Studio](https://weappy-studio.com/hollywood-animal/)  
**Platform:** [Steam (Windows)](https://store.steampowered.com/app/2680550/Hollywood_Animal/)

---

## ❓ FAQ

### Do I need Hollywood Animal to use this?
While the tool is designed for Hollywood Animal players, it can calculate screening distributions for any similar scenario. However, the calculations are specifically tuned for the game's mechanics.

### Is it free?
Yes! Completely free and open source (GNU AGPL v3).

### Which platforms are supported?
Desktop (Windows, Mac, Linux), Android, iOS, and web browsers.

### Can I use it offline?
Yes! The desktop and mobile apps work offline. Only the web version requires an internet connection.

### Where is my data stored?
All your saved movies are stored locally on your device. Nothing is sent to any server.

### How do I change themes?
Click the Settings icon (⚙️) in the top-right corner, then choose your preferred theme and light/dark mode.

---

## 🔗 Links

- **🎮 Hollywood Animal on Steam:** https://store.steampowered.com/app/2680550/Hollywood_Animal/
- **🌐 Try Web App:** https://aalbertinib.github.io/Hollywood-Animal-Master/
- **📥 Download Releases:** [GitHub Releases](../../releases)
- **🐛 Report Issues:** [GitHub Issues](../../issues)
- **💬 Discussions:** [GitHub Discussions](../../discussions)

---

## 👨‍💻 For Developers

**Want to contribute or build from source?**

See [CONTRIBUTING.md](./CONTRIBUTING.md) for development setup, build instructions, and contribution guidelines.

### Version Management

This project uses centralized version management from `gradle.properties`:

```bash
# View current version
./gradlew :composeApp:printVersion

# Update version: Edit gradle.properties, then sync
./gradlew :composeApp:syncVersionToIOS
```

📖 **Documentation:**
- [VERSION_MANAGEMENT.md](.github/VERSION_MANAGEMENT.md) - Complete guide
- [VERSION_QUICK_REFERENCE.md](.github/VERSION_QUICK_REFERENCE.md) - Quick commands
- [RELEASE_GUIDE.md](.github/RELEASE_GUIDE.md) - Release process

---

## 📝 License

This project is licensed under the **GNU Affero General Public License v3.0** (AGPL-3.0).

This means:
- ✅ You can use, modify, and distribute this software freely
- ✅ If you modify and deploy this as a web service, you must share your changes
- ✅ Commercial use is allowed
- ✅ Patent grant included

See the [LICENSE](LICENSE) file for full details.

---

## 🙏 Credits

- **Game:** [Hollywood Animal](https://store.steampowered.com/app/2680550/Hollywood_Animal/) by [Weappy Studio](https://weappy-studio.com/)
- **Framework:** [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) & [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)

---

<p align="center">
  <b>🎬 Made with ❤️ for Hollywood Animal players 🎬</b>
  <br>
</p>
