package common.map

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.platform.LocalDensity
import kotlinx.cinterop.*
import kotlinx.cinterop.get
import platform.CoreGraphics.CGAffineTransformMakeRotation
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.Foundation.NSBundle
import platform.Foundation.NSSelectorFromString
import platform.MapKit.*
import platform.UIKit.*
import platform.darwin.NSObject
import presentation.ui.main.inforute.view_model.BusMapUi
import presentation.ui.main.inforute.view_model.GeoPoint
import presentation.ui.main.inforute.view_model.StopMapUi
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

@OptIn(ExperimentalForeignApi::class)
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
    val density = LocalDensity.current
    val pxPerPoint = density.density.toDouble()

    val latestBusClick by rememberUpdatedState(onBusClick)
    val latestMapClick by rememberUpdatedState(onMapClick)
    val latestAnchor by rememberUpdatedState(onBusAnchorChanged)
    val latestSelectedId by rememberUpdatedState(selectedBusId)

    val delegate = remember { RouteMapDelegate() }.also {
        it.onBusClick = { id -> latestBusClick(id) }
        it.onMapClick = { latestMapClick() }
        it.onBusAnchorChanged = { off -> latestAnchor(off) }
        it.pxPerPoint = pxPerPoint
        it.selectedBusId = latestSelectedId
    }

    var didFit by remember(polyline) { mutableStateOf(false) }

    UIKitView(
        modifier = modifier,
        factory = {
            MKMapView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0)).apply {
                delegate.mapViewRef = this
                this.delegate = delegate

                rotateEnabled = false
                pitchEnabled = false
                showsCompass = false
                showsScale = false

                val tap = UITapGestureRecognizer(
                    target = delegate,
                    action = NSSelectorFromString("onMapTapped")
                )
                tap.cancelsTouchesInView = false
                tap.delegate = delegate
                addGestureRecognizer(tap)
            }
        },
        update = { mapView ->
            delegate.mapViewRef = mapView
            delegate.pxPerPoint = pxPerPoint
            delegate.selectedBusId = latestSelectedId

            mapView.removeOverlays(mapView.overlays)
            if (polyline.size >= 2) {
                memScoped {
                    val n = polyline.size
                    val coords = allocArray<CLLocationCoordinate2D>(n)
                    for (i in 0 until n) {
                        coords[i].latitude = polyline[i].lat
                        coords[i].longitude = polyline[i].lng
                    }

                    val outline = MKPolyline.polylineWithCoordinates(coords, n.toULong())
                    val inner = MKPolyline.polylineWithCoordinates(coords, n.toULong())

                    delegate.outlineOverlay = outline
                    delegate.innerOverlay = inner

                    mapView.addOverlay(outline)
                    mapView.addOverlay(inner)

                    if (!didFit) {
                        fitToPolyline(mapView, polyline)
                        didFit = true
                    }
                }
            }

            val toRemove = mapView.annotations.filter { it !is MKUserLocation }
            mapView.removeAnnotations(toRemove)

            stops.forEach { s ->
                val ann = MKPointAnnotation().apply {
                    setCoordinate(CLLocationCoordinate2DMake(s.position.lat, s.position.lng))
                    setTitle(s.name)
                    setSubtitle("STOP:${s.id}")
                }
                mapView.addAnnotation(ann)
            }

            buses.forEach { b ->
                val coord: CValue<CLLocationCoordinate2D> =
                    CLLocationCoordinate2DMake(b.position.lat, b.position.lng)

                val prev: CValue<CLLocationCoordinate2D>? = delegate.lastBusCoord[b.id]

                val headingDeg: Double =
                    if (prev != null) {
                        val d = delegate.distanceMeters(prev, coord)
                        if (d < delegate.minSegMeters) {
                            delegate.lastBusHeading[b.id] ?: 0.0
                        } else {
                            val h = delegate.bearingDeg(prev, coord)
                            delegate.lastBusHeading[b.id] = h
                            h
                        }
                    } else {
                        delegate.lastBusHeading[b.id] ?: 0.0
                    }

                delegate.lastBusCoord[b.id] = coord

                val ann = MKPointAnnotation().apply {
                    setCoordinate(coord)
                    setTitle(b.code)
                    setSubtitle("BUS:${b.id}|${b.destination}|$headingDeg")
                }
                mapView.addAnnotation(ann)
            }

            latestSelectedId?.let { delegate.pushAnchorForBusId(it) }
        }
    )
}

