package business.datasource.network.main.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PreviewBADTO(
    @SerialName("file") val file: String?,
    @SerialName("file_base64") val base64: String?,
)