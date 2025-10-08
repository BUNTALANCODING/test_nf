package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService

class GetNotificationUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<Unit, List<String>, String>(appDataStoreManager) {

    override suspend fun run(params: Unit, token: String) = service.getNotification(
        token = token
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<List<String>>?) =
        apiResponse?.result.toString()

    override val progressBarType = ProgressBarState.LoadingWithLogo
    override val needNetworkState = true
    override val createException = true
    override val checkToken = true
    override val showDialog = true
}