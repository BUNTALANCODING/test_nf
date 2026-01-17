package presentation.ui.main.home.view_model

sealed interface HomeEvent {
    data class ShowError(val message: String) : HomeEvent
    data object RequireLogin : HomeEvent
}