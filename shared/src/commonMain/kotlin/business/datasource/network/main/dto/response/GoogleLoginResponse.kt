package business.datasource.network.main.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class GoogleLoginResponse(
    val fullname: String?,
    val email: String?,
    val phone_number: String?,
    val role: String?,
    val token: String?
)
