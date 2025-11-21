package presentation.ui.main.uploadChunk

import business.core.NetworkState
import business.core.ProgressBarState
import business.core.UIComponentState
import business.core.ViewState
import business.datasource.network.common.JAlertResponse
import business.datasource.network.main.responses.HistoryRampcheckDTOItem
import business.datasource.network.main.responses.UploadChunkResponseDTO
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class UploadState (
    val resultUploadChunk: UploadChunkResponseDTO = UploadChunkResponseDTO(),
    val lastUploadedChunk: Int = 0,
    val totalChunks: Int = 0,
    val isUploading: Boolean = false,
    val fileName: String = "",
    val errorMessage: String = "",
    val progress: Int = 0,
    val status: String = "Ready",


    val errorResult: JAlertResponse = JAlertResponse(),
    val errorDialogState: UIComponentState = UIComponentState.Hide,
    val time: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val networkState: NetworkState = NetworkState.Good,
) : ViewState