package business.datasource.network.main.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateDeviceTokenRequestDTO(
    @SerialName("fcm_token") var token: String? = ""
)
