import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    jvmToolchain(21)
    
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            // Export necessary dependencies for Swift interop
            export(libs.androidx.lifecycle.viewmodelCompose)
        }
    }
    
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    
    js {
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = devServer?.copy(
                    open = false
                )
            }
        }
        binaries.executable()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer =
                    (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                        // Suppress Node.js deprecation warnings
                        open = false
                        static =
                            (static ?: mutableListOf()).apply {
                                // Serve sources to debug inside browser
                                add(project.rootDir.path)
                                add(project.projectDir.path)
                            }
                    }
                sourceMaps = true
            }
        }
        binaries.executable()
    }
    
    // Apply default hierarchy template to enable webMain source set for JS and WASM-JS targets
    // This automatically creates webMain as a parent of jsMain and wasmJsMain
    applyDefaultHierarchyTemplate()
    
    sourceSets {
        // Kotlin 2.2.20+ automatically provides webMain as parent of jsMain and wasmJsMain
        // via the default hierarchy template. No manual dependsOn() calls needed.
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.material3.windowSizeClass)
            implementation(libs.koin.android)
            implementation(libs.koin.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.animation)
            implementation(compose.material3)
            implementation(compose.material3AdaptiveNavigationSuite)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
        
        // Web platform dependencies (shared by JS and WasmJS)
        // Access webMain through getByName since it's automatically created by default hierarchy
        getByName("webMain").dependencies {
            implementation(libs.kotlinx.browser)
        }
    }
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
                storeFile = file(keystoreProperties["storeFile"] as String)
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

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "org.aalbertini.ham.MainKt"
        
        // jpackage requires JDK 17+ with jpackage.exe included
        // Try multiple fallbacks to find a suitable JDK
        javaHome = System.getenv("COMPOSE_DESKTOP_JAVA_HOME") 
            ?: runCatching {
                javaToolchains.launcherFor {
                    languageVersion.set(JavaLanguageVersion.of(21))
                    vendor.set(JvmVendorSpec.ORACLE)
                }.get().metadata.installationPath.asFile.absolutePath
            }.getOrElse {
                runCatching {
                    javaToolchains.launcherFor {
                        languageVersion.set(JavaLanguageVersion.of(21))
                    }.get().metadata.installationPath.asFile.absolutePath
            }.getOrElse {
                System.getenv("JAVA_HOME")
            }
        }

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "HollywoodAnimalMaster"
            packageVersion = appVersion

            // Installer icons per platform (guarded to avoid failures if not generated yet)
            windows {
                val ico = project.file("src/jvmMain/resources/app-icon.ico")
                if (ico.exists()) {
                    iconFile.set(ico)
                }
            }
            linux {
                val png = project.file("src/jvmMain/resources/app-icon.png")
                if (png.exists()) {
                    iconFile.set(png)
                }
            }
            macOS {
                val icns = project.file("src/jvmMain/resources/app-icon.icns")
                if (icns.exists()) {
                    iconFile.set(icns)
                }
            }
        }
    }
}
// Custom tasks to print output paths after building
// Note: outputDirectory uses @Internal instead of @InputDirectory to avoid
// Gradle validation failures when directory doesn't exist yet (e.g., in CI)
abstract class PrintOutputPathsTask : DefaultTask() {
    @get:Internal
    abstract val outputDirectory: DirectoryProperty
    
    @get:Input
    abstract val taskTitle: Property<String>
    
    @get:Input
    abstract val fileExtensions: ListProperty<String>
    
    @TaskAction
    fun printPaths() {
        val outputDir = outputDirectory.get().asFile
        println("\n${"=".repeat(80)}")
        println(taskTitle.get())
        println("=".repeat(80))
        println("Output directory: ${outputDir.absolutePath}")
        
        if (outputDir.exists()) {
            outputDir.walkTopDown().maxDepth(2).forEach { file ->
                if (file.isFile && (file.extension in fileExtensions.get())) {
                    println("  ✓ ${file.name}")
                    println("    Path: ${file.absolutePath}")
                }
            }
        } else {
            println("  (Directory will be created during build)")
        }
        println("=".repeat(80) + "\n")
    }
}

tasks.register<PrintOutputPathsTask>("printPackagePaths") {
    outputDirectory.set(layout.buildDirectory.dir("compose/binaries"))
    taskTitle.set("BUILD OUTPUT LOCATION - DESKTOP")
    fileExtensions.set(listOf("msi", "exe", "dmg", "deb", "rpm", "pkg"))
}

tasks.register<PrintOutputPathsTask>("printAndroidPaths") {
    outputDirectory.set(layout.buildDirectory.dir("outputs/apk"))
    taskTitle.set("BUILD OUTPUT LOCATION - ANDROID")
    fileExtensions.set(listOf("apk"))
}

