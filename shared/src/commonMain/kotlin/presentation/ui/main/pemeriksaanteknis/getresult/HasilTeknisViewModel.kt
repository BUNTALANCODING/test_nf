// HasilTeknisViewModel.kt
package presentation.ui.main.pemeriksaanteknis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import business.constants.DataStoreKeys
import business.core.AppDataStore
import business.datasource.network.main.MainService
import business.datasource.network.main.responses.HasilTeknisDTO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException



//class HasilTeknisViewModel(
//    private val service: MainService,
//    private val appDataStore: AppDataStore
//) : ViewModel() {
//
//    private val _state = MutableStateFlow(HasilTeknisState())
//    val state: StateFlow<HasilTeknisState> = _state
//
//    private var pollingJob: Job? = null
//
//    fun loadHasil(uniqueKey: String) {
//        pollingJob?.cancel()
//
//        pollingJob = viewModelScope.launch {
//            _state.value = HasilTeknisState(
//                isLoading = true,
//                data = null,
//                error = null
//            )
//
//            val tokenRaw = appDataStore.readValue(DataStoreKeys.TOKEN).orEmpty()
//            if (tokenRaw.isBlank()) {
//                _state.value = HasilTeknisState(
//                    isLoading = false,
//                    data = null,
//                    error = "Token kosong, user belum login / token belum tersimpan"
//                )
//                return@launch
//            }
//
//            val delayMs = 5_000L // 5 detik jeda antar request
//
//            while (true) {
//                try {
//                    val result: HasilTeknisDTO = service.getInteriorResult(
//                        token = tokenRaw,
//                        uniqueKey = uniqueKey
//                    )
//
//                    val data = result.data
//                    val responseList = data.response   // pastikan nullable di DTO
//                    val statusLower = data.status.lowercase()
//                    val msgLower = result.message.lowercase()
//
//                    println("HASIL_TEKNIS: status=${data.status}, message=${result.message}, responseNull=${responseList.isNullOrEmpty()}")
//
//                    _state.value = _state.value.copy(
//                        data = result,
//                        error = null,
//                        isLoading = true
//                    )
//
//                    val hasResponse = !responseList.isNullOrEmpty()
//
//                    val stillProcessingByStatus =
//                        statusLower == "sent" || statusLower == "processing"
//
//                    val stillProcessingByMessage =
//                        msgLower.contains("masih dalam proses identifikasi")
//
//                    val stillProcessing = stillProcessingByStatus || stillProcessingByMessage
//
//                    if (hasResponse || !stillProcessing) {
//                        _state.value = _state.value.copy(
//                            isLoading = false
//                        )
//                        println("HASIL_TEKNIS: stop polling, final status=$statusLower, hasResponse=$hasResponse")
//                        break
//                    }
//
//                    println("HASIL_TEKNIS: masih proses, lanjut polling...")
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    _state.value = _state.value.copy(
//                        isLoading = false,
//                        error = e.message ?: "Terjadi kesalahan saat mengambil hasil"
//                    )
//                    println("HASIL_TEKNIS: error, stop polling -> ${e.message}")
//                    break
//                }
//
//                delay(delayMs)
//            }
//        }
//    }
//
//
//    override fun onCleared() {
//        super.onCleared()
//        pollingJob?.cancel()
//    }
//}

//class HasilTeknisViewModel(
//    private val service: MainService,
//    private val appDataStore: AppDataStore
//) : ViewModel() {
//
//    private val _state = MutableStateFlow(HasilTeknisState())
//    val state: StateFlow<HasilTeknisState> = _state
//
//    private var pollingJob: Job? = null
//
//    fun loadHasil(uniqueKey: String) {
//        pollingJob?.cancel()
//
//        pollingJob = viewModelScope.launch {
//            _state.value = HasilTeknisState(
//                isLoading = true,
//                data = null,
//                error = null
//            )
//
//            val tokenRaw = appDataStore.readValue(DataStoreKeys.TOKEN).orEmpty()
//            if (tokenRaw.isBlank()) {
//                _state.value = HasilTeknisState(
//                    isLoading = false,
//                    data = null,
//                    error = "Token kosong, user belum login / token belum tersimpan"
//                )
//                return@launch
//            }
//
//            val delayMs = 5_000L
//
//            while (true) {
//                try {
//                    val result: HasilTeknisDTO = service.getInteriorResult(
//                        token = tokenRaw,
//                        uniqueKey = uniqueKey
//                    )
//
//                    val data = result.data
//                    val responseList = data.response
//                    val statusLower = data.status.lowercase()
//                    val msgLower = result.message.lowercase()
//
//                    println("HASIL_TEKNIS: status=${data.status}, msg=${result.message}, responseNull=${responseList.isNullOrEmpty()}")
//
//                    _state.value = _state.value.copy(
//                        data = result,
//                        error = null,
//                        isLoading = true
//                    )
//
//                    val hasResponse = !responseList.isNullOrEmpty()
//
//                    val stillProcessingByStatus =
//                        statusLower == "sent" || statusLower == "processing"
//                    val stillProcessingByMessage =
//                        msgLower.contains("masih dalam proses identifikasi")
//
//                    val stillProcessing = stillProcessingByStatus || stillProcessingByMessage
//
//                    if (hasResponse || !stillProcessing) {
//                        _state.value = _state.value.copy(isLoading = false)
//                        println("HASIL_TEKNIS: stop polling, final status=$statusLower, hasResponse=$hasResponse")
//                        break
//                    }
//
//                    println("HASIL_TEKNIS: masih proses, lanjut polling...")
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    _state.value = _state.value.copy(
//                        isLoading = false,
//                        error = e.message ?: "Terjadi kesalahan saat mengambil hasil"
//                    )
//                    println("HASIL_TEKNIS: error, stop polling -> ${e.message}")
//                    break
//                }
//
//                delay(delayMs)
//            }
//        }
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        pollingJob?.cancel()
//    }
//}



class HasilTeknisViewModel(
    private val service: MainService,
    private val appDataStore: AppDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(HasilTeknisState())
    val state: StateFlow<HasilTeknisState> = _state

    fun loadHasil(uniqueKey: String) {
        viewModelScope.launch {
            // state awal
            _state.value = HasilTeknisState(
                isLoading = true,
                data = null,
                error = null
            )

            try {
                val token = appDataStore.readValue(DataStoreKeys.TOKEN).orEmpty()
                if (token.isBlank()) {
                    _state.value = HasilTeknisState(
                        isLoading = false,
                        data = null,
                        error = "Token kosong"
                    )
                    return@launch
                }

                val delayMs = 5_000L

                while (true) {
                    try {
                        val result: HasilTeknisDTO = service.getInteriorResult(
                            token = token,
                            uniqueKey = uniqueKey
                        )

                        val data = result.data
                        val responseList = data.response ?: emptyList()

                        println("HASIL_VM: status=${data.status}, responseSize=${responseList.size}")

                        _state.value = _state.value.copy(
                            data = result,
                            error = null
                        )

                        val statusLower = data.status.lowercase()
                        val hasResponse = responseList.isNotEmpty()

                        // berhenti hanya kalau sudah completed + response ada
                        if (statusLower == "completed" && hasResponse) {
                            _state.value = _state.value.copy(isLoading = false)
                            println("HASIL_VM: completed, stop polling")
                            return@launch
                        }

                        // masih proses
                        _state.value = _state.value.copy(isLoading = true)

                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        e.printStackTrace()

                        val msg = e.message ?: "Terjadi kesalahan"

                        _state.value = HasilTeknisState(
                            isLoading = false,
                            data = null,
                            error = msg
                        )
                        return@launch
                    }

                    delay(delayMs)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = HasilTeknisState(
                    isLoading = false,
                    data = null,
                    error = e.message ?: "Terjadi kesalahan"
                )
            }
        }
    }
}



