package presentation.ui.main.inforute.view_model.detail

import business.datasource.network.main.dto.response.RouteDetailResponse

data class DetailRuteState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: RouteDetailResponse? = null
)
