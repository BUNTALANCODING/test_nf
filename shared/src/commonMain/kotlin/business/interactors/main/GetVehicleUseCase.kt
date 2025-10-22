package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.responses.GetVehicleDTO

class GetVehicleUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<Unit, List<GetVehicleDTO>, List<GetVehicleDTO>>(appDataStoreManager) {
    override suspend fun run(
        params: Unit,
        token: String
    ): MainGenericResponse<List<GetVehicleDTO>> = service.getVehicle(
        token = token
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<List<GetVehicleDTO>>?): List<GetVehicleDTO>? = apiResponse?.result

    override val progressBarType = ProgressBarState.FullScreenLoading
    override val needNetworkState = false
    override val createException = false
    override val checkToken = true
    override val showDialog = true
}