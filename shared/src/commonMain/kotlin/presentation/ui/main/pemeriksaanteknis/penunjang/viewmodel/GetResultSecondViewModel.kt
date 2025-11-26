package presentation.ui.main.pemeriksaanteknis.penunjang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import business.constants.DataStoreKeys
import business.core.AppDataStore
import business.datasource.network.main.MainService
import business.datasource.network.main.responses.HasilTeknisDTO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import presentation.ui.main.pemeriksaanteknis.HasilTeknisState
import kotlin.coroutines.cancellation.CancellationException

class GetResultSecondViewModel(
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
                        val result: HasilTeknisDTO = service.getResultSecond(
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

                        val statusLower = data.status?.lowercase()
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