package business.datasource.network.main.responses

import kotlinx.serialization.Serializable

@Serializable
data class UploadFileDTO(
    val filename: String? = ""
)
