package business.interactors.splash

import business.constants.AUTHORIZATION_BEARER_TOKEN
import business.constants.DataStoreKeys
import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.DataState
import business.core.ProgressBarState
import business.core.UIComponent
import business.datasource.network.common.MainGenericResponse
import business.util.handleUseCaseException
import business.datasource.network.splash.SplashService
import business.datasource.network.splash.responses.RegisterRequestDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RegisterUseCase(
    private val service: SplashService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<RegisterUseCase.Params, List<String>, String>(appDataStoreManager) {

    data class Params(
        val name: String,
        val email: String,
        val phone: String,
        val address: String,
        val password: String,
        val confirmPassword: String
    )

    override suspend fun run(params: Params, token: String): MainGenericResponse<List<String>> {
        val request = RegisterRequestDTO(
            name = params.name,
            email = params.email,
            phone = params.phone,
            address = params.address,
            password = params.password,
            confirmPassword = params.confirmPassword
        )
        val apiResponse = service.register(request)

        val result = apiResponse.result

        if (result != null) {
            appDataStoreManager.setValue(
                DataStoreKeys.TOKEN,
                AUTHORIZATION_BEARER_TOKEN + result
            )
            appDataStoreManager.setValue(
                DataStoreKeys.EMAIL,
                params.email
            )
        }
        return apiResponse
    }

    override fun mapApiResponse(apiResponse: MainGenericResponse<List<String>>?) = apiResponse?.result?.toString()

    override val progressBarType = ProgressBarState.ButtonLoading
    override val needNetworkState = false
    override val createException = false
    override val checkToken = false
    override val showDialog = true
}