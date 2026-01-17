package presentation.ui.main.auth.view_model

import common.auth.AuthUiContext


sealed interface LoginEvent {
    data class GoogleLogin(val ui: AuthUiContext) : LoginEvent
    object GuestLogin : LoginEvent
    object Logout : LoginEvent
}
