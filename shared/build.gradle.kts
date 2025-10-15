import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN

plugins {
    alias(libs.plugins.rampcheck.kotlinMultiplatform)
    alias(libs.plugins.rampcheck.shared)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.google.service)
    alias(libs.plugins.build.konfig)

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
    packageName = "app.net2software.rampcheck"

    defaultConfigs("prod") {
        buildConfigField(STRING, "version", "1.3.1")
        buildConfigField(BOOLEAN, "debug", "false")
        buildConfigField(STRING, "base_url", "")
    }

    defaultConfigs("dev") {
        buildConfigField(STRING, "version", "2.3.1-DEV")
        buildConfigField(BOOLEAN, "debug", "true")
        buildConfigField(STRING, "base_url", "https://dashboard-ramcek.net2software.net/api/v1/frontend/")
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
            buildConfigField(STRING, "base_url", "https://dashboard-ramcek.net2software.net/api/v1/frontend/")
        }
    }
}


