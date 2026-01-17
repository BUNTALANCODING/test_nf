package common.map

import arhud.LatLng
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class SensorFix(val lat: Double, val lon: Double, val headingDeg: Float)

expect class SensorProvider {
    fun start(onUpdate: (SensorFix) -> Unit)
    fun stop()
}

@Composable
expect fun rememberSensorProvider(): SensorProvider

@Composable
expect fun CameraPreview(modifier: Modifier = Modifier)


@Composable
expect fun KmpMapView(
    modifier: Modifier = Modifier,
    routePoints: List<LatLng>,
    user: LatLng?,
    followUser: Boolean,
    showUserMarker: Boolean = true,
    userHeadingDeg: Float = 0f
)


