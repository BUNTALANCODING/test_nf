package common.map

import android.graphics.Color as AColor
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.ViewConfiguration
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint as OsmGeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import presentation.ui.main.inforute.view_model.BusMapUi
import presentation.ui.main.inforute.view_model.GeoPoint
import presentation.ui.main.inforute.view_model.StopMapUi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import navbuss.shared.generated.resources.Res
import navbuss.shared.generated.resources.ic_buss
import navbuss.shared.generated.resources.ic_stop_marker
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

@Composable
actual fun PlatformRouteMap(
    polyline: List<GeoPoint>,
    stops: List<StopMapUi>,
    buses: List<BusMapUi>,
    selectedBusId: String?,
    onBusClick: (String) -> Unit,
    onMapClick: () -> Unit,
    onBusAnchorChanged: (Offset) -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle()
    val density = LocalDensity.current

    val latestOnBusClick by rememberUpdatedState(onBusClick)
    val latestOnMapClick by rememberUpdatedState(onMapClick)
    val latestOnBusAnchorChanged by rememberUpdatedState(onBusAnchorChanged)
    val latestSelectedId by rememberUpdatedState(selectedBusId)

    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName
    }

    var didCenter by remember { mutableStateOf(false) }
    var lastMarkerClickTs by remember { mutableStateOf(0L) }

    var zoomLevel by remember { mutableStateOf(16.0) }
    val updateZoom by rememberUpdatedState<(Double) -> Unit> { z -> zoomLevel = z }

    val baseZoom = 16.0
    val baseBusDp = 30f
    val busSizeDp: Dp = remember(zoomLevel) {
        val diff = zoomLevel - baseZoom
        val scale = (1.12).pow(diff).coerceIn(1.0, 2.0)
        (baseBusDp * scale.toFloat()).dp
    }

    val routeOutline = remember { Polyline() }
    val routeInner = remember { Polyline() }

    val stopMarkers = remember { mutableMapOf<String, Marker>() }
    val busMarkers = remember { mutableMapOf<String, Marker>() }

    val lastBusPos = remember { mutableMapOf<String, GeoPoint>() }
    val lastBusHeading = remember { mutableMapOf<String, Float>() }

    val busIcon = rememberMarkerDrawable(Res.drawable.ic_buss, size = busSizeDp)
    val stopIcon = rememberMarkerDrawable(Res.drawable.ic_stop_marker, size = 26.dp)

    val BUS_ICON_HEADING_OFFSET_DEG = 0f

    val BUS_ANCHOR_V = 0.7f
    val POINTER_TIP_GAP_DP = 2.dp

    fun drawableHeightPx(d: Drawable?): Int {
        if (d == null) return 0
        val bh = d.bounds.height()
        return when {
            bh > 0 -> bh
            d.intrinsicHeight > 0 -> d.intrinsicHeight
            else -> 0
        }
    }

    fun computeBusAnchorPx(marker: Marker): Offset {
        val pt = Point()
        mapView.projection.toPixels(marker.position, pt)

        val iconH = drawableHeightPx(marker.icon).takeIf { it > 0 } ?: 0
        val iconTopY = pt.y - (iconH * BUS_ANCHOR_V)

        val gapPx = with(density) { POINTER_TIP_GAP_DP.toPx() }
        val tipX = pt.x.toFloat()
        val tipY = (iconTopY + gapPx).toFloat()

        return Offset(tipX, tipY)
    }

    fun pushAnchorForBusId(busId: String) {
        val m = busMarkers[busId] ?: return
        mapView.post {
            latestOnBusAnchorChanged(computeBusAnchorPx(m))
        }
    }

    fun lerpAngleDeg(from: Float, to: Float, t: Float): Float {
        val delta = ((to - from + 540f) % 360f) - 180f
        return (from + delta * t + 360f) % 360f
    }

    fun headingToMarkerRotNoMap(headingDeg: Float): Float {
        return (360f - headingDeg + BUS_ICON_HEADING_OFFSET_DEG + 360f) % 360f
    }

    fun applyMarkerRotationFromHeading(marker: Marker, headingDeg: Float, smooth: Boolean) {
        val rotNoMap = headingToMarkerRotNoMap(headingDeg)
        val mapOri = mapView.mapOrientation
        val targetScreenRot = (rotNoMap - mapOri + 360f) % 360f

        marker.rotation = if (!smooth) {
            targetScreenRot
        } else {
            lerpAngleDeg(marker.rotation, targetScreenRot, 0.35f)
        }
    }

    fun refreshAllBusRotations() {
        busMarkers.forEach { (id, marker) ->
            val h = lastBusHeading[id] ?: return@forEach
            applyMarkerRotationFromHeading(marker, h, smooth = false)
        }
        mapView.invalidate()
    }

    AndroidView(
        modifier = modifier,
        factory = {
            mapView.apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(16.0)

                if (!overlays.contains(routeOutline)) overlays.add(routeOutline)
                if (!overlays.contains(routeInner)) overlays.add(routeInner)

                routeOutline.outlinePaint.apply {
                    color = AColor.BLACK
                    strokeWidth = 16f
                    isAntiAlias = true
                }
                routeInner.outlinePaint.apply {
                    color = AColor.parseColor("#EFEFEF")
                    strokeWidth = 8f
                    isAntiAlias = true
                }

                val suppressMs = (ViewConfiguration.getDoubleTapTimeout() + 50).toLong()
                overlays.add(
                    0,
                    MapEventsOverlay(object : MapEventsReceiver {
                        override fun singleTapConfirmedHelper(p: OsmGeoPoint?): Boolean {
                            val now = System.currentTimeMillis()
                            if (now - lastMarkerClickTs > suppressMs) latestOnMapClick()
                            return true
                        }

                        override fun longPressHelper(p: OsmGeoPoint?): Boolean = false
                    })
                )

                addMapListener(object : MapListener {
                    override fun onScroll(event: ScrollEvent?): Boolean {
                        refreshAllBusRotations()
                        latestSelectedId?.let { pushAnchorForBusId(it) }
                        return false
                    }

                    override fun onZoom(event: ZoomEvent?): Boolean {
                        val z = event?.zoomLevel ?: mapView.zoomLevelDouble
                        updateZoom(z)
                        refreshAllBusRotations()
                        return false
                    }
                })
            }
        },
        update = {}
    )

    LaunchedEffect(polyline) {
        val pts = polyline.map { OsmGeoPoint(it.lat, it.lng) }
        routeOutline.setPoints(pts)
        routeInner.setPoints(pts)
        if (!didCenter && pts.isNotEmpty()) {
            mapView.controller.setCenter(pts.first())
            didCenter = true
        }
        mapView.invalidate()
    }

    LaunchedEffect(stops, stopIcon) {
        val stopIds = stops.map { it.id }.toSet()
        stopMarkers.keys.filter { it !in stopIds }.forEach {
            mapView.overlays.remove(stopMarkers.remove(it))
        }

        stops.forEach { s ->
            val gp = OsmGeoPoint(s.position.lat, s.position.lng)
            val existing = stopMarkers[s.id]
            if (existing == null) {
                val m = Marker(mapView).apply {
                    position = gp
                    title = s.name
                    icon = stopIcon
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }
                stopMarkers[s.id] = m
                mapView.overlays.add(m)
            } else {
                existing.position = gp
                existing.icon = stopIcon
            }
        }
        mapView.invalidate()
    }

    LaunchedEffect(buses, selectedBusId, busIcon) {
        val busIds = buses.map { it.id }.toSet()
        busMarkers.keys.filter { it !in busIds }.forEach {
            mapView.overlays.remove(busMarkers.remove(it))
            lastBusPos.remove(it)
            lastBusHeading.remove(it)
        }

        buses.forEach { b ->
            val gp = OsmGeoPoint(b.position.lat, b.position.lng)

            val prev = lastBusPos[b.id]
            val headingDeg: Float = if (prev != null) {
                val d = distanceMeters(prev, b.position)
                if (d < 8.0) {
                    lastBusHeading[b.id] ?: 0f
                } else {
                    val h = bearingDeg(prev, b.position)
                    lastBusHeading[b.id] = h
                    h
                }
            } else {
                lastBusHeading[b.id] ?: 0f
            }

            lastBusPos[b.id] = b.position

            val existing = busMarkers[b.id]
            if (existing == null) {
                val m = Marker(mapView).apply {
                    position = gp
                    title = b.code
                    snippet = b.destination
                    icon = busIcon
                    setAnchor(Marker.ANCHOR_CENTER, BUS_ANCHOR_V)
                    setFlat(false)
                    applyMarkerRotationFromHeading(this, headingDeg, smooth = false)

                    setOnMarkerClickListener { marker, _ ->
                        lastMarkerClickTs = System.currentTimeMillis()
                        latestOnBusClick(b.id)
                        latestOnBusAnchorChanged(computeBusAnchorPx(marker))
                        true
                    }
                }
                busMarkers[b.id] = m
                mapView.overlays.add(m)
            } else {
                existing.position = gp
                existing.icon = busIcon
                existing.setAnchor(Marker.ANCHOR_CENTER, BUS_ANCHOR_V)
                existing.setFlat(false)
                applyMarkerRotationFromHeading(existing, headingDeg, smooth = true)
            }
        }

        selectedBusId?.let { pushAnchorForBusId(it) }
        mapView.invalidate()
    }

    LaunchedEffect(busIcon, selectedBusId) {
        selectedBusId?.let { pushAnchorForBusId(it) }
    }
}

