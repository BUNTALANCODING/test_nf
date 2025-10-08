package business.datasource.network.main.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequestDTO(
    @SerialName("current_password") var currentPassword: String? = "",
    @SerialName("new_password") var newPassword: String? = "",
    @SerialName("new_confirm_password") var newConfirmPassword: String? = ""
)
