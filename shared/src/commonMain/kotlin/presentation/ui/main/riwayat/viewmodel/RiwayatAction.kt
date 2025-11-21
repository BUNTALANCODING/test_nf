package presentation.ui.main.riwayat.viewmodel

import business.core.ViewSingleAction
import presentation.ui.main.home.view_model.HomeAction

sealed class RiwayatAction : ViewSingleAction {

    sealed class Navigation : RiwayatAction() {

        data object NavigateToMain : Navigation()

        data object NavigateToLogin : Navigation()
    }
}