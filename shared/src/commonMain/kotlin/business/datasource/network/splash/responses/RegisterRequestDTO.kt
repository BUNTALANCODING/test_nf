package business.datasource.network.splash.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDTO(
    @SerialName("email") val email: String,
    @SerialName("name") val name: String,
    @SerialName("address") val address: String,
    @SerialName("phone") val phone: String,
    @SerialName("password") val password: String,
    @SerialName("confirm_password") val confirmPassword: String
)
