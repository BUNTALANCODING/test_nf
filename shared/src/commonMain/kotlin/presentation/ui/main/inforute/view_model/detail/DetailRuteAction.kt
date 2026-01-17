package presentation.ui.main.inforute.view_model.detail

sealed interface DetailRuteAction {
    data class Load(val corridorCode: String) : DetailRuteAction
    data object Refresh : DetailRuteAction
    data object ErrorShown : DetailRuteAction
}