package business.interactor

import business.datasource.network.main.MainService
import business.datasource.network.main.dto.request.TripRequest
import business.datasource.network.main.dto.response.TripResponse

class GetTripUseCase(
    private val service: MainService
) {
    suspend operator fun invoke(routeCode: String, lat: String, long: String): TripResponse {
        val res = service.trip(TripRequest(routeCode, lat, long))
        if (!res.status!! || res.data == null) {
            throw Exception(res.message ?: "Gagal memuat trip")
        }
        return res.data!!
    }
}
