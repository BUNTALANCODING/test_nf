import app.net2software.rampcheck.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import app.net2software.rampcheck.configureKotlinAndroid
import com.android.build.api.dsl.LibraryExtension



class SharedConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("composeMultiplatform").get().get().pluginId)
            apply(libs.findPlugin("compose.compiler").get().get().pluginId)
        }

        val composeDeps = extensions.getByType<ComposeExtension>().dependencies
        extensions.configure<LibraryExtension>(::configureKotlinAndroid)

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.apply {

                commonTest {
                    dependencies {
                        implementation(kotlin("test"))
                        implementation(kotlin("test-common"))
                        implementation(kotlin("test-annotations-common"))
                        implementation(libs.findLibrary("kotest.framework.engine").get())
                        implementation(libs.findLibrary("kotest.assertions.core").get())
                        implementation(libs.findLibrary("kotest.property").get())
                        implementation(libs.findLibrary("ktor.mock").get())
                        implementation(libs.findLibrary("coroutines.test").get())
                        implementation(libs.findLibrary("turbine.turbine").get())
                        implementation(libs.findLibrary("mockk.io").get())
                        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                        implementation(composeDeps.uiTest)
                    }
                }

                commonMain {
                    dependencies {
                        implementation(composeDeps.runtime)
                        implementation(composeDeps.foundation)
                        implementation(composeDeps.animation)
                        implementation(composeDeps.material3)
                        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                        implementation(composeDeps.components.resources)
                        api(composeDeps.materialIconsExtended)
                        implementation(composeDeps.components.uiToolingPreview)
                        implementation(libs.findLibrary("ktor.core").get())
                        implementation(libs.findLibrary("ktor.logging").get())
                        implementation(libs.findLibrary("ktor.serialization").get())
                        implementation(libs.findLibrary("ktor.negotiation").get())
                        implementation(libs.findLibrary("kotlinx.serialization.json").get())
                        implementation(libs.findLibrary("kotlinx.datetime").get())
                        implementation(libs.findLibrary("kotlinx.coroutines.core").get())
                        implementation(libs.findLibrary("compose.navigation").get())
                        implementation(libs.findLibrary("compose-cupertino").get())
                        implementation(libs.findLibrary("kmp-date-time-picker").get())
//                        implementation(libs.findLibrary("qr-kit").get())
                        implementation(libs.findLibrary("androidx.lifecycle.viewmodel.compose").get())
                        implementation("am.highapps.parallaxtoolbar:compose-parallax-toolbar-kmp:1.1.0")
                        api(libs.findLibrary("koin.core").get())
                        api(libs.findLibrary("koin.compose").get())
                        api(libs.findLibrary("koin.compose.viewmodel").get())
                        api(libs.findLibrary("coil3").get())
                        api("io.github.mirzemehdi:kmpnotifier:1.5.1")
                        implementation("tech.kotlinlang:permission:0.13.0")
                        implementation("com.github.skydoves:landscapist-coil3:2.3.0")
                        implementation("be.digitalia.compose.htmlconverter:htmlconverter:1.1.0")
                        implementation("io.github.bvantur:inspektify-ktor2:1.0.0-beta09")
                        api("dev.icerock.moko:permissions:0.19.1")
                        implementation("dev.icerock.moko:mvvm-core:0.16.1")
                        implementation("dev.icerock.moko:mvvm-compose:0.16.1")
                        implementation("dev.icerock.moko:permissions-camera:0.19.1")
                        implementation("dev.icerock.moko:permissions-gallery:0.19.1")
                        implementation("dev.icerock.moko:permissions-location:0.19.1")
                        implementation("dev.icerock.moko:permissions-notifications:0.19.1")
                        implementation("dev.icerock.moko:permissions-storage:0.19.1")
                        implementation("io.github.vinceglb:filekit-compose:0.8.7")
                        api("dev.icerock.moko:permissions-compose:0.19.1")
                        implementation(libs.findLibrary("camerak").get())
                        implementation(libs.findLibrary("camerak-image-saver").get())
                        implementation("com.squareup.okio:okio:3.9.0")
                        implementation("io.insert-koin:koin-core:3.5.3")
                        implementation("io.coil-kt.coil3:coil-compose:3.0.0")
                        implementation("io.coil-kt.coil3:coil:3.0.0")
                        implementation("org.jetbrains.compose.ui:ui-graphics:1.6.10") // atau versi Compose kamu


                    }
                }

                androidMain {
                    dependencies {
                        api(libs.findLibrary("androidx.activity.compose").get())
                        api(libs.findLibrary("androidx.appcompat").get())
                        api(libs.findLibrary("androidx.core").get())
                        implementation(composeDeps.preview)
                        implementation(libs.findLibrary("koin.android").get())
                        implementation(libs.findLibrary("ktor.okhttp").get())
                        implementation(libs.findLibrary("system.ui.controller").get())
                        implementation(libs.findLibrary("accompanist.permissions").get())
                        implementation(libs.findLibrary("androidx.datastore.preferences").get())
                        implementation(libs.findLibrary("maps.compose").get())
                        implementation(libs.findBundle("play.services").get())

                        api(libs.findLibrary("coil3.gif").get())
                        api(libs.findLibrary("coil3.svg").get())
                        api(libs.findLibrary("coil3.core").get())
                        api(libs.findLibrary("coil3.video").get())

                        implementation(project.dependencies.platform("com.google.firebase:firebase-bom:34.7.0"))
                        implementation("com.google.firebase:firebase-auth")
                        implementation("com.google.firebase:firebase-crashlytics-ktx:18.6.0")

                        implementation("androidx.camera:camera-core:1.4.0")
                        implementation("androidx.camera:camera-camera2:1.4.0")
                        implementation("androidx.camera:camera-lifecycle:1.4.0")
                        implementation("androidx.camera:camera-view:1.4.0")
                        implementation("androidx.camera:camera-video:1.4.0")

                        implementation("androidx.work:work-runtime-ktx:2.9.0")
                        implementation("io.insert-koin:koin-androidx-workmanager:4.0.0")
                        implementation("io.ktor:ktor-client-cio:2.3.12")
                        implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")
                        implementation("org.osmdroid:osmdroid-android:6.1.20")
//                        implementation("com.google.guava:guava:32.1.2-android")
                        implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.0")




                    }
                }

                iosMain {
                    dependencies {
                        implementation(libs.findLibrary("ktor.darwin.ios").get())
                        implementation(libs.findLibrary("ktor.ios").get())
                        implementation("io.insert-koin:koin-core:3.5.3")
                    }
                }
            }
        }
    }
}
