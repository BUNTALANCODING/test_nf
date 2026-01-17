package business.datasource.network.main.dto.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NearestHalteRequest(
    @SerialName("lat") val lat: String,
    @SerialName("long") val lng: String
)