@OptIn(ExperimentalForeignApi::class)
private class RouteMapDelegate : NSObject(),
    MKMapViewDelegateProtocol,
    UIGestureRecognizerDelegateProtocol {

    var outlineOverlay: MKPolyline? = null
    var innerOverlay: MKPolyline? = null

    var onBusClick: (String) -> Unit = {}
    var onMapClick: () -> Unit = {}
    var onBusAnchorChanged: (Offset) -> Unit = {}

    var selectedBusId: String? = null
    var mapViewRef: MKMapView? = null
    var pxPerPoint: Double = 1.0

    val lastBusCoord = mutableMapOf<String, CValue<CLLocationCoordinate2D>>()
    val lastBusHeading = mutableMapOf<String, Double>()

    val minSegMeters: Double = 8.0

    private val BUS_ANCHOR_V = 0.70
    private val POINTER_TIP_GAP_PT = 2.0
    private val BUBBLE_NUDGE_UP_PT = 14.0

    private val BUS_ICON_HEADING_OFFSET_DEG = 0.0

    private val BUS_ICON_PT = 28.0
    private val STOP_ICON_PT = 22.0

    private var busImgCached: UIImage? = null
    private var stopImgCached: UIImage? = null

    private fun resizeImage(img: UIImage, targetW: Double, targetH: Double): UIImage {
        val size = CGSizeMake(targetW, targetH)
        UIGraphicsBeginImageContextWithOptions(size, false, 0.0)
        img.drawInRect(CGRectMake(0.0, 0.0, targetW, targetH))
        val out = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return out ?: img
    }

    override fun gestureRecognizer(
        gestureRecognizer: UIGestureRecognizer,
        shouldReceiveTouch: UITouch
    ): Boolean {
        val v = shouldReceiveTouch.view
        if (v is MKAnnotationView) return false
        if (v?.superview is MKAnnotationView) return false
        return true
    }

    @ObjCAction
    fun onMapTapped() {
        selectedBusId = null
        mapViewRef?.selectedAnnotations?.forEach { ann ->
            mapViewRef?.deselectAnnotation(ann as? MKAnnotationProtocol, animated = true)
        }
        onMapClick()
    }

    override fun mapView(mapView: MKMapView, didSelectAnnotationView: MKAnnotationView) {
        val ann = didSelectAnnotationView.annotation as? MKPointAnnotation ?: return
        val sub = ann.subtitle?.toString().orEmpty()
        if (!sub.startsWith("BUS:")) return

        val id = sub.removePrefix("BUS:").substringBefore("|")
        selectedBusId = id
        onBusClick(id)
        emitBusAnchor(mapView, ann)
    }

    override fun mapViewDidChangeVisibleRegion(mapView: MKMapView) {
        selectedBusId?.let { pushAnchorForBusId(it) }
    }

    override fun mapView(mapView: MKMapView, rendererForOverlay: MKOverlayProtocol): MKOverlayRenderer {
        val poly = rendererForOverlay as? MKPolyline ?: return MKOverlayRenderer(rendererForOverlay)
        val r = MKPolylineRenderer(poly)

        when (poly) {
            outlineOverlay -> {
                r.strokeColor = UIColor.blackColor
                r.lineWidth = 16.0
            }
            innerOverlay -> {
                r.strokeColor = UIColor.colorWithRed(0.94, 0.94, 0.94, 1.0)
                r.lineWidth = 8.0
            }
            else -> {
                r.strokeColor = UIColor.blackColor
                r.lineWidth = 10.0
            }
        }
        return r
    }

    private fun loadImage(name: String): UIImage? {
        UIImage.imageNamed(name)?.let { return it }
        val path = NSBundle.mainBundle.pathForResource(name, ofType = "png")
        return path?.let { UIImage.imageWithContentsOfFile(it) }
    }

    override fun mapView(mapView: MKMapView, viewForAnnotation: MKAnnotationProtocol): MKAnnotationView? {
        if (viewForAnnotation is MKUserLocation) return null
        val ann = viewForAnnotation as? MKPointAnnotation ?: return null

        val sub = ann.subtitle?.toString().orEmpty()
        val isBus = sub.startsWith("BUS:")
        val reuseId = if (isBus) "bus" else "stop"

        val imgName = if (isBus) "ic-buss" else "ic_stop_marker"
        val baseImg = loadImage(imgName)

        if (baseImg == null) {
            val fallback =
                (mapView.dequeueReusableAnnotationViewWithIdentifier("fallback_$reuseId") as? MKMarkerAnnotationView)
                    ?: MKMarkerAnnotationView(annotation = ann, reuseIdentifier = "fallback_$reuseId")
            fallback.annotation = ann
            fallback.canShowCallout = false
            fallback.glyphText = if (isBus) "B" else "S"
            fallback.markerTintColor = if (isBus) UIColor.systemOrangeColor else UIColor.systemRedColor
            return fallback
        }

        val view = (mapView.dequeueReusableAnnotationViewWithIdentifier(reuseId) as? MKAnnotationView)
            ?: MKAnnotationView(annotation = ann, reuseIdentifier = reuseId)

        view.annotation = ann
        view.canShowCallout = false

        val sizedImg: UIImage = if (isBus) {
            busImgCached ?: resizeImage(baseImg, BUS_ICON_PT, BUS_ICON_PT).also { busImgCached = it }
        } else {
            stopImgCached ?: resizeImage(baseImg, STOP_ICON_PT, STOP_ICON_PT).also { stopImgCached = it }
        }

        view.image = sizedImg

        val anchorV = if (isBus) BUS_ANCHOR_V else 1.0
        val h = sizedImg.size.useContents { height }
        view.centerOffset = CGPointMake(0.0, h * (0.5 - anchorV))

        if (isBus) {
            val parts = sub.removePrefix("BUS:").split("|")
            val headingDeg = parts.getOrNull(2)?.toDoubleOrNull() ?: 0.0

            val angleRad = ((headingDeg + BUS_ICON_HEADING_OFFSET_DEG) * PI / 180.0)

            view.transform = CGAffineTransformMakeRotation(angleRad)
        } else {
            view.transform = CGAffineTransformMakeRotation(0.0)
        }



        return view
    }

    private fun emitBusAnchor(mapView: MKMapView, ann: MKPointAnnotation) {
        val view = mapView.viewForAnnotation(ann)
        val imgH = view?.image?.size?.useContents { height } ?: 0.0

        val p = mapView.convertCoordinate(ann.coordinate, toPointToView = mapView)
        p.useContents {
            val tipX_pt = x
            val tipY_pt = y - (imgH * BUS_ANCHOR_V) + POINTER_TIP_GAP_PT - BUBBLE_NUDGE_UP_PT

            onBusAnchorChanged(
                Offset(
                    (tipX_pt * pxPerPoint).toFloat(),
                    (tipY_pt * pxPerPoint).toFloat()
                )
            )
        }
    }

    fun pushAnchorForBusId(id: String) {
        val map = mapViewRef ?: return
        map.layoutIfNeeded()

        val ann = map.annotations
            .filterIsInstance<MKPointAnnotation>()
            .firstOrNull { it.subtitle?.toString()?.startsWith("BUS:$id|") == true }
            ?: return

        emitBusAnchor(map, ann)
    }

    fun distanceMeters(a: CValue<CLLocationCoordinate2D>, b: CValue<CLLocationCoordinate2D>): Double {
        val R = 6371000.0
        val (latA, lonA) = a.useContents { latitude to longitude }
        val (latB, lonB) = b.useContents { latitude to longitude }

        val lat1 = latA * PI / 180.0
        val lat2 = latB * PI / 180.0
        val dLat = (latB - latA) * PI / 180.0
        val dLon = (lonB - lonA) * PI / 180.0

        val s1 = sin(dLat / 2.0)
        val s2 = sin(dLon / 2.0)

        val x = s1 * s1 + cos(lat1) * cos(lat2) * s2 * s2
        val c = 2.0 * atan2(sqrt(x), sqrt(1.0 - x))
        return R * c
    }

    fun bearingDeg(a: CValue<CLLocationCoordinate2D>, b: CValue<CLLocationCoordinate2D>): Double {
        val (latA, lonA) = a.useContents { latitude to longitude }
        val (latB, lonB) = b.useContents { latitude to longitude }

        val lat1 = latA * PI / 180.0
        val lat2 = latB * PI / 180.0
        val dLon = (lonB - lonA) * PI / 180.0

        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)

        return (atan2(y, x) * 180.0 / PI + 360.0) % 360.0
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun fitToPolyline(map: MKMapView, pts: List<GeoPoint>) {
    var minLat = Double.POSITIVE_INFINITY
    var maxLat = Double.NEGATIVE_INFINITY
    var minLng = Double.POSITIVE_INFINITY
    var maxLng = Double.NEGATIVE_INFINITY

    pts.forEach { p ->
        minLat = min(minLat, p.lat)
        maxLat = max(maxLat, p.lat)
        minLng = min(minLng, p.lng)
        maxLng = max(maxLng, p.lng)
    }

    val center = CLLocationCoordinate2DMake((minLat + maxLat) / 2.0, (minLng + maxLng) / 2.0)
    val latDelta = (maxLat - minLat) * 1.5
    val lngDelta = (maxLng - minLng) * 1.5

    val span = MKCoordinateSpanMake(
        if (latDelta == 0.0) 0.01 else max(0.01, latDelta),
        if (lngDelta == 0.0) 0.01 else max(0.01, lngDelta)
    )

    map.setRegion(MKCoordinateRegionMake(center, span), animated = false)
}
