package business.datasource.network.main.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Route(
    @SerialName("route_id") var routeId: Int? = null,
    @SerialName("route_code") var routeCode: String? = null,
    @SerialName("route_name") var routeName: String? = null,
    @SerialName("origin_port_name") var originPortName: String? = null,
    @SerialName("origin_port_description") var originPortDescription: String? = null,
    @SerialName("destination_port_name") var destinationPortName: String? = null,
    @SerialName("destination_port_description") var destinationPortDescription: String? = null
)