private fun bearingDeg(from: GeoPoint, to: GeoPoint): Float {
    val lat1 = Math.toRadians(from.lat)
    val lat2 = Math.toRadians(to.lat)
    val dLon = Math.toRadians(to.lng - from.lng)
    val y = sin(dLon) * cos(lat2)
    val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)
    val brng = (Math.toDegrees(atan2(y, x)) + 360.0) % 360.0
    return brng.toFloat()
}

private fun distanceMeters(a: GeoPoint, b: GeoPoint): Double {
    val R = 6371000.0
    val dLat = Math.toRadians(b.lat - a.lat)
    val dLon = Math.toRadians(b.lng - a.lng)
    val lat1 = Math.toRadians(a.lat)
    val lat2 = Math.toRadians(b.lat)
    val x = sin(dLat / 2) * sin(dLat / 2) +
            sin(dLon / 2) * sin(dLon / 2) * cos(lat1) * cos(lat2)
    val c = 2 * atan2(Math.sqrt(x), Math.sqrt(1 - x))
    return R * c
}

@Composable
private fun rememberMarkerDrawable(res: DrawableResource, size: Dp): Drawable? {
    val context = LocalContext.current
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val painter = painterResource(res)

    return remember(res, size, density, layoutDirection) {
        val px = with(density) { size.roundToPx().coerceAtLeast(1) }
        val image = ImageBitmap(px, px)
        val canvas = Canvas(image)
        val drawScope = CanvasDrawScope()
        drawScope.draw(
            density = density,
            layoutDirection = layoutDirection,
            canvas = canvas,
            size = Size(px.toFloat(), px.toFloat())
        ) { with(painter) { draw(Size(px.toFloat(), px.toFloat())) } }
        BitmapDrawable(context.resources, image.asAndroidBitmap()).apply {
            setBounds(0, 0, px, px)
        }
    }
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
