package presentation.ui.main.riwayat.viewmodel

import androidx.lifecycle.viewModelScope
import business.constants.DataStoreKeys
import business.core.AppDataStore
import business.core.AppDataStoreManager
import business.core.BaseViewModel
import business.datasource.network.main.request.HistoryRampcheckRequestDTO
import business.datasource.network.main.request.PreviewBARequestDTO
import business.datasource.network.main.request.SendEmailBARequestDTO
import business.interactors.main.HistoryRampcheckUseCase
import business.interactors.main.PreviewBAUseCase
import business.interactors.main.SendEmailBAUseCase
import common.PdfDownloader
import kotlinx.coroutines.launch

class RiwayatViewModel(
    private val getRiwayatUseCase: HistoryRampcheckUseCase,
    private val previewBAUseCase: PreviewBAUseCase,
    private val sendEmailBAUseCase: SendEmailBAUseCase,
    private val appDataStoreManager: AppDataStore,


    ) : BaseViewModel<RiwayatEvent, RiwayatState, RiwayatAction>() {

    override fun setInitialState() = RiwayatState()

    override fun onTriggerEvent(event: RiwayatEvent) {
        when (event) {
            is RiwayatEvent.GetListRiwayat -> {
                getRiwayat()
            }

            is RiwayatEvent.UpdateStatusRiwayat -> {
                onUpdateStatusRiwayat(event.value)
            }

            is RiwayatEvent.OnUpdateRampcheckId -> {
                onUpdateRampcheckId(event.value)
            }

            is RiwayatEvent.PreviewBA -> {
                previewBA()
            }

            is RiwayatEvent.SendEmailBA -> {
                sendEmailBA(event.emails, event.sendToMyEmail)
            }

            is RiwayatEvent.ShowSendEmailDialog -> {
                setState { copy(isSendEmailDialogOpen = true) }
            }

            is RiwayatEvent.HideSendEmailDialog -> {
                setState { copy(isSendEmailDialogOpen = false) }
            }

            is RiwayatEvent.UpdateMyEmail -> {
                setStateValue()
            }

            is RiwayatEvent.HideSuccessEmailDialog -> {
                setState { copy(isSuccessEmailDialogOpen = false) }
            }


        }
    }


    private fun sendEmailBA(emails: List<String>, sendToMyEmail: Boolean) {
        val myEmail = state.value.myEmail

        val finalEmails = if (sendToMyEmail && myEmail.isNotBlank()) {
            emails + myEmail
        } else {
            emails
        }

        executeUseCase(
            sendEmailBAUseCase.execute(
                params = SendEmailBARequestDTO(
                    rampcheckId = state.value.rampcheckId,
                    emails = finalEmails
                )
            ),
            onSuccess = { data, status, code ->
                if (status== true && data != null) {
                    setState {
                        copy(
                            isSendEmailDialogOpen = false,
                            isSuccessEmailDialogOpen = true
                        )
                    }
                } else {
                    setState {
                        copy(
                            isSendEmailDialogOpen = false
                        )
                    }
                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            }
        )
    }



    private fun getRiwayat() {
        executeUseCase(
            getRiwayatUseCase.execute(
                params = HistoryRampcheckRequestDTO(
                    status = state.value.statusRiwayat
                ),
            ),
            onSuccess = { data, status, code ->
                data?.let { historyList ->
                    setState {
                        copy(
                            listRiwayat = historyList
                        )
                    }

                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            },
        )
    }

    private fun previewBA() {
        executeUseCase(
            previewBAUseCase.execute(
                params = PreviewBARequestDTO(
                    rampcheckId = state.value.rampcheckId
                )
            ),
            onSuccess = { data, status, code ->
                data?.let {
                    setState {
                        copy(urlPreviewBA = data.file_url ?: "")
                    }
                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            },
        )
    }


    private fun onUpdateStatusRiwayat(value: Int) {
        setState { copy(statusRiwayat = value) }
        getRiwayat()
    }

    private fun onUpdateRampcheckId(value: Int) {
        setState {
            copy(
                rampcheckId = value,
                urlPreviewBA = ""
            )
        }
    }

    private fun setStateValue() {
        viewModelScope.launch {
            val myEmail = appDataStoreManager.readValue(DataStoreKeys.OFFICER_EMAIL)
            setState {
                copy(myEmail = myEmail ?: "")
            }

        }

    }
}