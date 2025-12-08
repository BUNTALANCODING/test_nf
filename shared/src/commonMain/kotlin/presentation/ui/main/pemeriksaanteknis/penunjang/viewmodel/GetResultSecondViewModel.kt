package presentation.ui.main.pemeriksaanteknis.penunjang.viewmodel

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
import logger.getTimeMillis
import presentation.ui.main.pemeriksaanteknis.HasilTeknisState
import kotlin.coroutines.cancellation.CancellationException

class GetResultSecondViewModel(
    private val service: MainService,
    private val appDataStore: AppDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(HasilTeknisState())
    val state: StateFlow<HasilTeknisState> = _state

    private var pollingJob: Job? = null

    fun loadHasil(uniqueKey: String) {
        pollingJob?.cancel()

        pollingJob = viewModelScope.launch {
            _state.value = HasilTeknisState(
                isLoading = true,
                data = null,
                error = null,
                statusMessage = "Video sedang diproses",
                showCancelButton = false
            )

            try {
                val token = appDataStore.readValue(DataStoreKeys.TOKEN).orEmpty()
                if (token.isBlank()) {
                    _state.value = HasilTeknisState(
                        isLoading = false,
                        data = null,
                        error = "Token kosong",
                        statusMessage = ""
                    )
                    return@launch
                }

                val delayMs = 5_000L
                val cancelAfterMs = 20_000L
                val startTime = getTimeMillis()

                while (true) {
                    try {
                        val result: HasilTeknisDTO = service.getResultSecond(
                            token = token,
                            uniqueKey = uniqueKey
                        )

                        val data = result.data
                        val responseList = data.response ?: emptyList()

                        val statusLower = data.status?.lowercase()
                        val hasResponse = responseList.isNotEmpty()

                        val statusMessage = when (statusLower) {
                            "processing" -> "Video sedang diproses"
                            "sent"       -> "Identifikasi sedang diproses"
                            "completed"  -> "Identifikasi selesai diproses"
                            else         -> ""
                        }

                        val elapsed = getTimeMillis() - startTime
                        val showCancel = elapsed >= cancelAfterMs

                        println("HASIL_VM: status=${data.status}, responseSize=${responseList.size}, elapsed=$elapsed")

                        _state.value = _state.value.copy(
                            data = result,
                            error = null,
                            statusMessage = statusMessage,
                            showCancelButton = showCancel
                        )

                        if (statusLower == "completed" && hasResponse) {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                showCancelButton = false
                            )
                            println("HASIL_VM: completed, stop polling")
                            return@launch
                        }

                        _state.value = _state.value.copy(isLoading = true)

                    } catch (e: kotlinx.coroutines.CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        e.printStackTrace()

                        val msg = e.message ?: "Terjadi kesalahan"

                        _state.value = HasilTeknisState(
                            isLoading = false,
                            data = null,
                            error = msg,
                            statusMessage = "",
                            showCancelButton = false
                        )
                        return@launch
                    }

                    delay(delayMs)
                }
            } catch (e: kotlinx.coroutines.CancellationException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    statusMessage = "Dibatalkan oleh pengguna",
                    showCancelButton = false
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = HasilTeknisState(
                    isLoading = false,
                    data = null,
                    error = e.message ?: "Terjadi kesalahan",
                    statusMessage = "",
                    showCancelButton = false
                )
            }
        }
    }

    fun cancel() {
        pollingJob?.cancel()
        _state.value = _state.value.copy(
            isLoading = false,
            statusMessage = "Dibatalkan oleh pengguna",
            showCancelButton = false
        )
    }
}