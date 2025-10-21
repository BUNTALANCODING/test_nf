package business.datasource.network.main.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KIRCompareRequestDTO(
    @SerialName("qr_plat") val platNumber: String?,
)