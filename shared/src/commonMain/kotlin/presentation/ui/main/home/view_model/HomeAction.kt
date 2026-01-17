package presentation.ui.main.home.view_model

sealed interface HomeAction {
    data object Load : HomeAction
    data object Refresh : HomeAction
    data object ErrorShown : HomeAction
}