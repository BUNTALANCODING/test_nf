package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.request.CheckStatusRequestDTO
import business.datasource.network.main.responses.CheckStatusDTO

class CheckStatusUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<CheckStatusRequestDTO, CheckStatusDTO, CheckStatusDTO>(appDataStoreManager) {

    override suspend fun run(params: CheckStatusRequestDTO, token: String) = service.checkStatus(
        token = token,
        requestDTO = params
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<CheckStatusDTO>?) =
        apiResponse?.result

    override val progressBarType = ProgressBarState.ButtonLoading
    override val needNetworkState = true
    override val createException = true
    override val checkToken = true
    override val showDialog = true
}