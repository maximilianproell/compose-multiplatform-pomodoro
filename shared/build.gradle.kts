plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.moko.resources)
}

kotlin {
    android()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(compose.animation)
                implementation(compose.materialIconsExtended)

                // Navigation with voyager
                implementation(libs.voyager.navigator)

                // Moko resources
                implementation(libs.moko.resources)
                implementation(libs.moko.resources.compose)

                // Kotlinx Datetime
                implementation(libs.kotlinx.datetime)

                // Kermit multiplatform logger
                implementation(libs.touchlab.kermit)
            }
        }
        val androidMain by getting {
            dependencies {
                dependsOn(commonMain)
                api(libs.androidx.activity.compose)
                api(libs.androidx.appcompat)
                api(libs.androidx.core.ktx)

                implementation(libs.sqldelight.android.driver)

                implementation(compose.preview)
                implementation(compose.uiTooling)
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation(libs.sqldelight.native.driver)
            }
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.compose.multiplatform.pomodoro.common"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        compose = true
    }
    val composeVersion = extra["compose.version"] as String
    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.compose.multiplatform.pomodoro"
}

sqldelight {
    databases {
        create("PomodoroDatabase") {
            packageName.set("com.compose.multiplatform.pomodoro.storage")
        }
    }
}
