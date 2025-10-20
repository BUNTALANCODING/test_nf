package presentation.ui.main.home.view_model

import business.core.ViewSingleAction
import presentation.ui.main.auth.view_model.LoginAction.Navigation

sealed class HomeAction : ViewSingleAction {

    sealed class Navigation : HomeAction() {

        data object NavigateToMain : Navigation()

        data object NavigateToLogin : Navigation()

        data object NavigateToGuide : Navigation()

        data object NavigateToKIR : Navigation()
    }

}