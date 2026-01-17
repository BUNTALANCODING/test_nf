package presentation.ui.main.inforute.view_model.info

sealed interface InfoRuteEvent {
    data object RequireLogin : InfoRuteEvent
    data class ShowError(val message: String) : InfoRuteEvent
}