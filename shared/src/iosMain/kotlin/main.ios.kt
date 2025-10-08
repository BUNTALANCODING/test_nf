import androidx.compose.ui.window.ComposeUIViewController
import common.Context
import logger.Logger
import logger.LoggerDefaults
import platform.UIKit.UIViewController
import presentation.App
import kotlin.experimental.ExperimentalNativeApi

//@OptIn(ExperimentalNativeApi::class)
fun mainViewController(
    mapUIViewController: () -> UIViewController,
    latitude: Double,
    longitude: Double,
) = ComposeUIViewController {
    mapViewController = mapUIViewController
    globalLatitude = latitude
    globalLongitude = longitude
    initializeLogger()
    App(Context())
}

lateinit var mapViewController: () -> UIViewController
var globalLatitude: Double = 0.0
var globalLongitude: Double = 0.0

// iOS main function or App delegate
fun initializeLogger() {
    val config = if (Platform.isDebugBinary) {
        LoggerDefaults.debugConfig()
    } else {
        LoggerDefaults.releaseConfig()
    }

    Logger.initialize(config)
    Logger.i("iOS Application started - Debug: ${Platform.isDebugBinary}")
}

// fun mainViewController() = ComposeUIViewController { App(Context()) }

