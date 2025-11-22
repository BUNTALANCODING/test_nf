package business.datasource.network.main.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NegativeAnswerRequestDTO(
    @SerialName("type") val type: String?,
    @SerialName("condition") val condition: String?,
    @SerialName("step") val step: String?,
)