package business.datasource.network.main.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PreviewBARequestDTO(
    @SerialName("rampcheck_id") val rampcheckId: Int?,
)
