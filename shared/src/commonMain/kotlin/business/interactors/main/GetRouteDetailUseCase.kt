package business.interactor


import business.datasource.network.main.MainService
import business.datasource.network.main.dto.request.RouteDetailRequest
import business.datasource.network.main.dto.response.RouteDetailResponse

class GetRouteDetailUseCase(
    private val service: MainService
) {
    suspend operator fun invoke(corridorCode: String): RouteDetailResponse {
        val res = service.routeDetail(RouteDetailRequest(corridorCode = corridorCode))
        if (res.status != true || res.data == null) {
            throw IllegalStateException(res.message ?: "Gagal memuat detail rute")
        }
        return res.data!!
    }
}
