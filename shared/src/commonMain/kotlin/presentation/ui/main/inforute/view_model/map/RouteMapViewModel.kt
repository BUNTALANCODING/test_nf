package presentation.ui.main.inforute.view_model.map


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import business.interactors.main.GetRouteMapUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RouteMapViewModel(
    private val getRouteMap: GetRouteMapUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RouteMapState())
    val state = _state.asStateFlow()

    fun loadRoute(routeCode: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            getRouteMap(routeCode)
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            polyline = result.polyline,
                            stops = result.stops
                        )
                    }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
        }
    }
}

//package presentation.ui.main.inforute.view_model.map
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import business.interactors.main.GetRouteMapUseCase
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import presentation.ui.main.inforute.view_model.BusMapUi
//
//class RouteMapViewModel(
//    private val getRouteMap: GetRouteMapUseCase,
//    private val observeBus: ObserveBusLocationUseCase // nanti kita buat
//) : ViewModel() {
//
//    private val _state = MutableStateFlow(RouteMapState())
//    val state = _state.asStateFlow()
//
//    private var loadJob: Job? = null
//    private var busJob: Job? = null
//    private var lastRouteCode: String? = null
//
//    fun loadRoute(routeCode: String) {
//        val rc = routeCode.trim()
//        if (rc.isBlank()) {
//            _state.update { it.copy(isLoading = false, error = "routeCode kosong") }
//            return
//        }
//
//        val current = _state.value
//        if (lastRouteCode == rc && current.polyline.isNotEmpty() && current.error == null) return
//
//        lastRouteCode = rc
//        loadJob?.cancel()
//
//        loadJob = viewModelScope.launch {
//            _state.update {
//                it.copy(
//                    isLoading = true,
//                    error = null,
//                    routeCode = rc,
//                    topic = null,
//                    polyline = emptyList(),
//                    stops = emptyList(),
//                    buses = emptyList()
//                )
//            }
//
//            getRouteMap(rc)
//                .onSuccess { result ->
//                    _state.update {
//                        it.copy(
//                            isLoading = false,
//                            error = null,
//                            topic = result.topic,
//                            polyline = result.polyline,
//                            stops = result.stops
//                        )
//                    }
//                    // auto start realtime bus
//                    startRealtime(result.topic)
//                }
//                .onFailure { e ->
//                    _state.update {
//                        it.copy(
//                            isLoading = false,
//                            error = e.message ?: "Gagal memuat peta"
//                        )
//                    }
//                }
//        }
//    }
//
//    fun startRealtime(topic: String?) {
//        val t = topic?.trim()
//        if (t.isNullOrBlank()) return
//
//        busJob?.cancel()
//        busJob = viewModelScope.launch {
//            observeBus(t).collect { buses: List<BusMapUi> ->
//                _state.update { it.copy(buses = buses) }
//            }
//        }
//    }
//
//    fun stopRealtime() {
//        busJob?.cancel()
//    }
//
//    fun clear() {
//        loadJob?.cancel()
//        stopRealtime()
//        lastRouteCode = null
//        _state.value = RouteMapState()
//    }
//}

