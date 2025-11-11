package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.request.SendEmailBARequestDTO
import business.datasource.network.main.responses.SendEmailBADTO

class SendEmailBAUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<SendEmailBARequestDTO, SendEmailBADTO, SendEmailBADTO>(appDataStoreManager) {
    override suspend fun run(
        params: SendEmailBARequestDTO,
        token: String
    ): MainGenericResponse<SendEmailBADTO> = service.sendEmailBA(
        token = token,
        params = params
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<SendEmailBADTO>?): SendEmailBADTO? = apiResponse?.result

    override val progressBarType = ProgressBarState.FullScreenLoading
    override val needNetworkState = false
    override val createException = false
    override val checkToken = true
    override val showDialog = true
}