package presentation.ui.main.auth.view_model

import business.datasource.network.main.dto.response.GoogleLoginResponse
import common.auth.AuthUser

data class LoginState(
    val isLoading: Boolean = false,
    val user: AuthUser? = null,
    val apiUser: GoogleLoginResponse? = null,
    val error: String? = null
)

