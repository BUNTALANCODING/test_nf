package common.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas as ACanvas
import android.graphics.Paint
import android.graphics.Path as APath
import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import arhud.LatLng
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@Composable
actual fun KmpMapView(
    modifier: Modifier,
    routePoints: List<LatLng>,
    user: LatLng?,
    followUser: Boolean,
    showUserMarker: Boolean,
    userHeadingDeg: Float
) {
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            initOsmdroid(ctx)
            MapView(ctx).apply {
                setMultiTouchControls(true)
                controller.setZoom(17.0)
            }
        },
        update = { map ->
            map.overlays.clear()

            if (routePoints.size >= 2) {
                val line = Polyline().apply {
                    setPoints(routePoints.map { GeoPoint(it.lat, it.lon) })
                    outlinePaint.strokeWidth = 8f
                }
                map.overlays.add(line)
            }

            if (user != null && showUserMarker) {
                val gp = GeoPoint(user.lat, user.lon)

                val marker = Marker(map).apply {
                    position = gp
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                    icon = makeBlueArrowDrawable(map.context)
                    rotation = userHeadingDeg
                    infoWindow = null
                }
                map.overlays.add(marker)

                if (followUser) map.controller.setCenter(gp)
            } else if (user != null && followUser) {
                map.controller.setCenter(GeoPoint(user.lat, user.lon))
            }

            map.invalidate()
        }
    )
}

private fun initOsmdroid(ctx: Context) {
    Configuration.getInstance().userAgentValue = ctx.packageName
}

private fun makeBlueArrowDrawable(ctx: Context): BitmapDrawable {
    val density = ctx.resources.displayMetrics.density
    val sizePx = (46f * density).toInt().coerceAtLeast(24)
    val bmp = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
    val c = ACanvas(bmp)

    val blue = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = 0xFF2F6FED.toInt()
    }
    val whiteStroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = sizePx * 0.09f
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        color = 0xFFFFFFFF.toInt()
    }
    val shadow = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = 0x2D000000
    }

    val w = sizePx.toFloat()
    val h = sizePx.toFloat()
    val cx = w / 2f

    val p = APath().apply {
        moveTo(cx, 0f)
        lineTo(w, h * 0.84f)
        lineTo(cx, h * 0.68f)
        lineTo(0f, h * 0.84f)
        close()
    }

    c.save()
    c.translate(w * 0.03f, h * 0.04f)
    c.drawPath(p, shadow)
    c.restore()

    c.drawPath(p, blue)
    c.drawPath(p, whiteStroke)

    return BitmapDrawable(ctx.resources, bmp)
}
