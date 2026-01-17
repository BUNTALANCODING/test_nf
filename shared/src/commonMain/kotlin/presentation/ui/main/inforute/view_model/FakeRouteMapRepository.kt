package presentation.ui.main.inforute.view_model

import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sin

class FakeRouteMapRepository : RouteMapRepository {

    private val t0 = getTimeMillis()

    private val route: List<GeoPoint> = buildList {
        val start = GeoPoint(-6.59550, 106.81680)
        val end   = GeoPoint(-6.60780, 106.82950)

        val n = 30
        for (i in 0..n) {
            val f = i.toFloat() / n

            val lat = lerp(start.lat, end.lat, f)
            val lngBase = lerp(start.lng, end.lng, f)

            // curve kecil biar gak lurus
            val curve = (sin(f.toDouble() * PI) * 0.0012).toFloat()

            add(GeoPoint(lat, lngBase + curve))
        }
    }

    private val stops = listOf(
        StopMapUi("s1", "Terminal Bubulak", route[2]),
        StopMapUi("s2", "Ruko Yasmin 1", route[7]),
        StopMapUi("s3", "Radar Bogor", route[12]),
        StopMapUi("s4", "Semplak", route[16]),
        StopMapUi("s5", "Transmart", route[20]),
        StopMapUi("s6", "Cimanggu 1", route[24]),
        StopMapUi("s7", "Ramayana Jalan Baru", route[28]),
    )

    override suspend fun fetchRoutePolyline(routeId: String): List<GeoPoint> {
        delay(120)
        return route
    }

    override suspend fun fetchStops(routeId: String): List<StopMapUi> {
        delay(100)
        return stops
    }

    override suspend fun fetchBuses(routeId: String): List<BusMapUi> {
        val now = getTimeMillis()
        val elapsed = (now - t0).coerceAtLeast(0L)

        val p1 = ((elapsed % 60_000L).toFloat() / 60_000f)
        val p2 = (((elapsed + 18_000L) % 90_000L).toFloat() / 90_000f)

        val pos1 = positionOnPolyline(route, p1)
        val pos2 = positionOnPolyline(route, p2)

        val eta1 = (3 + (2 * (1f - p1))).roundToInt()
        val eta2 = (5 + (3 * (1f - p2))).roundToInt()

        return listOf(
            BusMapUi("bus1", "TP 024", "Menuju UIKA 1", "$eta1 menit", pos1),
            BusMapUi("bus2", "TP 011", "Menuju Terminal Bubulak", "$eta2 menit", pos2),
        )
    }

    private fun lerp(a: Double, b: Double, t: Float): Double = a + (b - a) * t

    private fun positionOnPolyline(points: List<GeoPoint>, t: Float): GeoPoint {
        if (points.size < 2) return points.firstOrNull() ?: GeoPoint(0.0, 0.0)

        val clamped = t.coerceIn(0f, 1f)
        val segs = points.size - 1
        val x = clamped * segs
        val i = floor(x).toInt().coerceIn(0, segs - 1)
        val localT = (x - i).toFloat()

        val a = points[i]
        val b = points[i + 1]
        return GeoPoint(
            lerp(a.lat, b.lat, localT),
            lerp(a.lng, b.lng, localT)
        )
    }
}

