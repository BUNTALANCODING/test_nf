package business.interactors.main

import business.datasource.network.main.MainService
import business.datasource.network.main.responses.HasilTeknisDTO

class GetHasilTeknisUseCase(
    private val service: MainService
) {
    suspend fun execute(
        token: String,
        uniqueKey: String
    ): HasilTeknisDTO {
        return service.getInteriorResult(token, uniqueKey)
    }
}
