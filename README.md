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

**Hollywood Animal Master** helps players of [**Hollywood Animal**](https://store.steampowered.com/app/2680550/Hollywood_Animal/) decide how many screenings to rent each week of a release.

Enter your movie's scores and the screenings your own cinemas provide. The planner shows, week by week, the audience demand and what is left to rent. After each week, type in the attendance the game reports and the rest of the run recalibrates to what your movie actually draws.

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

### 🧮 Release planner
- Four methods from the community: **Fast decay** (default, 2026 reports), **Steam guide** (the V1 formula), **Two-thirds**, or your own **Custom** curve
- Own cinemas subtracted every week, with per-week overrides when another film uses them
- **Calibration**: enter real Screenings and Attendance after each week and the remaining weeks rescale to your audience
- Week 1 bonus for holiday releases, 4 / 8 / 12-week runs, initial run vs extension split
- Summary (total to rent, peak, rent after week 4) and an interactive chart

### 💾 Library
- Saved movies update automatically as you edit them
- Search, duplicate, delete with undo
- Copy any plan to the clipboard
- Compare 2 to 4 saved movies side by side: inputs, totals (with the difference from the first one picked) and what to rent each week. Duplicate a movie and change the method to compare methods
- Export / import the whole library as text, to move it between devices or keep a backup
- V1 saves are still read; an unreadable save is set aside instead of being overwritten

### 🎭 Material 3 theming
- Full Material 3 color schemes generated from six seed palettes (Golden Age, Red Carpet, Technicolor, Neon Marquee, Backlot, Film Noir)
- Palette styles, contrast levels, light / dark / system
- Android 12+: wallpaper colors
- Adaptive layout: bottom bar on phones, navigation rail and two panes on wide screens

## 📖 How to Use

1. **Plan**: enter the commercial (and optionally artistic) score, your own screenings per week, pick a method.
2. **Release**: book weeks 1–4 with the "to rent" numbers.
3. **Recalibrate**: after each week, open the week card and copy *Screenings* and *Attendance* from the in-game distribution results. The next weeks update.
4. **Extend**: for weeks 5+, use *Setup distribution* in the game. Players report renting after week 4 rarely pays.

See the in-app **Guide** and [docs/V2_AUDIT.md](docs/V2_AUDIT.md) for the sources and patch notes behind each method.

---

## 🎮 About Hollywood Animal

[**Hollywood Animal**](https://store.steampowered.com/app/2680550/Hollywood_Animal/) is a strategic tycoon game where you build and manage a Hollywood movie studio from the 1920s onwards.

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
All your saved movies are stored locally on your device. Nothing is sent to any server. In the web version they live in the browser's storage, which clearing site data erases: use **Library → Export library** to keep a copy.

### How do I change themes?
Open **Settings** in the navigation bar (or rail), then pick a palette, style, contrast and light/dark mode.

### Are the numbers official?
No. Weappy has not published a formula. The methods come from Steam guides and discussions, so use calibration with your real attendance for the best results.

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
