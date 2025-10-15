package business.datasource.network.splash.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDTO(
    @SerialName("officer_username") val username: String,
    @SerialName("officer_password") val password: String,
)

@Serializable
data class ForgotRequestDTO(
    @SerialName("email") val email: String
)
