package business.datasource.network.main.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadPetugasRequestDTO(
    @SerialName("officer_image") val officerImage: String?,
)
