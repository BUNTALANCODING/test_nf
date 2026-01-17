package presentation.ui.main.inforute.view_model.info

import business.interactor.GetRouteListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import presentation.ui.main.inforute.InfoRuteUiState
import presentation.ui.main.inforute.RouteInfo
import presentation.ui.main.inforute.RouteStatus

class InfoRuteViewModel(
    private val useCase: GetRouteListUseCase
) {
    private val _state = MutableStateFlow(InfoRuteUiState(isLoading = true))
    val state: StateFlow<InfoRuteUiState> = _state

    suspend fun load(query: String = "") {
        val q = query.trim()
        _state.update { it.copy(isLoading = true, errorMessage = null, query = q) }

        runCatching { useCase(q) }
            .onSuccess { dtoList ->
                val routes = dtoList.map { dto ->
                    RouteInfo(
                        id = dto.routeCode,
                        number = dto.corridorCode,
                        title = dto.routeName,
                        operationalHours = dto.operationalHour,
                        status = when (dto.statusOperational.lowercase()) {
                            "masih beroperasi" -> RouteStatus.ACTIVE
                            "hampir selesai" -> RouteStatus.ALMOST_DONE
                            else -> RouteStatus.ENDED
                        }
                    )
                }
                _state.update { it.copy(isLoading = false, routes = routes) }
            }
            .onFailure { e ->
                _state.update { it.copy(isLoading = false, errorMessage = e.message ?: "Terjadi kesalahan") }
            }
    }

    fun onQueryChange(q: String) {
        _state.update { it.copy(query = q) }
    }
}
