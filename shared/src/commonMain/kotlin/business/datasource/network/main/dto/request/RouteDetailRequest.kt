package business.datasource.network.main.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RouteDetailRequest(
    @SerialName("corridor_code") val corridorCode: String
)
