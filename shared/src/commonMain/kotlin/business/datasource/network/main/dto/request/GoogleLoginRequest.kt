package business.datasource.network.main.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class GoogleLoginRequest(
    val id_token: String
)
