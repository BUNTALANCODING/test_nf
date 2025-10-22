package business.datasource.network.main.responses


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VehiclePhotoDTO(
    @SerialName("front_image") val frontImage: String?,
    @SerialName("back_image") val backImage: String?,
    @SerialName("right_image") val rightImage: String?,
    @SerialName("left_image") val leftImage: String?,
    @SerialName("nrkb_image") val nrkbImage: String?,
)
