package presentation.ui.main.auth.view_model

import business.core.NetworkState
import business.core.ProgressBarState
import business.core.ViewState

data class LoginState(
    val nameRegister: String = "",
    val emailRegister: String = "",
    val addressRegister: String = "-",
    val phoneRegister: String = "",
    val confirmPasswordRegister: String = "",
    val usernameLogin: String = "",
    val passwordLogin: String = "",
    val fcmToken: String = "",
    val isTokenValid: Boolean = false,
    val isSuccessRegister: Boolean = false,
    val isSuccessResend: Boolean = false,
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val networkState: NetworkState = NetworkState.Good,
) : ViewState
