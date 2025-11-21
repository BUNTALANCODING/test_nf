package presentation.ui.main.riwayat.viewmodel

import business.core.BaseViewModel
import business.datasource.network.main.request.HistoryRampcheckRequestDTO
import business.datasource.network.main.request.PreviewBARequestDTO
import business.interactors.main.HistoryRampcheckUseCase
import business.interactors.main.PreviewBAUseCase

class RiwayatViewModel(
    private val getRiwayatUseCase: HistoryRampcheckUseCase,
    private val previewBAUseCase: PreviewBAUseCase,

    ) : BaseViewModel<RiwayatEvent, RiwayatState, RiwayatAction>()  {

    override fun setInitialState() = RiwayatState()

    override fun onTriggerEvent(event: RiwayatEvent) {
        when(event) {
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


        }
    }

    //FETCHING
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

    private fun onUpdateRampcheckId(value: Int){
        setState {
            copy(
                rampcheckId = value,
                urlPreviewBA = ""
            )
        }
    }


}