abstract class PrintWebPathsTask : DefaultTask() {
    @get:Internal
    abstract val outputDirectory: DirectoryProperty
    
    @TaskAction
    fun printPaths() {
        val wasmDir = outputDirectory.get().asFile
        println("\n${"=".repeat(80)}")
        println("BUILD OUTPUT LOCATION - WEB (WASM)")
        println("=".repeat(80))
        println("Output directory: ${wasmDir.absolutePath}")
        
        if (wasmDir.exists()) {
            println("  ✓ index.html")
            println("    Path: ${wasmDir.absolutePath}/index.html")
            wasmDir.listFiles()?.filter { it.extension in listOf("wasm", "js") }?.forEach { file ->
                println("  ✓ ${file.name}")
            }
        } else {
            println("  (Directory will be created during build)")
        }
        println("=".repeat(80) + "\n")
    }
}

tasks.register<PrintWebPathsTask>("printWebPaths") {
    outputDirectory.set(layout.buildDirectory.dir("dist/wasmJs/productionExecutable"))
}

tasks.register<PrintOutputPathsTask>("printIosPaths") {
    outputDirectory.set(layout.buildDirectory.dir("bin/iosSimulatorArm64/debugFramework"))
    taskTitle.set("BUILD OUTPUT LOCATION - iOS FRAMEWORK")
    fileExtensions.set(listOf("framework"))
}

// Attach output path printing to package tasks
tasks.matching { it.name.startsWith("package") && it.name != "packageDistributionForCurrentOS" }.configureEach {
    finalizedBy("printPackagePaths")
}

tasks.matching { it.name.contains("assembleDebug") || it.name.contains("assembleRelease") }.configureEach {
    finalizedBy("printAndroidPaths")
}

tasks.matching { it.name.contains("wasmJsBrowserDistribution") }.configureEach {
    finalizedBy("printWebPaths")
}

tasks.matching { it.name.contains("linkDebugFramework") }.configureEach {
    finalizedBy("printIosPaths")
}

// Task to export version for CI/CD
tasks.register("exportVersion") {
    group = "versioning"
    description = "Exports project version to a file for CI/CD consumption"
    
    doLast {
        val versionFile = file("${project.rootDir}/build/version.txt")
        versionFile.parentFile.mkdirs()
        versionFile.writeText(appVersion)
        println("Version exported: $appVersion")
        println("Version file: ${versionFile.absolutePath}")
    }
}

// Task to print current version
tasks.register("printVersion") {
    group = "versioning"
    description = "Prints the current project version"
    
    doLast {
        println("=".repeat(60))
        println("Project Version Information")
        println("=".repeat(60))
        println("Version: $appVersion")
        println("Android Version Code: $appVersionCode")
        println("Android Version Name: $appVersion")
        println("Desktop Package Version: $appVersion")
        println("iOS Marketing Version: $appVersion")
        println("=".repeat(60))
    }
}

// Task to sync version to iOS configuration
tasks.register("syncVersionToIOS") {
    group = "versioning"
    description = "Syncs version from gradle.properties to iOS Config.xcconfig"
    
    doLast {
        val iosConfigFile = file("${project.rootDir}/iosApp/Configuration/Config.xcconfig")
        if (iosConfigFile.exists()) {
            var content = iosConfigFile.readText()
            
            // Update MARKETING_VERSION
            content = content.replace(
                Regex("MARKETING_VERSION=.*"),
                "MARKETING_VERSION=$appVersion"
            )
            
            // Update CURRENT_PROJECT_VERSION (build number from versionCode)
            content = content.replace(
                Regex("CURRENT_PROJECT_VERSION=.*"),
                "CURRENT_PROJECT_VERSION=$appVersionCode"
            )
            
            iosConfigFile.writeText(content)
            println("✅ iOS version synced: $appVersion (build $appVersionCode)")
        } else {
            println("⚠️  iOS Config.xcconfig not found, skipping sync")
        }
    }
}

// Configure print tasks to only run locally (not in CI) when their directories exist
val isCI = System.getenv("CI") == "true" || System.getenv("GITHUB_ACTIONS") == "true"

tasks.named("printPackagePaths") {
    onlyIf { 
        !isCI && (this as PrintOutputPathsTask).outputDirectory.get().asFile.exists()
    }
}

tasks.named("printAndroidPaths") {
    onlyIf { 
        !isCI && (this as PrintOutputPathsTask).outputDirectory.get().asFile.exists()
    }
}

tasks.named("printWebPaths") {
    onlyIf { 
        !isCI && (this as PrintWebPathsTask).outputDirectory.get().asFile.exists()
    }
}

tasks.named("printIosPaths") {
    onlyIf { 
        !isCI && (this as PrintOutputPathsTask).outputDirectory.get().asFile.exists()
    }
}

rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin> {
    rootProject.the<YarnRootExtension>().yarnLockAutoReplace = true
}