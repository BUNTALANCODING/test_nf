package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.responses.UploadPetugasDTO

class UploadPetugasUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<UploadPetugasUseCase.Params, UploadPetugasDTO, UploadPetugasDTO>(appDataStoreManager) {

    data class Params(
        val officerImage: ByteArray?,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Params

            if (officerImage != null) {
                if (other.officerImage == null) return false
                if (!officerImage.contentEquals(other.officerImage)) return false
            } else if (other.officerImage != null) return false

            return true
        }

        override fun hashCode(): Int {
            return officerImage?.contentHashCode() ?: 0
        }
    }

    override suspend fun run(
        params: Params,
        token: String
    ): MainGenericResponse<UploadPetugasDTO> = service.uploadFotoPetugas(
        token = token,
        officerImage = params.officerImage
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<UploadPetugasDTO>?): UploadPetugasDTO? = apiResponse?.result

    override val progressBarType = ProgressBarState.FullScreenLoading
    override val needNetworkState = false
    override val createException = false
    override val checkToken = false
    override val showDialog = true
}