package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService

class ForgotPasswordUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<ForgotPasswordUseCase.Params, String, String>(appDataStoreManager) {

    data class Params(
        val email: String
    )

    override suspend fun run(
        params: Params,
        token: String
    ) = service.forgotPassword(
        token = token,
        email = params.email
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<String>?) =
        apiResponse?.result

    override val progressBarType = ProgressBarState.ButtonLoading
    override val needNetworkState = true
    override val createException = true
    override val checkToken = true
    override val showDialog = true
}