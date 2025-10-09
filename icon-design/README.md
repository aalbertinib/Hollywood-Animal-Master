# Hollywood Animals App Icon

## 🎨 Icon Design
The icon features a Hollywood clapperboard with a paw print, representing the "Hollywood Animals" video game theme.

## 📐 Icon Specifications

### Design Elements:
- **Background**: Dark blue gradient (#1a1a2e to #16213e)
- **Clapperboard**: Gold and black striped top, white bottom
- **Paw Print**: Red/coral color (#ff6b6b) in center

## 🔧 Generate Icons from SVG

### Option 1: Online Tools (Recommended)
Use these free online tools to convert `hollywood-animals-icon.svg` to all required sizes:

1. **For Android & iOS**: https://www.appicon.co/
   - Upload `hollywood-animals-icon.svg`
   - Select Android + iOS
   - Download and extract

2. **Alternative**: https://icon.kitchen/
   - Upload SVG
   - Generate all sizes
   - Download platform-specific packages

### Option 2: Command Line (ImageMagick)
```bash
# Install ImageMagick first
# Then run from icon-design folder:

# Android icons
magick hollywood-animals-icon.svg -resize 48x48 ../composeApp/src/androidMain/res/mipmap-mdpi/ic_launcher.png
magick hollywood-animals-icon.svg -resize 72x72 ../composeApp/src/androidMain/res/mipmap-hdpi/ic_launcher.png
magick hollywood-animals-icon.svg -resize 96x96 ../composeApp/src/androidMain/res/mipmap-xhdpi/ic_launcher.png
magick hollywood-animals-icon.svg -resize 144x144 ../composeApp/src/androidMain/res/mipmap-xxhdpi/ic_launcher.png
magick hollywood-animals-icon.svg -resize 192x192 ../composeApp/src/androidMain/res/mipmap-xxxhdpi/ic_launcher.png

# iOS icon
magick hollywood-animals-icon.svg -resize 1024x1024 ../iosApp/iosApp/Assets.xcassets/AppIcon.appiconset/app-icon-1024.png
```

## 📱 Required Icon Sizes

### Android
- **mdpi**: 48x48px
- **hdpi**: 72x72px
- **xhdpi**: 96x96px
- **xxhdpi**: 144x144px
- **xxxhdpi**: 192x192px

### iOS
- **Universal**: 1024x1024px (single image for all devices)

### Desktop (JVM)
- **Windows**: 256x256px (saved as `app-icon.ico`)
- **macOS**: 1024x1024px (saved as `app-icon.icns`)
- **Linux**: 512x512px (PNG)

### Web
- **favicon.ico**: 32x32px, 16x16px
- **apple-touch-icon.png**: 180x180px
- **manifest icons**: 192x192px, 512x512px

## 📂 File Placement

After generating icons, place them in:

```
composeApp/src/androidMain/res/
├── mipmap-mdpi/ic_launcher.png
├── mipmap-hdpi/ic_launcher.png
├── mipmap-xhdpi/ic_launcher.png
├── mipmap-xxhdpi/ic_launcher.png
└── mipmap-xxxhdpi/ic_launcher.png

iosApp/iosApp/Assets.xcassets/AppIcon.appiconset/
└── app-icon-1024.png

composeApp/src/jvmMain/resources/
├── app-icon.png (256x256)
└── app-icon.ico (Windows)

composeApp/src/webMain/resources/
├── favicon.ico
└── apple-touch-icon.png
```

## 🚀 Quick Start

1. **Go to**: https://www.appicon.co/
2. **Upload**: `hollywood-animals-icon.svg`
3. **Select**: Android + iOS
4. **Download** the generated zip files
5. **Extract** and copy files to respective folders above

That's it! Your app will have professional icons across all platforms.

## 🎨 Customization

To modify the icon design, edit `hollywood-animals-icon.svg` with:
- **Adobe Illustrator**
- **Inkscape** (free)
- **Figma**
- Any SVG editor

### Color Scheme:
- Dark Blue: `#1a1a2e` / `#16213e`
- Gold: `#ffd700` / `#ffaa00`
- Paw Print: `#ff6b6b`
- Clapperboard: `#ffffff` / `#2d2d44`
