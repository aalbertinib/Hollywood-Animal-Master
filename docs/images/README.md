# 📸 Screenshots & Images Guide

This directory contains all images and screenshots used in the main README.md file.

## 📋 Required Screenshots

### Hero Image
- **Filename:** `app-hero.png`
- **Size:** 1600x900px (or similar 16:9 ratio)
- **Description:** Main hero image showing the app interface
- **Recommendation:** Full app window with a nice calculation example, Midnight Purple theme

### Calculator Demo
- **Filename:** `calculator-demo.gif`
- **Size:** 1400x800px (animated GIF)
- **Description:** Animated demonstration of using the calculator
- **Recommendation:** Show entering values → clicking calculate → results appearing

### Platform Showcase
- **Filename:** `platforms-showcase.png`
- **Size:** 1600x900px
- **Description:** Collage showing the app running on different platforms
- **Recommendation:** Desktop + Mobile + Tablet + Web browser side-by-side

### Screenshots - Usage Guide

#### Parameters Input
- **Filename:** `screenshot-parameters.png`
- **Size:** 800x600px
- **Description:** Parameters section with filled values
- **Recommendation:** Show "Commercial Score: 85" and "Number of Seats: 50000"

#### Results Display
- **Filename:** `screenshot-results.png`
- **Size:** 800x600px
- **Description:** Results section expanded with week-by-week breakdown
- **Recommendation:** Show clear weekly values

#### Saved Movies Section
- **Filename:** `screenshot-saved-movies.png`
- **Size:** 1200x800px
- **Description:** Saved Movies section with 3-5 saved calculations
- **Recommendation:** Show variety of movie titles and parameters

### Theme Screenshots

Create screenshots for each theme in both light and dark mode:

#### Hollywood Classic
- `theme-hollywood-classic-light.png` (700x500px)
- `theme-hollywood-classic-dark.png` (700x500px)

#### Midnight Purple
- `theme-midnight-purple-light.png` (700x500px)
- `theme-midnight-purple-dark.png` (700x500px)

#### Ocean Breeze
- `theme-ocean-breeze.png` (700x500px)

#### Sunset Glow
- `theme-sunset-glow.png` (700x500px)

#### Forest Whisper
- `theme-forest-whisper.png` (700x500px)

**Recommendation:** Show the same calculator state in each theme for consistency

### Theme Animation
- **Filename:** `theme-animation.gif`
- **Size:** 1200x800px (animated GIF)
- **Description:** Circular reveal animation when changing themes
- **Recommendation:** Record clicking theme toggle button, show smooth circular reveal

## 🎨 Screenshot Guidelines

### General Requirements
- **Format:** PNG for static images, GIF for animations
- **Quality:** High resolution, crisp text
- **Compression:** Optimize file size without losing quality (use tools like TinyPNG)
- **Consistency:** Use same window size for similar screenshots
- **Clean UI:** No debug overlays, clean data

### Content Guidelines
- **Sample Data:** Use realistic Hollywood movie titles and scores
  - Example titles: "The Jazz Singer", "Gone with the Wind", "Casablanca"
  - Commercial scores: 75-95 range
  - Seat counts: 25,000-75,000 range
- **Theme:** Prefer Midnight Purple or Hollywood Classic for main screenshots
- **State:** Show app in use, not empty states

### Recording Animations (GIF)
- **FPS:** 30-60 fps for smooth playback
- **Duration:** 3-10 seconds max
- **Loop:** Should loop seamlessly
- **Tools:** 
  - Windows: ScreenToGif, ShareX
  - Mac: Kap, GIPHY Capture
  - Cross-platform: OBS Studio

### Optimization Tools
- **PNG:** TinyPNG, ImageOptim, PNGGauntlet
- **GIF:** Gifsicle, ezgif.com
- **Batch:** ImageMagick for bulk processing

## 📝 Screenshot Checklist

Before adding screenshots to the repo:

- [ ] All images are properly named according to filenames above
- [ ] Images are optimized (file size < 500KB for PNGs, < 2MB for GIFs)
- [ ] Screenshots show clean, realistic data
- [ ] Text is crisp and readable
- [ ] Colors are accurate (no washed out or oversaturated)
- [ ] No personal information or debug data visible
- [ ] Animated GIFs loop smoothly
- [ ] All referenced images in README.md exist

## 🚀 Quick Screenshot Setup

### Using Compose Preview (Recommended)
1. Open the project in Android Studio / IntelliJ IDEA
2. Navigate to a `@Preview` composable
3. Click "Run Preview"
4. Use built-in screenshot tool
5. Crop and save

### Using Desktop App
1. Run `.\gradlew.bat :composeApp:run`
2. Set up the desired state
3. Use Windows Snipping Tool (Win+Shift+S) or equivalent
4. Save with proper filename

### Using Android Emulator
1. Run app on Android emulator
2. Use emulator's built-in screenshot tool
3. Save and crop as needed

## 📐 Image Dimensions Reference

```
Hero/Banner Images:     1600x900px  (16:9)
Full Screenshots:       1200x800px  (3:2)
Half Screenshots:       800x600px   (4:3)
Theme Previews:         700x500px   (7:5)
Animated GIFs:          1200x800px  (3:2, 5-10s)
Platform Showcase:      1600x900px  (16:9)
```

## 🎬 Example Screenshot Session

1. **Launch app** with Midnight Purple theme
2. **Enter sample data:** 
   - Title: "The Jazz Singer"
   - Commercial Score: 85
   - Seats: 50,000
3. **Screenshot:** Parameters section → `screenshot-parameters.png`
4. **Click Calculate**
5. **Screenshot:** Results section → `screenshot-results.png`
6. **Save 3-4 movies** with different values
7. **Screenshot:** Saved Movies → `screenshot-saved-movies.png`
8. **Record GIF:** Click theme button → circular reveal → `theme-animation.gif`
9. **Switch themes** and repeat for each color scheme
10. **Screenshot:** Full app window → `app-hero.png`

---

**Note:** Once screenshots are added, verify all image links work in README.md by viewing the rendered markdown on GitHub.
