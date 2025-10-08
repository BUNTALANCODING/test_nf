package business.datasource.network.splash.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginDTO(
    @SerialName("token") val token: String?
)