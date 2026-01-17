package business.interactors.main


import business.datasource.network.main.MainService
import business.datasource.network.main.dto.request.MapsRouteRequest
import presentation.ui.main.inforute.view_model.GeoPoint
import presentation.ui.main.inforute.view_model.StopMapUi

data class RouteMapResult(
    val topic: String,
    val polyline: List<GeoPoint>,
    val stops: List<StopMapUi>
)

class GetRouteMapUseCase(
    private val mainService: MainService
) {

    suspend operator fun invoke(routeCode: String): Result<RouteMapResult> {
        return runCatching {
            val response = mainService.mapsRoute(
                MapsRouteRequest(route_code = routeCode)
            )

            if (response.status != true) {
                throw IllegalStateException(response.message ?: "Request gagal")
            }

            val data = response.data
                ?: throw IllegalStateException(response.message ?: "Data kosong")

            val polyline = data.routePath.coordinates.mapNotNull { c ->
                if (c.size >= 2) GeoPoint(lat = c[1], lng = c[0]) else null
            }

            val stops = data.haltePosition.mapIndexed { i, h ->
                StopMapUi(
                    id = "s$i",
                    name = h.name,
                    position = GeoPoint(lat = h.coordinates.lat, lng = h.coordinates.lng)
                )
            }

            RouteMapResult(
                topic = data.topic,
                polyline = polyline,
                stops = stops
            )
        }
    }
}
