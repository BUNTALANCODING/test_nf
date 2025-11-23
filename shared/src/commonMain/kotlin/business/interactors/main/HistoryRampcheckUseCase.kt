package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.request.HistoryRampcheckRequestDTO
import business.datasource.network.main.responses.HistoryRampcheckDTO
import business.datasource.network.main.responses.HistoryRampcheckDTOItem

class HistoryRampcheckUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<HistoryRampcheckRequestDTO, List<HistoryRampcheckDTOItem>, List<HistoryRampcheckDTOItem>>(appDataStoreManager) {
    override suspend fun run(
        params: HistoryRampcheckRequestDTO,
        token: String
    ): MainGenericResponse<List<HistoryRampcheckDTOItem>> = service.historyRampcheck(
        token = token,
        params = params
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<List<HistoryRampcheckDTOItem>>?): List<HistoryRampcheckDTOItem>? = apiResponse?.result

    override val progressBarType = ProgressBarState.LoadingWithLogo
    override val needNetworkState = false
    override val createException = false
    override val checkToken = true
    override val showDialog = true
}