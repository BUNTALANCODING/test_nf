package business.datasource.network.main.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TripRequest(
    @SerialName("route_code") val routeCode: String,
    @SerialName("lat") val lat: String,
    @SerialName("long") val long: String
)