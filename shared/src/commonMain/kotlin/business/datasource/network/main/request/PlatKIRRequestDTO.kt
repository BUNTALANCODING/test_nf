package business.datasource.network.main.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlatKIRRequestDTO(
    @SerialName("plat_number") val platNumber: String?,
    @SerialName("kir_image") val kirImage: String?,
    @SerialName("step") val step: String?
)
