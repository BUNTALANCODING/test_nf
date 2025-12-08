package presentation.ui.main.pemeriksaanteknis.utama.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import business.constants.DataStoreKeys
import business.core.AppDataStore
import business.core.DataState
import business.datasource.network.main.responses.ChunkResponse
import business.interactors.main.UploadChunkUseCase
import common.FileContainer
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import presentation.ui.main.uploadChunk.UploadController
import presentation.ui.main.pemeriksaanteknis.utama.viewmodel.UploadState

class UploadViewModel(
    private val uploadChunkUseCase: UploadChunkUseCase,
    private val appDataStore: AppDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(UploadState())
    val state: StateFlow<UploadState> = _state

    private var fileContainer: FileContainer? = null
    private var controller = UploadController()
    private var uploadJob: Job? = null

    private var busTypeId: Int? = null

    fun setBusTypeId(id: Int) {
        busTypeId = id
    }


    fun setFile(container: FileContainer) {
        fileContainer = container
        controller = UploadController()

        _state.value = _state.value.copy(
            fileName = container.fileName,
            status = "Ready to upload",
            progress = 0,
            isUploading = false,
            isPaused = false
        )
    }

    fun startUploadInterior() {
        val container = fileContainer ?: return

        val totalBytes = container.size
        val durationMs = container.durationMs

        val oneGbInBytes = 1_073_741_824L
        val maxDurationMs = 3 * 60 * 1000L

        val reasons = mutableListOf<String>()

        if (totalBytes <= 0L) {
            reasons += "File kosong (0 byte), tidak bisa diupload"
        }

        if (totalBytes > oneGbInBytes) {
            reasons += "Ukuran file melebihi 1 GB"
        }

        if (durationMs > maxDurationMs) {
            reasons += "Durasi video lebih dari 3 menit"
        }

        if (reasons.isNotEmpty()) {
            _state.value = _state.value.copy(
                showLimitDialog = true,
                limitDialogMessage = reasons.joinToString("\n"),
                isUploading = false,
                progress = 0,
                status = "Tidak bisa upload"
            )
            return
        }

        uploadJob?.cancel()

        val maxChunks = 30

        val chunkSizeLong = (totalBytes + maxChunks - 1) / maxChunks
        val chunkSize = chunkSizeLong.toInt()
        val totalChunks = ((totalBytes + chunkSizeLong - 1) / chunkSizeLong).toInt()

        _state.value = _state.value.copy(
            status = "Uploading...",
            isUploading = true,
            progress = 0,
            isPaused = false
        )

        uploadJob = viewModelScope.launch {

            val token = appDataStore.readValue(DataStoreKeys.TOKEN) ?: ""
            println("VM_UPLOAD: TOKEN FROM DATASTORE = '$token'")

            if (token.isBlank()) {
                _state.value = _state.value.copy(
                    status = "Token kosong, user belum login / token belum tersimpan",
                    isUploading = false,
                    progress = 0,
                )
                return@launch
            }

            val uniqueKey = "${container.fileName}_${getTimeMillis()}"

            var uploadedBytes = 0L
            var chunkIndex = 0

            var failedChunks = 0

            try {
                container.forEachChunk(chunkSize) { chunk ->

                    if (controller.isCancelled()) {
                        throw CancellationException("User cancelled")
                    }

                    controller.waitIfPaused()

                    val currentIndex = chunkIndex
                    println("VM_UPLOAD: sending chunk $currentIndex / ${totalChunks - 1}")

                    try {
                        uploadChunkUseCase.execute(
                            token = token,
                            fileName = container.fileName,
                            uniqueKey = uniqueKey,
                            chunkIndex = currentIndex,
                            totalChunks = totalChunks,
                            chunk = chunk,
                        ).collect { dataState ->

                            when (dataState) {
                                is DataState.Loading -> {
                                }

                                is DataState.Data -> {
                                    val apiResponse: ChunkResponse? = dataState.data
                                    println("VM_UPLOAD: chunk $currentIndex OK -> $apiResponse")

                                    uploadedBytes += chunk.size
                                    val percent = ((uploadedBytes.toFloat() / totalBytes.toFloat()) * 100).toInt()

                                    val newProgress = percent.coerceIn(0, 100)

                                    val current = _state.value
                                    _state.value = if (current.isPaused) {
                                        current.copy(
                                            progress = newProgress
                                        )
                                    } else {
                                        current.copy(
                                            progress = newProgress,
                                            status = "Uploading...",
                                            isUploading = true
                                        )
                                    }
                                }

                                is DataState.Response -> {
                                    println("VM_UPLOAD: chunk $currentIndex failed -> ${dataState.uiComponent}")
                                    failedChunks += 1

                                    _state.value = _state.value.copy(
                                        status = "Beberapa chunk gagal (chunk $currentIndex), lanjut upload...",
                                        isUploading = true
                                    )
                                }

                                is DataState.NetworkStatus -> {
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        failedChunks += 1
                        println("VM_UPLOAD: Exception di chunk $currentIndex -> ${e.message}")

                        _state.value = _state.value.copy(
                            status = "Exception di chunk $currentIndex, lanjut upload...",
                            isUploading = true
                        )
                    }

                    chunkIndex++
                }

                val finalStatus = if (failedChunks == 0) {
                    "Upload completed!"
                } else {
                    "Upload selesai, tapi $failedChunks chunk gagal dikirim"
                }

                _state.value = _state.value.copy(
                    status = finalStatus,
                    isUploading = false,
                    progress = 100,
                    uniqueKey = uniqueKey
                )

            } catch (e: CancellationException) {
                _state.value = _state.value.copy(
                    status = "Cancelled",
                    isUploading = false
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = _state.value.copy(
                    status = "Error: ${e.message}",
                    isUploading = false
                )
            }
        }
    }

    fun pause() {
        controller.pause()
        _state.value = _state.value.copy(status = "Paused", isPaused = true)

    }

    fun resume() {
        controller.resume()
        _state.value = _state.value.copy(status = "Resumed", isPaused = false)
    }

    fun cancel() {
        controller.cancel()
        uploadJob?.cancel()
        _state.value = _state.value.copy(
            status = "Cancelled",
            isUploading = false,
            isPaused = false
        )
    }

    fun dismissLimitDialog() {
        _state.value = _state.value.copy(
            showLimitDialog = false,
            limitDialogMessage = ""
        )
    }

}

