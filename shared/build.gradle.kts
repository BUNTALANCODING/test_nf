import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN

plugins {
    alias(libs.plugins.rampcheck.kotlinMultiplatform)
    alias(libs.plugins.rampcheck.shared)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.google.services)
    alias(libs.plugins.build.konfig)
    kotlin("native.cocoapods")
}

ktlint {
    android = true
    outputToConsole = true
    outputColorName = "RED"
}

kotlin {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            //export(project("io.github.mirzemehdi:kmpnotifier:1.5.1"))
            baseName = "shared"
            isStatic = true
        }
    }

    cocoapods {
        summary = "Shared module for iOS (Firebase Auth + Google Sign-In)"
        homepage = "https://example.com"
        version = "1.0.0" // ✅ tambahkan baris ini
        ios.deploymentTarget = "14.0"

        framework {
            baseName = "shared"
            isStatic = true
        }

        pod("FirebaseCore", "~> 10.23.0")
        pod("FirebaseAuth", "~> 10.23.0")
        pod("GoogleSignIn", "~> 7.0.0")

    }

    sourceSets {
        commonTest {
            dependencies {

            }
        }
        commonMain {
            dependencies {
            }
        }

        androidMain {
            dependencies {

            }
        }
        iosMain {
            dependencies {

            }
        }
    }
}

buildkonfig {
    packageName = "com.jaring.app_buss_jaring"

    defaultConfigs("prod") {
        buildConfigField(STRING, "version", "1.3.1")
        buildConfigField(BOOLEAN, "debug", "false")
        buildConfigField(STRING, "base_url", "")
    }

    defaultConfigs("dev") {
        buildConfigField(STRING, "version", "2.3.1-DEV")
        buildConfigField(BOOLEAN, "debug", "true")
        buildConfigField(STRING, "base_url", "https://tracking-bus.net2software.net/api/v1/")
    }

    targetConfigs("prod") {
        defaultConfigs {
            buildConfigField(BOOLEAN, "debug", "false")
            buildConfigField(STRING, "base_url", "")
        }
    }

    targetConfigs("dev") {
        defaultConfigs {
            buildConfigField(BOOLEAN, "debug", "true")
            buildConfigField(STRING, "base_url", "https://tracking-bus.net2software.net/api/v1/")
        }
    }
}


