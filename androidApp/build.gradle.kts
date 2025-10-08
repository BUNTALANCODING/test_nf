import com.android.build.gradle.internal.api.ApkVariantOutputImpl
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.rampcheck.androidApp)
//    alias(libs.plugins.google.service)
//    alias(libs.plugins.firebase.service)
}
kotlin {
    androidTarget()
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant {
            sourceSetTree.set(KotlinSourceSetTree.test)

            dependencies {
                //implementation("io.github.vinceglb:filekit-core:0.10.0-beta04")
                /*implementation("androidx.compose.ui:ui-test-junit4-android:1.6.4")
                debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.4")*/
            }
        }
    }
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":shared"))
                /*implementation("androidx.compose.material:material-ripple:1.7.0-alpha05")*/
            }
        }
    }
}
android {
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    namespace = "app.net2software.rampcheck.android"
    defaultConfig {
        applicationId = "app.app.net2software.rampcheck.android"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.android.version.code.get().toInt()
        versionName = libs.versions.android.version.name.get()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("boolean", "ENABLE_LOGGING", "true")
            isDebuggable = true
        }
        release {
            buildConfigField("boolean", "ENABLE_LOGGING", "false")
            isDebuggable = false
            isMinifyEnabled = false
            isShrinkResources = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        buildConfig = true
    }
    applicationVariants.configureEach {
        outputs.configureEach {
            (this as? ApkVariantOutputImpl)?.outputFileName =
                //"${rootProject.name}_${versionName}(${versionCode})_${buildType.name}.apk"
                "rampcheck_android_${buildType.name}.apk"
        }

        // rename the output AAB file
        tasks.named(
            "sign${flavorName.uppercaseFirstChar()}${buildType.name.uppercaseFirstChar()}Bundle",
            com.android.build.gradle.internal.tasks.FinalizeBundleTask::class.java
        ) {
            val file = finalBundleFile.asFile.get()
            val finalFile =
                File(
                    file.parentFile,
                    "${rootProject.name}_$versionName($versionCode)_${buildType.name}.aab"
                )
            finalBundleFile.set(finalFile)
        }
    }
}

