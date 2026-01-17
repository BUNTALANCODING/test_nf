package common.picklocation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint as OsmGeoPoint
import org.osmdroid.views.MapView
import presentation.ui.main.inforute.view_model.GeoPoint

@Composable
actual fun PlatformPickLocationMap(
    initial: GeoPoint,
    onCameraMoved: (GeoPoint) -> Unit,
    modifier: Modifier,
    focusLocation: GeoPoint?,
    initialZoom: Double,
    focusZoom: Double
) {
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle()
    var didInit by remember { mutableStateOf(false) }

    val latestOnCameraMoved by rememberUpdatedState(onCameraMoved)
    val latestFocus by rememberUpdatedState(focusLocation)

    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName
    }

    AndroidView(
        modifier = modifier,
        factory = {
            mapView.apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)

                controller.setZoom(initialZoom)
                controller.setCenter(OsmGeoPoint(initial.lat, initial.lng))

                addMapListener(object : MapListener {
                    override fun onScroll(event: ScrollEvent?): Boolean {
                        val c = mapCenter
                        latestOnCameraMoved(GeoPoint(c.latitude, c.longitude))
                        return false
                    }

                    override fun onZoom(event: ZoomEvent?): Boolean {
                        val c = mapCenter
                        latestOnCameraMoved(GeoPoint(c.latitude, c.longitude))
                        return false
                    }
                })
            }
        },
        update = { view ->
            if (!didInit) {
                view.controller.setZoom(initialZoom)
                view.controller.setCenter(OsmGeoPoint(initial.lat, initial.lng))
                didInit = true
            }

            latestFocus?.let { target ->
                view.controller.setZoom(focusZoom)
                view.controller.animateTo(OsmGeoPoint(target.lat, target.lng))
            }
        }
    )
}

@Composable
private fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(mapView, lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_DESTROY -> mapView.onDetach()
                else -> Unit
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }
    return mapView
}
