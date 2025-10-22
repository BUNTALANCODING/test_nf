package business.datasource.network.main.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetVehicleDTO(
    @SerialName("fleet_id") val fleetId: String? = "",
    @SerialName("plat_number") val platNumber: String? = ""
)
