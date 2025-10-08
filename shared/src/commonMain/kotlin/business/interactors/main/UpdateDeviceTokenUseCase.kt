package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.request.UpdateDeviceTokenRequestDTO

class UpdateDeviceTokenUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<UpdateDeviceTokenRequestDTO, String, String>(appDataStoreManager) {

    override suspend fun run(params: UpdateDeviceTokenRequestDTO, token: String) = service.updateDeviceToken(
        token = token,
        requestDTO = params
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<String>?) =
        apiResponse?.result

    override val progressBarType = ProgressBarState.LoadingWithLogo
    override val needNetworkState = true
    override val createException = true
    override val checkToken = true
    override val showDialog = false
}