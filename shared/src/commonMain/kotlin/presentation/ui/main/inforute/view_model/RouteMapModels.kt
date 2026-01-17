package presentation.ui.main.inforute.view_model

data class GeoPoint(val lat: Double, val lng: Double)

data class StopMapUi(
    val id: String,
    val name: String,
    val position: GeoPoint
)

data class BusMapUi(
    val id: String,
    val code: String,
    val destination: String,
    val eta: String,
    val position: GeoPoint
)

interface RouteMapRepository {
    suspend fun fetchRoutePolyline(routeId: String): List<GeoPoint>
    suspend fun fetchStops(routeId: String): List<StopMapUi>
    suspend fun fetchBuses(routeId: String): List<BusMapUi>
}
