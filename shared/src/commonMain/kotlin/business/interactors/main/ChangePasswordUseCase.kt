package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.request.ChangePasswordRequestDTO

class ChangePasswordUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<ChangePasswordRequestDTO, String, String>(appDataStoreManager) {

    override suspend fun run(params: ChangePasswordRequestDTO, token: String) = service.changePassword(
        token = token,
        requestDTO = params
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<String>?) =
        apiResponse?.result

    override val progressBarType = ProgressBarState.ButtonLoading
    override val needNetworkState = true
    override val createException = true
    override val checkToken = true
    override val showDialog = true
}