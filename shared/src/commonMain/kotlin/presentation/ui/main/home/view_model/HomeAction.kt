package presentation.ui.main.home.view_model

import business.core.ViewSingleAction
import presentation.ui.main.auth.view_model.LoginAction.Navigation

sealed class HomeAction : ViewSingleAction {

    sealed class Navigation : HomeAction() {

        data object NavigateToMain : Navigation()

        data object NavigateToLogin : Navigation()

        data object NavigateToGuide : Navigation()

        data object NavigateToResultScreen : Navigation()

        data object NavigateToKIR : Navigation()

        data object NavigateToQRKIR : Navigation()

        data object NavigateToPemeriksaanAdministrasi : Navigation()

        data object NavigateToKPReguler : Navigation()

        data object NavigateToKPCadangan : Navigation()

        data object NavigateToSIMPengemudi : Navigation()

        data object NavigateHasilKartuUji : Navigation()

        data object NavigateHasilSIMPengemudi : Navigation()

        data object NavigateToBack : Navigation()

        data object NavigateToTeknisUtama : Navigation()

        data object NavigateToBeritaAcara : Navigation()

        data object Logout : Navigation()
    }

}