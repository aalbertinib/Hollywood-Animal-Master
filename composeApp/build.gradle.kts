import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    jvmToolchain(21)
    
    // AGP 9: a multiplatform module is an Android *library*; the app itself lives in :androidApp.
    android {
        namespace = "org.aalbertini.ham.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        // Compose Multiplatform resources (strings, drawables) are packaged as Android resources.
        androidResources {
            enable = true
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
            testTask {
                useKarma {
                    useChromeHeadless()
                    useFirefox()
                }
            }
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
            testTask {
                useKarma {
                    useChromeHeadless()
                    useFirefox()
                }
            }
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer =
                    (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                        // Suppress Node.js deprecation warnings
                        open = false
                        // Serve sources to debug inside browser
                        static(project.rootDir.path)
                        static(project.projectDir.path)
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
        
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.animation)
            implementation(libs.compose.material3)
            implementation(libs.compose.material3.navigationSuite)
            implementation(libs.compose.material.iconsExtended)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.materialkolor)
            implementation(libs.compose.adaptive)
            implementation(libs.navigationevent.compose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutinesTest)
        }
        // Compose UI tests run on the desktop JVM only (no emulator or browser needed).
        getByName("jvmTest").dependencies {
            implementation(libs.compose.ui.test)
            implementation(compose.desktop.currentOs)
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
val appBuildNumber = appVersion.split(".").let { parts ->
    parts[0].toInt() * 10000 + (parts.getOrNull(1)?.toInt() ?: 0) * 100 + (parts.getOrNull(2)?.toInt() ?: 0)
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
        println("Android: see ./gradlew :androidApp:printVersion")
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
            
            // Update CURRENT_PROJECT_VERSION (build number, same formula as the Android versionCode)
            content = content.replace(
                Regex("CURRENT_PROJECT_VERSION=.*"),
                "CURRENT_PROJECT_VERSION=$appBuildNumber"
            )
            
            iosConfigFile.writeText(content)
            println("✅ iOS version synced: $appVersion (build $appBuildNumber)")
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
    rootProject.the<YarnRootExtension>().yarnLockAutoReplaceProperty.set(true)
}