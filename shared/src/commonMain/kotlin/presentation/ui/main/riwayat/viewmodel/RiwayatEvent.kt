package presentation.ui.main.riwayat.viewmodel

import business.core.ViewEvent
import presentation.ui.main.home.view_model.HomeEvent

sealed class RiwayatEvent : ViewEvent {

    data object GetListRiwayat: RiwayatEvent()
    data class UpdateStatusRiwayat(val value: Int): RiwayatEvent()
    data object PreviewBA : RiwayatEvent()
    data class OnUpdateRampcheckId(val value: Int) : RiwayatEvent()

    data object ShowSendEmailDialog : RiwayatEvent()
    data object HideSendEmailDialog : RiwayatEvent()

    data class SendEmailBA(val emails: List<String>, val sendToMyEmail: Boolean) : RiwayatEvent()

    data object UpdateMyEmail : RiwayatEvent()

    data object HideSuccessEmailDialog : RiwayatEvent()








}