package presentation.ui.main.inforute.view_model.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import business.datasource.network.main.dto.response.TripResponse
import business.interactor.GetRouteDetailUseCase
import business.interactor.GetTripUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailRuteViewModel(
    private val useCase: GetRouteDetailUseCase,
    private val getTripUseCase: GetTripUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailRuteState())
    val state: StateFlow<DetailRuteState> = _state.asStateFlow()

    private val _trip = MutableStateFlow<TripResponse?>(null)
    val trip: StateFlow<TripResponse?> = _trip.asStateFlow()

    private val _events = Channel<DetailRuteEvent>(capacity = Channel.BUFFERED)
    val events: Flow<DetailRuteEvent> = _events.receiveAsFlow()

    private var lastCorridor: String? = null


    fun onAction(action: DetailRuteAction) {
        when (action) {
            is DetailRuteAction.Load -> {
                lastCorridor = action.corridorCode
                load(action.corridorCode)
            }

            DetailRuteAction.Refresh -> {
                lastCorridor?.let { load(it) }
            }

            DetailRuteAction.ErrorShown -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    private fun load(corridorCode: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            runCatching { useCase(corridorCode) }
                .onSuccess { response ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            data = response,
                            error = null
                        )
                    }
                }
                .onFailure { e ->
                    val msg = e.message ?: "Terjadi kesalahan"

                    if (msg == "TOKEN_EMPTY") {
                        _state.update { it.copy(isLoading = false) }
                        _events.trySend(DetailRuteEvent.RequireLogin)
                        return@launch
                    }

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = msg,
                            data = null
                        )
                    }
                    _events.trySend(DetailRuteEvent.ShowError(msg))
                }
        }
    }

    fun loadTrip(routeCode: String, lat: String, long: String) {
        viewModelScope.launch {
            runCatching { getTripUseCase(routeCode, lat, long) }
                .onSuccess { _trip.value = it }
                .onFailure { _trip.value = null }
        }
    }

    /**
     * Saat user klik tombol SWAP:
     * Hanya memuat data trip baru berdasarkan nextRoute
     * tanpa memanggil ulang API detail rute.
     */
    fun swapToNextRoute(lat: String, long: String) {
        val nextRoute = _trip.value?.nextRoute?.trim().orEmpty()
        if (nextRoute.isBlank()) return

        viewModelScope.launch {
            runCatching {
                getTripUseCase(nextRoute, lat, long)
            }.onSuccess { newTrip ->
                _trip.value = newTrip
            }.onFailure {
                _events.trySend(DetailRuteEvent.ShowError("Gagal memuat rute berikutnya"))
            }
        }
    }
}
