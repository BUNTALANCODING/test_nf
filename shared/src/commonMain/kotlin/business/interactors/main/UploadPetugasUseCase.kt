package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.request.UploadPetugasRequestDTO
import business.datasource.network.main.responses.UploadPetugasDTO

class UploadPetugasUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<UploadPetugasRequestDTO, UploadPetugasDTO, UploadPetugasDTO>(appDataStoreManager) {


    override suspend fun run(
        params: UploadPetugasRequestDTO,
        token: String
    ): MainGenericResponse<UploadPetugasDTO> = service.uploadFotoPetugas(
        token = token,
        request = params
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<UploadPetugasDTO>?): UploadPetugasDTO? = apiResponse?.result

    override val progressBarType = ProgressBarState.ButtonLoading
    override val needNetworkState = false
    override val createException = false
    override val checkToken = false
    override val showDialog = true
}