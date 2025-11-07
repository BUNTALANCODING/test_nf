package business.interactors.main

import business.constants.AUTHORIZATION_BEARER_TOKEN
import business.constants.DataStoreKeys
import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.DataState
import business.core.ProgressBarState
import business.core.UIComponent
import business.datasource.network.common.JRNothing
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.request.SubmitSignatureRequestDTO
import business.datasource.network.main.responses.GetLocationDTO
import business.datasource.network.main.responses.SubmitSignatureDTO
import business.util.handleUseCaseException
import business.datasource.network.splash.SplashService
import business.datasource.network.splash.responses.LoginDTO
import business.datasource.network.splash.responses.LoginRequestDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SubmitSignatureUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<SubmitSignatureRequestDTO, SubmitSignatureDTO, SubmitSignatureDTO>(appDataStoreManager) {
    override suspend fun run(
        params: SubmitSignatureRequestDTO,
        token: String
    ): MainGenericResponse<SubmitSignatureDTO> = service.submitSignature(
        token = token,
        params = params
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<SubmitSignatureDTO>?): SubmitSignatureDTO? = apiResponse?.result

    override val progressBarType = ProgressBarState.FullScreenLoading
    override val needNetworkState = false
    override val createException = false
    override val checkToken = true
    override val showDialog = true
}