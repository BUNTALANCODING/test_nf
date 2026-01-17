package business.datasource.network.main.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MapsRouteResponse(
    val topic: String,
    @SerialName("halte_position")
    val haltePosition: List<HaltePositionDto>,
    @SerialName("route_path")
    val routePath: RoutePathDto
)

@Serializable
data class HaltePositionDto(
    val name: String,
    val coordinates: CoordinatesDto // ✅ ini yang bener
)

@Serializable
data class CoordinatesDto(
    val lng: Double,
    val lat: Double
)

@Serializable
data class RoutePathDto(
    val type: String,
    val coordinates: List<List<Double>> // ✅ sudah benar (lng, lat)
)
