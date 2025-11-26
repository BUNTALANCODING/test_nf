package presentation.ui.main.pemeriksaanteknis.penunjang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import business.constants.DataStoreKeys
import business.core.AppDataStore
import business.core.DataState
import business.datasource.network.main.responses.ChunkResponse
import business.interactors.main.IdentifyPenunjangUseCase
import business.interactors.main.UploadChunkUseCase
import common.FileContainer
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import presentation.ui.main.uploadChunk.UploadController
import presentation.ui.main.uploadChunk.UploadState

class IdentifyPenunjangViewModel(
    private val identifyPenunjangUseCase: IdentifyPenunjangUseCase,
    private val appDataStore: AppDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(UploadState())
    val state: StateFlow<UploadState> = _state

    private var fileContainer: FileContainer? = null
    private var controller = UploadController()
    private var uploadJob: Job? = null

    fun setFile(container: FileContainer) {
        fileContainer = container
        controller = UploadController()

        _state.value = _state.value.copy(
            fileName = container.fileName,
            status = "Ready to upload",
            progress = 0,
            isUploading = false
        )
    }

    fun startUploadInterior() {
        val container = fileContainer ?: return

        // 🔍 CEK SIZE 0
        val totalBytes = container.size
        if (totalBytes <= 0L) {
            _state.value = _state.value.copy(
                status = "File kosong (0 byte), tidak bisa diupload",
                isUploading = false,
                progress = 0
            )
            return
        }

        // 🔍 LIMIT 1GB
        val oneGbInBytes = 1_073_741_824L // 1GB = 1024^3
        if (totalBytes > oneGbInBytes) {
            _state.value = _state.value.copy(
                status = "Ukuran file melebihi 1 GB",
                isUploading = false,
                progress = 0
            )
            return
        }

        // cancel upload lama jika masih jalan
        uploadJob?.cancel()

        _state.value = _state.value.copy(
            status = "Uploading...",
            isUploading = true,
            progress = 0
        )

        uploadJob = viewModelScope.launch {

            // 🔑 AMBIL TOKEN DARI DATASTORE
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

            val chunkSize = 5_000_000 // 1 MB
            val totalChunks = ((totalBytes + chunkSize - 1) / chunkSize).toInt()

            val uniqueKey = "${container.fileName}_${getTimeMillis()}"

            var uploadedBytes = 0L
            var chunkIndex = 0 // 0-based

            // cuma buat statistik, tidak dipakai untuk stop loop
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
                        identifyPenunjangUseCase.execute(
                            token = token,               // kirim token ke usecase
                            fileName = container.fileName,
                            uniqueKey = uniqueKey,
                            chunkIndex = currentIndex,
                            totalChunks = totalChunks,
                            chunk = chunk
                        ).collect { dataState ->

                            when (dataState) {
                                is DataState.Loading -> {
                                    // optional: global loading
                                }

                                is DataState.Data -> {
                                    val apiResponse: ChunkResponse? = dataState.data
                                    println("VM_UPLOAD: chunk $currentIndex OK -> $apiResponse")

                                    // tetap naikkan progress walaupun status di response false
                                    uploadedBytes += chunk.size
                                    val percent = ((uploadedBytes.toFloat() / totalBytes.toFloat()) * 100).toInt()

                                    _state.value = _state.value.copy(
                                        progress = percent.coerceIn(0, 100),
                                        status = "Uploading...",
                                        isUploading = true
                                    )
                                }

                                is DataState.Response -> {
                                    // ada error dari server (timeout, dll)
                                    println("VM_UPLOAD: chunk $currentIndex failed -> ${dataState.uiComponent}")
                                    failedChunks += 1

                                    // TIDAK STOP, TIDAK SET isUploading = false
                                    // cukup tulis status sementara
                                    _state.value = _state.value.copy(
                                        status = "Beberapa chunk gagal (chunk $currentIndex), lanjut upload...",
                                        isUploading = true
                                    )
                                }

                                is DataState.NetworkStatus -> {
                                    // optional: bisa log disini
                                }
                            }
                        }
                    } catch (e: Exception) {
                        // Error per chunk (misal timeout dari Ktor)
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

                // SELESAI SEMUA CHUNK
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
        _state.value = _state.value.copy(status = "Paused")
    }

    fun resume() {
        controller.resume()
        _state.value = _state.value.copy(status = "Resumed")
    }

    fun cancel() {
        controller.cancel()
        uploadJob?.cancel()
        _state.value = _state.value.copy(
            status = "Cancelled",
            isUploading = false
        )
    }
}