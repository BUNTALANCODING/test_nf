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
import business.datasource.network.main.responses.GetLocationDTO
import business.util.handleUseCaseException
import business.datasource.network.splash.SplashService
import business.datasource.network.splash.responses.LoginDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetLocationUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<Unit, List<GetLocationDTO>, List<GetLocationDTO>>(appDataStoreManager) {

    override suspend fun run(params: Unit, token: String) = service.getLocation(
        token = token
    )
    override fun mapApiResponse(apiResponse: MainGenericResponse<List<GetLocationDTO>>?) = apiResponse?.result

    override val progressBarType = ProgressBarState.ButtonLoading
    override val needNetworkState = false
    override val createException = false
    override val checkToken = true
    override val showDialog = true
}