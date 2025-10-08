package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.request.FerryRequestDTO
import business.datasource.network.main.responses.FerryDTO

class GetAvailableFerryUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<GetAvailableFerryUseCase.Params, List<FerryDTO>, List<FerryDTO>>(appDataStoreManager) {

    data class Params(
        val req: FerryRequestDTO
    )

    override suspend fun run(params: Params, token: String) = service.getFerry(
        token = token,
        requestDTO = params.req
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<List<FerryDTO>>?) =
        apiResponse?.result

    override val progressBarType = ProgressBarState.ButtonLoading
    override val needNetworkState = true
    override val createException = true
    override val checkToken = true
    override val showDialog = true
}