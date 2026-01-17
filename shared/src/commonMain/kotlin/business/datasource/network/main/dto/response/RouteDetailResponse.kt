package business.datasource.network.main.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RouteDetailResponse(
    @SerialName("route_code")
    val routeCode: String? = null,

    @SerialName("route_name")
    val routeName: String? = null,

    @SerialName("operational_hour")
    val operationalHour: String? = null,

    @SerialName("operational_status")
    val statusOperational: String? = null,

    @SerialName("fare")
    val fare: String? = null
)
