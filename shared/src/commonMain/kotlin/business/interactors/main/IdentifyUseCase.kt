package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.request.IdentifyRequestDTO
import business.datasource.network.main.responses.IdentifyDTOItem

class IdentifyUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<IdentifyRequestDTO, List<IdentifyDTOItem>, List<IdentifyDTOItem>>(appDataStoreManager) {
    override suspend fun run(
        params: IdentifyRequestDTO,
        token: String
    ): MainGenericResponse<List<IdentifyDTOItem>> = service.identity(
        token = token,
        params = params
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<List<IdentifyDTOItem>>?): List<IdentifyDTOItem>? = apiResponse?.result

    override val progressBarType = ProgressBarState.LoadingWithLogo
    override val needNetworkState = false
    override val createException = false
    override val checkToken = true
    override val showDialog = true
}