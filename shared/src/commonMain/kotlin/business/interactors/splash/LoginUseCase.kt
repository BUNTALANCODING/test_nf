package business.interactors.splash

import business.constants.AUTHORIZATION_BEARER_TOKEN
import business.constants.DataStoreKeys
import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.DataState
import business.core.ProgressBarState
import business.core.UIComponent
import business.datasource.network.common.JRNothing
import business.datasource.network.common.MainGenericResponse
import business.util.handleUseCaseException
import business.datasource.network.splash.SplashService
import business.datasource.network.splash.responses.LoginDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginUseCase(
    private val service: SplashService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<LoginUseCase.Params, LoginDTO, LoginDTO>(appDataStoreManager) {

    data class Params(
        val email: String,
        val password: String,
    )

    override suspend fun run(params: Params, token: String): MainGenericResponse<LoginDTO> {
        val apiResponse = service.login(params.email, params.password)

        val result = apiResponse.result

        if (result?.token != null) {
            appDataStoreManager.setValue(
                DataStoreKeys.TOKEN,
                AUTHORIZATION_BEARER_TOKEN + result.token
            )
            /*appDataStoreManager.setValue(
                DataStoreKeys.EMAIL,
                params.email
            )*/
        }

        return apiResponse
    }

    override fun mapApiResponse(apiResponse: MainGenericResponse<LoginDTO>?) = apiResponse?.result

    override val progressBarType = ProgressBarState.ButtonLoading
    override val needNetworkState = false
    override val createException = false
    override val checkToken = false
    override val showDialog = true
}