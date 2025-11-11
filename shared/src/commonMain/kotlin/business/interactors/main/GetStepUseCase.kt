package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.request.GetStepRequestDTO
import business.datasource.network.main.responses.GetStepDTO

class GetStepUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<GetStepRequestDTO, GetStepDTO, GetStepDTO>(appDataStoreManager) {
    override suspend fun run(
        params: GetStepRequestDTO,
        token: String
    ): MainGenericResponse<GetStepDTO> = service.getStep(
        token = token,
        params = params
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<GetStepDTO>?): GetStepDTO? = apiResponse?.result

    override val progressBarType = ProgressBarState.FullScreenLoading
    override val needNetworkState = false
    override val createException = false
    override val checkToken = true
    override val showDialog = true
}