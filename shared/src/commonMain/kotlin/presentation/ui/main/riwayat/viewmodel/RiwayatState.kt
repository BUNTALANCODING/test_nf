package presentation.ui.main.riwayat.viewmodel

import business.core.NetworkState
import business.core.ProgressBarState
import business.core.UIComponentState
import business.core.ViewState
import business.datasource.network.common.JAlertResponse
import business.datasource.network.main.responses.GetVehicleDTO
import business.datasource.network.main.responses.HistoryRampcheckDTO
import business.datasource.network.main.responses.HistoryRampcheckDTOItem
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class RiwayatState (
    val listRiwayat: List<HistoryRampcheckDTOItem> = listOf(),
    val statusRiwayat: Int = 1,
    val urlPreviewBA : String = "",
    val rampcheckId : Int = 0,

    val errorResult: JAlertResponse = JAlertResponse(),
    val errorDialogState: UIComponentState = UIComponentState.Hide,
    val time: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val networkState: NetworkState = NetworkState.Good,
    val isEmailSent: Boolean = false,


    val isSendEmailDialogOpen: Boolean = false


) : ViewState
