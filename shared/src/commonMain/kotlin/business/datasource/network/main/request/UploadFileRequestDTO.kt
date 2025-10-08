package business.datasource.network.main.request

import kotlinx.serialization.Serializable

@Serializable
data class UploadFileRequestDTO(
    val file: String? = ""
)
