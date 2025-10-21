package business.datasource.network.main.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlatKIRDTO(
    @SerialName("plat_number") val platNumber: String?,
    @SerialName("kir_image") val kirImage: String?
)