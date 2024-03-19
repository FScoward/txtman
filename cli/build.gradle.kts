import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    kotlin("multiplatform") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.23"
}

group = "me.fscoward"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosArm64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "com.github.fscoward.txtman.cli.main"
            }
        }
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting
        val nativeMain by getting
        val nativeTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        nativeMain.dependencies {
            implementation("com.github.ajalt.clikt:clikt:4.2.2")
            implementation("com.aallam.ulid:ulid-kotlin:1.3.0")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0-RC.2")
            implementation("io.arrow-kt:arrow-core:1.2.1")
            implementation("io.arrow-kt:arrow-fx-coroutines:1.2.1")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
            implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.3.1")
            implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")
        }
    }
}
