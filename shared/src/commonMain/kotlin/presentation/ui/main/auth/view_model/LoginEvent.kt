package presentation.ui.main.auth.view_model

import business.core.NetworkState
import business.core.UIComponent
import business.core.ViewEvent
import presentation.ui.main.home.view_model.HomeEvent

sealed class LoginEvent : ViewEvent {

    data class OnUpdateNameRegister(val value: String) : LoginEvent()

    data class OnUpdateEmailRegister(val value: String) : LoginEvent()

    data class OnUpdateAddressRegister(val value: String) : LoginEvent()

    data class OnUpdatePhoneRegister(val value: String) : LoginEvent()

    data class OnUpdateConfirmPasswordRegister(val value: String) : LoginEvent()

    data class OnUpdateUsernameLogin(val value: String) : LoginEvent()

    data class OnUpdateFCMToken(val value: String) : LoginEvent()

    data class OnUpdatePasswordLogin(val value: String) : LoginEvent()

    data class ForgotPassword(val value: String) : LoginEvent()

    data object ResendForgotPassword : LoginEvent()

    data object ResetPasswordField : LoginEvent()

    data class IsSuccessResend(val value: Boolean) : LoginEvent()

    data object Register : LoginEvent()

    data object Login : LoginEvent()

    data object OnRetryNetwork : LoginEvent()

    data class OnUpdateNetworkState(val networkState: NetworkState) : LoginEvent()
}
