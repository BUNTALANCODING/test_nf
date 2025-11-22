package business.interactors.main

import business.core.AppDataStore
import business.core.BaseUseCase
import business.core.ProgressBarState
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.MainService
import business.datasource.network.main.request.SubmitQuestionsRequestDTO

class SubmitQuestionUseCase(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
) : BaseUseCase<SubmitQuestionsRequestDTO, String, String>(appDataStoreManager) {
    override suspend fun run(
        params: SubmitQuestionsRequestDTO,
        token: String
    ): MainGenericResponse<String> = service.submitQuestion(
        token = token,
        requestDTO = params
    )

    override fun mapApiResponse(apiResponse: MainGenericResponse<String>?): String? = apiResponse?.result

    override val progressBarType = ProgressBarState.ButtonLoading
    override val needNetworkState = false
    override val createException = false
    override val checkToken = true
    override val showDialog = true
}