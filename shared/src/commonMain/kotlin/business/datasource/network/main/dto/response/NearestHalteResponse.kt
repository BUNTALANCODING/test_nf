package business.datasource.network.main.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class NearestHalteResponse(
    val halte: HalteDto,
    @SerialName("bus_arrival") val busArrival: JsonElement? = null
)

@Serializable
data class HalteDto(
    @SerialName("halte_name") val halteName: String,
    val distance: String,
    @SerialName("operational_hour") val operationalHour: String,
    @SerialName("status_operational") val statusOperational: String
)

@Serializable
data class BusArrivalDto(
    @SerialName("corridor_code") val corridorCode: Int,
    @SerialName("bus_code") val busCode: String,
    @SerialName("eta_min") val etaMin: String,
    @SerialName("eta_time") val etaTime: String,
    @SerialName("last_destination") val lastDestination: String
)
