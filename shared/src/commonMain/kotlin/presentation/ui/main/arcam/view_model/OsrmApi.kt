package presentation.ui.main.arcam.view_model

import arhud.LatLng
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

class OsrmApi(
    private val http: HttpClient,
    private val baseUrl: String = "https://router.project-osrm.org" // demo
) {
    suspend fun routeFoot(from: LatLng, to: LatLng): List<LatLng> {
        val url = "$baseUrl/route/v1/foot/${from.lon},${from.lat};${to.lon},${to.lat}"
        val resp: OsrmRouteResponse = http.get(url) {
            parameter("overview", "full")
            parameter("geometries", "geojson")
            parameter("steps", "false")
        }.body()

        val r = resp.routes.firstOrNull() ?: error("No routes")
        return r.geometry.coordinates.map { LatLng(lat = it[1], lon = it[0]) }
    }
}

@Serializable
private data class OsrmRouteResponse(
    val routes: List<Route>
) {
    @Serializable data class Route(val geometry: Geometry)
    @Serializable data class Geometry(val coordinates: List<List<Double>>) // [lon,lat]
}
