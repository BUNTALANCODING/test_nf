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
import business.datasource.network.main.request.KIRCompareRequestDTO
import business.datasource.network.main.request.PlatKIRRequestDTO
import business.datasource.network.main.request.RampcheckStartRequestDTO
import business.datasource.network.main.responses.GetLocationDTO
import business.datasource.network.main.responses.KIRCompareDTO
import business.datasource.network.main.responses.PlatKIRDTO
import business.datasource.network.main.responses.RampcheckStartDTO
import business.util.handleUseCaseException
import business.datasource.network.splash.SplashService
import business.datasource.network.splash.responses.LoginDTO
import business.datasource.network.splash.responses.LoginRequestDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class KIRCompareUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<KIRCompareRequestDTO, KIRCompareDTO, KIRCompareDTO>(appDataStoreManager) {
    override suspend fun run(
        params: KIRCompareRequestDTO,
        token: String
    ): MainGenericResponse<KIRCompareDTO> = service.kirCompare(
        token = token,
        request = params
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<KIRCompareDTO>?): KIRCompareDTO? = apiResponse?.result

    override val progressBarType = ProgressBarState.ButtonLoading
    override val needNetworkState = false
    override val createException = false
    override val checkToken = true
    override val showDialog = true
}