package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.request.TransactionRequestDTO
import business.datasource.network.main.responses.TransactionDTO

class TransactionUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<TransactionRequestDTO, List<TransactionDTO>, List<TransactionDTO>>(appDataStoreManager) {

    override suspend fun run(
        params: TransactionRequestDTO,
        token: String
    ) = service.getTransaction(requestDTO = params, token = token)

    override fun mapApiResponse(apiResponse: MainGenericResponse<List<TransactionDTO>>?) =
        apiResponse?.result

    override val progressBarType = ProgressBarState.LoadingWithLogo
    override val needNetworkState = true
    override val createException = true
    override val checkToken = true
    override val showDialog = true
}