package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.responses.ItemsItemLoadCard

class LoadCardUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<Unit, List<ItemsItemLoadCard>, List<ItemsItemLoadCard>>(appDataStoreManager) {
    override suspend fun run(
        params: Unit,
        token: String
    ): MainGenericResponse<List<ItemsItemLoadCard>> = service.loadCard(
        token = token,
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<List<ItemsItemLoadCard>>?): List<ItemsItemLoadCard>? = apiResponse?.result

    override val progressBarType = ProgressBarState.LoadingWithLogo
    override val needNetworkState = false
    override val createException = false
    override val checkToken = true
    override val showDialog = true
}