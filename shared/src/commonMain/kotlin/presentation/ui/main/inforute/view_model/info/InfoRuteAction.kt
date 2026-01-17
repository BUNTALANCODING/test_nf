package presentation.ui.main.inforute.view_model.info

sealed interface InfoRuteAction {
    data object Load : InfoRuteAction
    data object Refresh : InfoRuteAction
    data class QueryChange(val value: String) : InfoRuteAction
    data object ErrorShown : InfoRuteAction
}