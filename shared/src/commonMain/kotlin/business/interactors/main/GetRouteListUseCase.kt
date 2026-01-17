package business.interactor


import business.datasource.network.main.MainService
import business.datasource.network.main.dto.request.RouteListRequest
import business.datasource.network.main.dto.response.RouteListResponse

class GetRouteListUseCase(
    private val service: MainService
) {
    suspend operator fun invoke(query: String): List<RouteListResponse> {
        val q = query.trim()

        val res = service.routeList(
            RouteListRequest(value = q)
        )

        if (res.status != true || res.data == null) {
            throw IllegalStateException(res.message ?: "Gagal memuat rute")
        }
        return res.data!!
    }
}
