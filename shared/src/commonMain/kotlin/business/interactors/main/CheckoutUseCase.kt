package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.request.CheckoutRequestDTO
import business.datasource.network.main.responses.CheckoutDTO

class CheckoutUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<CheckoutRequestDTO, CheckoutDTO, CheckoutDTO>(appDataStoreManager) {

    override suspend fun run(params: CheckoutRequestDTO, token: String) = service.checkout(
        token = token,
        requestDTO = params
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<CheckoutDTO>?) =
        apiResponse?.result

    override val progressBarType = ProgressBarState.LoadingWithLogo
    override val needNetworkState = true
    override val createException = true
    override val checkToken = true
    override val showDialog = true
}