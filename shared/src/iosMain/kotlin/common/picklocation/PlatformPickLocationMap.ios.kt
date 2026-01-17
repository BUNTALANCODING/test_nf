package common.picklocation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.delay
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.*
import platform.darwin.NSObject
import presentation.ui.main.inforute.view_model.GeoPoint
import kotlin.math.pow

private fun zoomToSpan(zoom: Double): Double {
    val baseZoom = 16.0
    val baseSpan = 0.01
    val scale = 2.0.pow(zoom - baseZoom)
    return (baseSpan / scale).coerceIn(0.0008, 0.5)
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PlatformPickLocationMap(
    initial: GeoPoint,
    onCameraMoved: (GeoPoint) -> Unit,
    modifier: Modifier,
    focusLocation: GeoPoint?,
    initialZoom: Double,
    focusZoom: Double
) {
    var didSetInitial by remember { mutableStateOf(false) }
    var isAutoFocusing by remember { mutableStateOf(false) }

    val mapRef = remember { mutableStateOf<MKMapView?>(null) }
    val latestOnCameraMoved by rememberUpdatedState(onCameraMoved)

    val mapDelegate = remember {
        object : NSObject(), MKMapViewDelegateProtocol {
            override fun mapView(mapView: MKMapView, regionDidChangeAnimated: Boolean) {
                if (!isAutoFocusing) {
                    mapView.centerCoordinate.useContents {
                        latestOnCameraMoved(GeoPoint(latitude, longitude))
                    }
                }
            }
        }
    }

    LaunchedEffect(focusLocation) {
        val mapView = mapRef.value ?: return@LaunchedEffect
        val target = focusLocation ?: return@LaunchedEffect

        isAutoFocusing = true
        val span = zoomToSpan(focusZoom)
        val region = MKCoordinateRegionMake(
            CLLocationCoordinate2DMake(target.lat, target.lng),
            MKCoordinateSpanMake(span, span)
        )
        mapView.setRegion(region, true)
        delay(800)
        isAutoFocusing = false
    }

    UIKitView(
        modifier = modifier,
        factory = {
            val mapView = MKMapView().apply {
                val span = zoomToSpan(initialZoom)
                val region = MKCoordinateRegionMake(
                    CLLocationCoordinate2DMake(initial.lat, initial.lng),
                    MKCoordinateSpanMake(span, span)
                )
                setRegion(region, false)

                this.delegate = mapDelegate
                mapRef.value = this

                rotateEnabled = false
                pitchEnabled = false
                showsCompass = false
                showsScale = false
            }
            mapView
        },
        update = { mapView ->
            mapRef.value = mapView
            if (!didSetInitial) {
                val span = zoomToSpan(initialZoom)
                val region = MKCoordinateRegionMake(
                    CLLocationCoordinate2DMake(initial.lat, initial.lng),
                    MKCoordinateSpanMake(span, span)
                )
                mapView.setRegion(region, false)
                didSetInitial = true
            }
        }
    )
}
