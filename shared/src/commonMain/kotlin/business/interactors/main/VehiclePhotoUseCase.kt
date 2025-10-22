package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.request.VehiclePhotoRequestDTO
import business.datasource.network.main.responses.VehiclePhotoDTO

class VehiclePhotoUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<VehiclePhotoRequestDTO, VehiclePhotoDTO, VehiclePhotoDTO>(appDataStoreManager) {
    override suspend fun run(
        params: VehiclePhotoRequestDTO,
        token: String
    ): MainGenericResponse<VehiclePhotoDTO> = service.vehiclePhoto(
        token = token,
        request = params
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<VehiclePhotoDTO>?): VehiclePhotoDTO? = apiResponse?.result

    override val progressBarType = ProgressBarState.ButtonLoading
    override val needNetworkState = false
    override val createException = false
    override val checkToken = true
    override val showDialog = true
}