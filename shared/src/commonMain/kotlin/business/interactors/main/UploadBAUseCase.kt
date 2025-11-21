package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.request.UploadChunkRequestDTO
import business.datasource.network.main.responses.UploadChunkResponseDTO

class UploadChunkUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore
) : BaseUseCase<UploadChunkRequestDTO, UploadChunkResponseDTO, UploadChunkResponseDTO>(appDataStoreManager) {

    override suspend fun run(
        params: UploadChunkRequestDTO,
        token: String
    ): MainGenericResponse<UploadChunkResponseDTO>? {

        return service.uploadChunkFile(
            token = token,
            request = params
        )
    }

    override fun mapApiResponse(apiResponse: MainGenericResponse<UploadChunkResponseDTO>?): UploadChunkResponseDTO? =
        apiResponse?.result

    override val progressBarType = ProgressBarState.DialogLoading
    override val needNetworkState = false
    override val createException = false
    override val checkToken = true
    override val showDialog = false
}

