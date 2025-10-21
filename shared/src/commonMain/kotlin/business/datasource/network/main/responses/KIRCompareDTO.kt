package business.datasource.network.main.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KIRCompareDTO(
    @SerialName("rampcheck_id") val rampcheckId: Int?,
    @SerialName("stored_plate") val storedPlate: String?,
    @SerialName("status") val status: String?,

)