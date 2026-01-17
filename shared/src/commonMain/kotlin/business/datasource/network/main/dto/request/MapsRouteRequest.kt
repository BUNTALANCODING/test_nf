package business.datasource.network.main.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class MapsRouteRequest(
    val route_code: String
)
