package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.responses.ProfileDTO
import business.domain.main.Profile

class GetProfileUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<Unit, ProfileDTO, ProfileDTO>(appDataStoreManager) {

    override suspend fun run(params: Unit, token: String) = service.getProfile(
        token = token
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<ProfileDTO>?) =
        apiResponse?.result

    override val progressBarType = ProgressBarState.LoadingWithLogo
    override val needNetworkState = true
    override val createException = true
    override val checkToken = true
    override val showDialog = true
}