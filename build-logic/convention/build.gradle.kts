plugins {
    `kotlin-dsl`
}

group = "com.nf.test.buildlogic"

dependencies {
    compileOnly(libs.plugins.kotlin.serialization.toDep())
    compileOnly(libs.plugins.androidApplication.toDep())
    compileOnly(libs.plugins.androidLibrary.toDep())
    compileOnly(libs.plugins.composeMultiplatform.toDep())
    compileOnly(libs.plugins.kotlinMultiplatform.toDep())
    compileOnly(libs.plugins.compose.compiler.toDep())
}

fun Provider<PluginDependency>.toDep() = map {
    "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("kotlinMultiplatform") {
            id = "com.nf.test.kotlinMultiplatform"
            implementationClass = "KotlinMultiplatformConventionPlugin"
        }
        register("shared") {
            id = "com.nf.test.shared"
            implementationClass = "SharedConventionPlugin"
        }
        register("androidApp") {
            id = "com.nf.test.androidApp"
            implementationClass = "AndroidAppConventionPlugin"
        }
    }
}