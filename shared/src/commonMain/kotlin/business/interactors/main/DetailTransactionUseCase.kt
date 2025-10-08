package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.request.DetailTransactionRequestDTO
import business.datasource.network.main.responses.DetailTransaction

class DetailTransactionUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<DetailTransactionRequestDTO, DetailTransaction, DetailTransaction>(appDataStoreManager) {

    override suspend fun run(params: DetailTransactionRequestDTO, token: String) = service.getDetailTransaction(
        token = token,
        requestDTO = params
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<DetailTransaction>?) =
        apiResponse?.result

    override val progressBarType = ProgressBarState.LoadingWithLogo
    override val needNetworkState = true
    override val createException = true
    override val checkToken = true
    override val showDialog = true
}