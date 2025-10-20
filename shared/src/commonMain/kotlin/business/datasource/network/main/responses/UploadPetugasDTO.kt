package business.datasource.network.main.responses


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadPetugasDTO(
	@SerialName("officer_image") val officerImage: String?,
)
