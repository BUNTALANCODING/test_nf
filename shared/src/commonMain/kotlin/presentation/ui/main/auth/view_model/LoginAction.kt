package presentation.ui.main.auth.view_model

import common.auth.AuthUiContext

sealed interface LoginAction {
    data class Google(val ui: AuthUiContext) : LoginAction
    data object ErrorShown : LoginAction
}


