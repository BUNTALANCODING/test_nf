package arhud

import kotlin.math.*

data class LatLng(val lat: Double, val lon: Double)

private const val R = 6378137.0 // meter

private fun degToRad(deg: Double): Double = deg * PI / 180.0
private fun radToDeg(rad: Double): Double = rad * 180.0 / PI

fun haversineMeters(a: LatLng, b: LatLng): Double {
    val dLat = degToRad(b.lat - a.lat)
    val dLon = degToRad(b.lon - a.lon)
    val lat1 = degToRad(a.lat)
    val lat2 = degToRad(b.lat)

    val h = sin(dLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)
    return 2 * R * asin(sqrt(h))
}

fun bearingDeg(from: LatLng, to: LatLng): Float {
    val lat1 = degToRad(from.lat)
    val lat2 = degToRad(to.lat)
    val dLon = degToRad(to.lon - from.lon)

    val y = sin(dLon) * cos(lat2)
    val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)

    val brng = radToDeg(atan2(y, x))
    return ((brng + 360.0) % 360.0).toFloat()
}

fun norm180(angle: Float): Float = ((angle + 540f) % 360f) - 180f

fun lerpAngle(old: Float, new: Float, a: Float): Float {
    val delta = norm180(new - old)
    return (old + a * delta + 360f) % 360f
}
