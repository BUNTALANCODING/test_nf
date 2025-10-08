package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.request.ChangePasswordRequestDTO
import business.datasource.network.main.request.UploadFileRequestDTO

class UploadFileUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<UploadFileRequestDTO, String, String>(appDataStoreManager) {

    override suspend fun run(params: UploadFileRequestDTO, token: String) = service.uploadFile(
        token = token,
        requestDTO = params
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<String>?) =
        apiResponse?.result

    override val progressBarType = ProgressBarState.LoadingWithLogo
    override val needNetworkState = true
    override val createException = true
    override val checkToken = true
    override val showDialog = true
}