package business.datasource.network.main.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckQRRequestDTO(
    @SerialName("qr_url") val qrUrl: String?,
)