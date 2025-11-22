package business.datasource.network.main.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VehiclePhotoRequestDTO(
    @SerialName("front_image") val frontImage: String?,
    @SerialName("back_image") val backImage: String?,
    @SerialName("right_image") val rightImage: String?,
    @SerialName("left_image") val leftImage: String?,
    @SerialName("nrkb_image") val nrkbImage: String?,
    @SerialName("step") val step: String?,
)