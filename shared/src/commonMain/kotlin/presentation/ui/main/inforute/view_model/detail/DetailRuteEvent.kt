package presentation.ui.main.inforute.view_model.detail

sealed interface DetailRuteEvent {
    data object RequireLogin : DetailRuteEvent
    data class ShowError(val message: String) : DetailRuteEvent
}