package presentation.ui.main.home.view_model

import business.core.ViewSingleAction

sealed class HomeAction : ViewSingleAction {

    sealed class Navigation : HomeAction() {

        data object NavigateToMain : Navigation()

        data object NavigateToLogin : Navigation()

    }

}