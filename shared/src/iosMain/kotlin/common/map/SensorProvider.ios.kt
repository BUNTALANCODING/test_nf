package common.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.*
import platform.darwin.NSObject

@Composable
actual fun rememberSensorProvider(): SensorProvider = remember { SensorProvider() }

@OptIn(ExperimentalForeignApi::class)
actual class SensorProvider {
    private val manager = CLLocationManager()
    private var onUpdate: ((SensorFix) -> Unit)? = null

    private var lastLat = 0.0
    private var lastLon = 0.0
    private var lastHeading = 0f

    private val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {

        override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
            val loc = didUpdateLocations.lastOrNull() as? CLLocation ?: return

            loc.coordinate.useContents {
                lastLat = latitude
                lastLon = longitude
            }

            onUpdate?.invoke(SensorFix(lastLat, lastLon, lastHeading))
        }

        override fun locationManager(manager: CLLocationManager, didUpdateHeading: CLHeading) {
            val h = if (didUpdateHeading.trueHeading > 0)
                didUpdateHeading.trueHeading
            else
                didUpdateHeading.magneticHeading

            lastHeading = h.toFloat()
            onUpdate?.invoke(SensorFix(lastLat, lastLon, lastHeading))
        }
    }

    actual fun start(onUpdate: (SensorFix) -> Unit) {
        this.onUpdate = onUpdate
        manager.delegate = delegate

        manager.requestWhenInUseAuthorization()
        manager.desiredAccuracy = kCLLocationAccuracyBest
        manager.distanceFilter = 1.0

        manager.startUpdatingLocation()

        if (CLLocationManager.headingAvailable()) {
            manager.headingFilter = 1.0
            manager.startUpdatingHeading()
        }
    }

    actual fun stop() {
        manager.stopUpdatingLocation()
        manager.stopUpdatingHeading()
        onUpdate = null
    }
}
