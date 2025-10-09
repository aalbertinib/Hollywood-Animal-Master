import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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
        }
    }
    
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    
    js {
        browser()
        binaries.executable()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    
    sourceSets {
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
        webMain.dependencies {
            implementation(libs.kotlinx.browser)
        }
    }
}

android {
    namespace = "org.aalbertini.ham"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.aalbertini.ham"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
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
            packageName = "org.aalbertini.ham"
            packageVersion = "1.0.0"
        }
    }
}

// Custom tasks to print output paths after building
abstract class PrintOutputPathsTask : DefaultTask() {
    @get:InputDirectory
    @get:Optional
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
    @get:InputDirectory
    @get:Optional
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
