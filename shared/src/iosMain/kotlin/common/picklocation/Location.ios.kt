package common.picklocation

import androidx.compose.runtime.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.*
import platform.Foundation.NSError
import platform.darwin.NSObject
import presentation.ui.main.inforute.view_model.GeoPoint

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberUserLocation(): State<GeoPoint?> {
    val locState = remember { mutableStateOf<GeoPoint?>(null) }

    DisposableEffect(Unit) {
        val manager = CLLocationManager()

        val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
            override fun locationManager(
                manager: CLLocationManager,
                didUpdateLocations: List<*>
            ) {
                val loc = didUpdateLocations.lastOrNull() as? CLLocation ?: return

                val point = loc.coordinate.useContents {
                    GeoPoint(latitude, longitude)
                }
                locState.value = point
            }

            override fun locationManager(
                manager: CLLocationManager,
                didFailWithError: NSError
            ) {
            }
        }

        manager.delegate = delegate
        manager.desiredAccuracy = kCLLocationAccuracyBest
        manager.requestWhenInUseAuthorization()
        manager.startUpdatingLocation()

        onDispose {
            manager.stopUpdatingLocation()
            manager.delegate = null
        }
    }

    return locState
}
