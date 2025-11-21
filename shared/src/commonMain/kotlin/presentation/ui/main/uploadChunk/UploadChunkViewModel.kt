package presentation.ui.main.uploadChunk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import business.constants.DataStoreKeys
import business.core.AppDataStore
import business.core.BaseViewModel
import business.core.DataState
import business.core.NetworkState
import business.datasource.network.main.MainService
import business.datasource.network.main.request.UploadChunkRequestDTO
import business.interactors.main.UploadChunkUseCase
import common.FileContainer
import common.NetworkClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import logger.Logger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
//class UploadChunkViewModel(
//    private val uploadChunkUseCase: UploadChunkUseCase,
//    private val mainService: MainService
//) : BaseViewModel<UploadEvent, UploadState, UploadAction>() {
//
//    private var uploadJob: Job? = null
//    private var isPaused = false
//    private var isCanceled = false
//
//    override fun setInitialState() = UploadState()
//
//    override fun onTriggerEvent(event: UploadEvent) {
//        when(event) {
//            is UploadEvent.UploadFile -> startUpload(event)
//            is UploadEvent.PauseUpload -> isPaused = true
//            is UploadEvent.ResumeUpload -> isPaused = false
//            is UploadEvent.CancelUpload -> {
//                isCanceled = true
//                uploadJob?.cancel()
//                setState { copy(isUploading = false, progressPercent = 0f) }
//            }
//        }
//    }
//
//    private fun startUpload(event: UploadEvent.UploadFile) {
//        isPaused = false
//        isCanceled = false
//
//        setState {
//            copy(
//                isUploading = true,
//                totalChunks = event.totalChunks,
//                lastUploadedChunk = 0,
//                progressPercent = 0f,
//                errorMessage = ""
//            )
//        }
//
//        val chunkSize = 1024 * 1024 // 1MB
//
//        uploadJob = viewModelScope.launch {
//            for (chunkIndex in 1..event.totalChunks) {
//
//                if (isCanceled) {
//                    println("DEBUG_UPLOAD: Upload canceled at chunk $chunkIndex")
//                    break
//                }
//
//                // Pause handling
//                while (isPaused) {
//                    println("DEBUG_UPLOAD: Upload paused at chunk $chunkIndex")
//                    delay(500)
//                    if (isCanceled) break
//                }
//                if (isCanceled) break
//
//                // Slice chunk
//                val start = (chunkIndex - 1) * chunkSize
//                val end = minOf(chunkIndex * chunkSize, event.file.size)
//                val chunk = event.file.copyOfRange(start, end)
//
//                val request = UploadChunkRequestDTO(
//                    uniqueKey = event.uniqueKey,
//                    file = chunk,
//                    chunkIndex = chunkIndex,
//                    totalChunks = event.totalChunks
//                )
//
//                println("DEBUG_UPLOAD: Uploading chunk $chunkIndex / ${event.totalChunks}...")
//
//                try {
//                    // Sequentially collect Flow for this chunk
//                    var chunkSuccess = false
//                    uploadChunkUseCase.execute(request).collect { dataState ->
//                        println("DEBUG_UPLOAD: Chunk $chunkIndex emitted -> $dataState")
//                        when (dataState) {
//                            is DataState.Loading -> setState { copy(progressBarState = dataState.progressBarState) }
//                            is DataState.Data -> {
//                                chunkSuccess = true
//                                setState {
//                                    copy(
//                                        lastUploadedChunk = chunkIndex,
//                                        progressPercent = chunkIndex.toFloat() / event.totalChunks.toFloat()
//                                    )
//                                }
//                            }
//                            is DataState.Response -> {
//                                println("DEBUG_UPLOAD: Chunk $chunkIndex failed: ${dataState.uiComponent}")
//                                setError { dataState.uiComponent }
//                                chunkSuccess = false
//                            }
//                            is DataState.NetworkStatus -> {
//                                if (dataState.networkState != NetworkState.Good) {
//                                    println("DEBUG_UPLOAD: Network issue at chunk $chunkIndex")
//                                    chunkSuccess = false
//                                }
//                            }
//                        }
//                    }
//
//                    if (!chunkSuccess) {
//                        println("DEBUG_UPLOAD: Stopping upload due to failure at chunk $chunkIndex")
//                        isCanceled = true
//                        setState { copy(isUploading = false, errorMessage = "Upload failed at chunk $chunkIndex") }
//                        break
//                    }
//
//                } catch (e: Exception) {
//                    println("DEBUG_UPLOAD: Exception at chunk $chunkIndex: ${e.message}")
//                    e.printStackTrace()
//                    isCanceled = true
//                    setState { copy(isUploading = false, errorMessage = "Exception at chunk $chunkIndex") }
//                    break
//                }
//            }
//
//            if (!isCanceled) {
//                println("DEBUG_UPLOAD: All chunks uploaded successfully!")
//                setState {
//                    copy(
//                        isUploading = false,
//                        progressPercent = 1f,
//                        lastUploadedChunk = event.totalChunks
//                    )
//                }
//            }
//        }
//    }
//}

class UploadViewModel(
    private val appDataStore: AppDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(UploadState())
    val state: StateFlow<UploadState> = _state

    private var fileContainer: FileContainer? = null
    private var controller = UploadController()
    private var uploadJob: Job? = null

    fun setFile(container: FileContainer) {
        this.fileContainer = container
        this.controller = UploadController()

        _state.value = _state.value.copy(
            fileName = container.fileName,
            status = "Ready to upload",
            progress = 0,
            isUploading = false
        )
    }

    fun startUploadInterior() {
        val container = fileContainer ?: return

        _state.value = _state.value.copy(
            status = "Uploading...",
            isUploading = true
        )


        uploadJob = viewModelScope.launch {

            val rawToken = appDataStore.readValue(DataStoreKeys.TOKEN) ?: ""

            if (rawToken.isEmpty()) {
                _state.value = _state.value.copy(
                    status = "Error: Not Logged In",
                    isUploading = false
                )
                return@launch
            }

            val result = NetworkClient.uploadVideoInterior(
                token = rawToken,
                container = container,
                controller = controller
            ) { percent ->
                _state.value = _state.value.copy(
                    progress = percent
                    // We do not update lastUploadedChunk/totalChunks
                    // because we decided not to track them in the NetworkClient
                )
            }

            _state.value = _state.value.copy(
                status = result,
                isUploading = false,
                progress = 100
            )
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
        _state.value = _state.value.copy(status = "Cancelled", isUploading = false)
    }
}
