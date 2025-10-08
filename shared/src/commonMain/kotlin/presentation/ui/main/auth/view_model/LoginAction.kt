package presentation.ui.main.auth.view_model

import business.core.ViewSingleAction

sealed class LoginAction : ViewSingleAction {

    sealed class Navigation : LoginAction() {

        data object NavigateToMain : Navigation()

        data object NavigateToLogin : Navigation()

        data object NavigateToRegister : Navigation()

        data object NavigateToSuccess : Navigation()

    }

}