import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

// The Android application. AGP 9 no longer lets a Kotlin Multiplatform module be an Android app,
// so this module holds the entry point (MainActivity, manifest, launcher icons) and depends on
// :composeApp for everything else. Kotlin is built into AGP 9: no kotlin-android plugin.
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

// Version management - read from gradle.properties
val appVersion = project.findProperty("project.version") as String? ?: "1.0.0"
val versionParts = appVersion.split(".")
val appVersionCode = versionParts[0].toInt() * 10000 +
                     (versionParts.getOrNull(1)?.toInt() ?: 0) * 100 +
                     (versionParts.getOrNull(2)?.toInt() ?: 0)

android {
    namespace = "org.aalbertini.ham"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    // Load keystore properties from file or environment variables
    val keystorePropertiesFile = rootProject.file("keystore.properties")
    val keystoreProperties = Properties()

    val useKeystoreConfig = if (keystorePropertiesFile.exists()) {
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))
        true
    } else {
        // Try to load from environment variables (for CI/CD)
        System.getenv("KEYSTORE_FILE")?.let { keystoreProperties["storeFile"] = it }
        System.getenv("KEYSTORE_PASSWORD")?.let { keystoreProperties["storePassword"] = it }
        System.getenv("KEY_ALIAS")?.let { keystoreProperties["keyAlias"] = it }
        System.getenv("KEY_PASSWORD")?.let { keystoreProperties["keyPassword"] = it }

        keystoreProperties.containsKey("storeFile") &&
        keystoreProperties.containsKey("storePassword")
    }

    signingConfigs {
        if (useKeystoreConfig) {
            create("release") {
                // Relative to the project root, as keystore.properties.template documents.
                storeFile = rootProject.file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            }
        }
    }

    defaultConfig {
        applicationId = "org.aalbertini.ham"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = appVersionCode
        versionName = appVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            if (useKeystoreConfig) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(projects.composeApp)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
}

// Task to print the Android version (the iOS build number uses the same formula)
tasks.register("printVersion") {
    group = "versioning"
    description = "Prints the Android version name and code"

    doLast {
        println("Android Version Name: $appVersion")
        println("Android Version Code: $appVersionCode")
    }
}

// Print where the APKs are after assembling, like :composeApp does for desktop and web packages
val isCI = System.getenv("CI") == "true" || System.getenv("GITHUB_ACTIONS") == "true"
val apkDirectory = layout.buildDirectory.dir("outputs/apk")
tasks.register("printAndroidPaths") {
    onlyIf { !isCI && apkDirectory.get().asFile.exists() }
    doLast {
        val outputDir = apkDirectory.get().asFile
        println("\n${"=".repeat(80)}")
        println("BUILD OUTPUT LOCATION - ANDROID")
        println("=".repeat(80))
        outputDir.walkTopDown().filter { it.isFile && it.extension == "apk" }.forEach { apk ->
            println("  ✓ ${apk.name}")
            println("    Path: ${apk.absolutePath}")
        }
        println("=".repeat(80) + "\n")
    }
}
tasks.matching { it.name == "assembleDebug" || it.name == "assembleRelease" }.configureEach {
    finalizedBy("printAndroidPaths")
}
