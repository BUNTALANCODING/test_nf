package presentation.ui.main.inforute.view_model.info

import presentation.ui.main.inforute.RouteInfo

data class InfoRuteState(
    val query: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val routes: List<RouteInfo> = emptyList()
)