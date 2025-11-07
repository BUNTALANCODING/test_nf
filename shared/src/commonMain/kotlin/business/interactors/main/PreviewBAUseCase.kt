package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.request.PreviewBARequestDTO
import business.datasource.network.main.responses.PreviewBADTO

class PreviewBAUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<PreviewBARequestDTO, PreviewBADTO, PreviewBADTO>(appDataStoreManager) {
    override suspend fun run(
        params: PreviewBARequestDTO,
        token: String
    ): MainGenericResponse<PreviewBADTO> = service.previewBA(
        token = token,
        params = params
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<PreviewBADTO>?): PreviewBADTO? = apiResponse?.result

    override val progressBarType = ProgressBarState.FullScreenLoading
    override val needNetworkState = false
    override val createException = false
    override val checkToken = true
    override val showDialog = true
}