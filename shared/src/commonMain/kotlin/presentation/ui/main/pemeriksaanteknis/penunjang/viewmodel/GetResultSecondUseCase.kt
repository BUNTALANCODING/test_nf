package presentation.ui.main.pemeriksaanteknis.penunjang.viewmodel

import business.datasource.network.main.MainService
import business.datasource.network.main.responses.HasilTeknisDTO

class GetResultSecondUseCase(
    private val service: MainService
) {
    suspend fun execute(
        token: String,
        uniqueKey: String
    ): HasilTeknisDTO {
        return service.getResultSecond(token, uniqueKey)
    }
}