package common.map

import arhud.LatLng
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.readValue
import platform.CoreGraphics.*
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.*
import platform.UIKit.*
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.math.PI

@OptIn(ExperimentalForeignApi::class)
class IOSMapKitView : UIView(frame = CGRectZero.readValue()) {

    private val map = MKMapView(frame = bounds).apply {
        showsUserLocation = false
        rotateEnabled = true
    }

    private var routeOverlay: MKPolyline? = null

    private class UserArrowAnnotation : MKPointAnnotation()

    private val userAnno = UserArrowAnnotation()
    private var userAnnoAdded = false
    private val userReuseId = "userArrow"

    private val arrowImage: UIImage by lazy { makeBlueArrowImage(46.0) }

    private val delegate = object : NSObject(), MKMapViewDelegateProtocol {

        override fun mapView(
            mapView: MKMapView,
            rendererForOverlay: MKOverlayProtocol
        ): MKOverlayRenderer {
            val poly = rendererForOverlay as? MKPolyline
                ?: return MKOverlayRenderer(rendererForOverlay)

            return MKPolylineRenderer(poly).apply {
                strokeColor = UIColor.blackColor
                lineWidth = 6.0
            }
        }

        override fun mapView(
            mapView: MKMapView,
            viewForAnnotation: MKAnnotationProtocol
        ): MKAnnotationView? {
            if (viewForAnnotation is UserArrowAnnotation) {
                val v = mapView.dequeueReusableAnnotationViewWithIdentifier(userReuseId)
                    ?: MKAnnotationView(annotation = viewForAnnotation, reuseIdentifier = userReuseId)

                v.annotation = viewForAnnotation
                v.image = arrowImage
                v.canShowCallout = false
                v.centerOffset = CGPointMake(0.0, 0.0)
                return v
            }

            return null
        }
    }

    init {
        addSubview(map)
        map.delegate = delegate
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        map.setFrame(bounds)
    }

    fun update(
        routePoints: List<LatLng>,
        user: LatLng?,
        followUser: Boolean,
        showUserMarker: Boolean,
        userHeadingDeg: Float
    ) {
        routeOverlay?.let { map.removeOverlay(it) }
        routeOverlay = null

        if (routePoints.size >= 2) {
            memScoped {
                val n = routePoints.size
                val coords = allocArray<CLLocationCoordinate2D>(n)
                for (i in 0 until n) {
                    coords[i].latitude = routePoints[i].lat
                    coords[i].longitude = routePoints[i].lon
                }
                val poly = MKPolyline.polylineWithCoordinates(coords, n.toULong())
                routeOverlay = poly
                map.addOverlay(poly)
            }
        }

        if (user != null && showUserMarker) {
            val coord = CLLocationCoordinate2DMake(user.lat, user.lon)
            userAnno.setCoordinate(coord)

            if (!userAnnoAdded) {
                map.addAnnotation(userAnno)
                userAnnoAdded = true
            }

            applyUserRotation(userHeadingDeg)

            if (followUser) {
                val region = MKCoordinateRegionMakeWithDistance(coord, 800.0, 800.0)
                map.setRegion(region, animated = false)
            }
        } else {
            if (userAnnoAdded) {
                map.removeAnnotation(userAnno)
                userAnnoAdded = false
            }

            if (user != null && followUser) {
                val coord = CLLocationCoordinate2DMake(user.lat, user.lon)
                val region = MKCoordinateRegionMakeWithDistance(coord, 800.0, 800.0)
                map.setRegion(region, animated = false)
            }
        }
    }

    private fun applyUserRotation(deg: Float) {
        val rad = (deg.toDouble() * PI) / 180.0

        val viewNow = map.viewForAnnotation(userAnno) as? MKAnnotationView
        if (viewNow != null) {
            viewNow.transform = CGAffineTransformMakeRotation(rad)
            return
        }

        dispatch_async(dispatch_get_main_queue()) {
            val v = map.viewForAnnotation(userAnno) as? MKAnnotationView
            if (v != null) v.transform = CGAffineTransformMakeRotation(rad)
        }
    }

    private fun makeBlueArrowImage(size: Double): UIImage {
        val sz = CGSizeMake(size, size)
        UIGraphicsBeginImageContextWithOptions(sz, false, 0.0)

        val ctx = UIGraphicsGetCurrentContext()!!
        val w = size
        val h = size
        val cx = w / 2.0

        val blue = UIColor(
            red = 0x2F.toDouble() / 255.0,
            green = 0x6F.toDouble() / 255.0,
            blue = 0xED.toDouble() / 255.0,
            alpha = 1.0
        )

        val p = UIBezierPath().apply {
            moveToPoint(CGPointMake(cx, 0.0))
            addLineToPoint(CGPointMake(w, h * 0.84))
            addLineToPoint(CGPointMake(cx, h * 0.68))
            addLineToPoint(CGPointMake(0.0, h * 0.84))
            closePath()
        }

        CGContextSaveGState(ctx)
        CGContextSetShadowWithColor(
            ctx,
            CGSizeMake(w * 0.03, h * 0.04),
            2.0,
            UIColor.blackColor.colorWithAlphaComponent(0.18).CGColor
        )
        blue.setFill()
        p.fill()
        CGContextRestoreGState(ctx)

        blue.setFill()
        p.fill()

        UIColor.whiteColor.setStroke()
        p.lineWidth = h * 0.09
        p.stroke()

        val img = UIGraphicsGetImageFromCurrentImageContext()!!
        UIGraphicsEndImageContext()
        return img
    }
}
