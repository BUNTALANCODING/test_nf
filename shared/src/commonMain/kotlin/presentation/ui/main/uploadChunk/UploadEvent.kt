package presentation.ui.main.uploadChunk

import business.core.ViewEvent
import presentation.ui.main.riwayat.viewmodel.RiwayatEvent

sealed class UploadEvent : ViewEvent {

    data class UploadFile(
        val uniqueKey: String,
        val file: ByteArray,
        val chunkIndex: Int,
        val totalChunks: Int
    ) : UploadEvent()

    data object CancelUpload : UploadEvent()
    data object PauseUpload : UploadEvent()
    data object ResumeUpload : UploadEvent()
}