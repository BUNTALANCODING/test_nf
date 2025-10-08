package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.request.UpdateProfileRequestDTO

class UpdateProfileUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<UpdateProfileRequestDTO, Boolean, Boolean>(appDataStoreManager) {

    override suspend fun run(
        params: UpdateProfileRequestDTO,
        token: String
    ) = service.updateProfile(
        token = token,
        requestDTO = params
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<Boolean>?) = apiResponse?.status

    override val progressBarType = ProgressBarState.ButtonLoading
    override val needNetworkState = false
    override val createException = false
    override val checkToken = true
    override val showDialog = true
}