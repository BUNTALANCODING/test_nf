package business.datasource.network.main.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RouteListResponse(
    @SerialName("corridor_code") val corridorCode: Int,
    @SerialName("route_code") val routeCode: String,
    @SerialName("route_name") val routeName: String,
    @SerialName("operational_hour") val operationalHour: String,
    @SerialName("status_operational") val statusOperational: String
)
