package business.datasource.network.main.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IdentifyRequestDTO(
    @SerialName("question_name") val questionName: String?,
    @SerialName("image_base64") val imageBase64: String?,
)
