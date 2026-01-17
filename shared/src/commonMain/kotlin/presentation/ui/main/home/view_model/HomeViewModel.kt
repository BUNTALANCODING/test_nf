package presentation.ui.main.home.view_model


import dev.icerock.moko.mvvm.viewmodel.ViewModel
import business.interactors.main.GetNearestHalteUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getNearestHalteUseCase: GetNearestHalteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _events = Channel<HomeEvent>(capacity = Channel.BUFFERED)
    val events: Flow<HomeEvent> = _events.receiveAsFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.Load -> loadNearest()
            HomeAction.Refresh -> loadNearest()
            HomeAction.ErrorShown -> _state.update { it.copy(errorMessage = null) }
        }
    }

    private fun loadNearest() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val (halte, arrivals) = getNearestHalteUseCase()
                _state.update {
                    it.copy(
                        isLoading = false,
                        halteTerdekat = halte,
                        busArrivals = arrivals
                    )
                }
            } catch (e: Throwable) {
                val msg = e.message ?: "Terjadi kesalahan"
                if (msg == "TOKEN_EMPTY") {
                    _state.update { it.copy(isLoading = false) }
                    _events.trySend(HomeEvent.RequireLogin)
                    return@launch
                }
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = msg,
                        halteTerdekat = null,
                        busArrivals = emptyList()
                    )
                }
                _events.trySend(HomeEvent.ShowError(msg))
            }
        }
    }
}
