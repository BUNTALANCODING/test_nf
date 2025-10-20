package business.datasource.network.main.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RampcheckStartRequestDTO(
    @SerialName("inspection_date") val inspectionDate: String?,
    @SerialName("rampcheck_location_id") val rampcheckLocationId: String?,
    @SerialName("latitude") val latitude: String?,
    @SerialName("longitude") val longitude: String?,

    )