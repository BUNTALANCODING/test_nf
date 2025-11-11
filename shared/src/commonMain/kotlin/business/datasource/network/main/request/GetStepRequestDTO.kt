package business.datasource.network.main.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetStepRequestDTO(
    @SerialName("step") val step: Int?,
)
