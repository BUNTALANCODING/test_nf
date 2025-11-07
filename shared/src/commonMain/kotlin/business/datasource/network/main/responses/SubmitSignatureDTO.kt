package business.datasource.network.main.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubmitSignatureDTO(
    @SerialName("rampcheck_id") val rampcheckId: Int?,
)