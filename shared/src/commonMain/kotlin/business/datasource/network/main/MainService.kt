package business.datasource.network.main

import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.dto.request.GoogleLoginRequest
import business.datasource.network.main.dto.request.MapsRouteRequest
import business.datasource.network.main.dto.request.NearestHalteRequest
import business.datasource.network.main.dto.request.RouteDetailRequest
import business.datasource.network.main.dto.request.RouteListRequest
import business.datasource.network.main.dto.request.TripRequest
import business.datasource.network.main.dto.response.GoogleLoginResponse
import business.datasource.network.main.dto.response.MapsRouteResponse
import business.datasource.network.main.dto.response.NearestHalteResponse
import business.datasource.network.main.dto.response.RouteDetailResponse
import business.datasource.network.main.dto.response.RouteListResponse
import business.datasource.network.main.dto.response.TripResponse


interface MainService {

    companion object {
        const val SEARCH_FILTER = "search/filter"
        const val SEARCH = "search"
        const val HOME = "home"
        const val PROFILE = "profile"
        const val NOTIFICATIONS = "notifications"
        const val LIST_BUS = "list_bus"
        const val DETAIL_TRANSACTION = "trx_detail"
        const val SUMMARY_TRANSACTION = "trx_summary"
        const val HISTORY_ATTENDANCE = "attendance"
        const val ATTENDANCE = "attendance"
        const val HISTORY_RIT = "history_rit"
        const val RIT = "rit"
        const val ATTENDANCE_QR = "attendance_qr"
        const val TICKET_TRANSACTION = "trx_detail"
        const val HISTORY_QRIS = "history_qris"
        const val QR_BUS = "init_qris"
        const val GOOGLE_LOGIN = "google-login"
        const val NEAREST_HALTE = "tracker/nearest_halte"
        const val ROUTE_LIST = "tracker/route_list"
        const val ROUTE_DETAIL = "tracker/detail_route"
        const val TRIP = "tracker/trip"
        const val MAPS_ROUTE = "tracker/maps_route"



    }
    suspend fun loginWithGoogle(request: GoogleLoginRequest): MainGenericResponse<GoogleLoginResponse>

    suspend fun nearestHalte(
        request: NearestHalteRequest
    ): MainGenericResponse<NearestHalteResponse>

    suspend fun routeList(
        request: RouteListRequest
    ): MainGenericResponse<List<RouteListResponse>>

    suspend fun routeDetail(
        request: RouteDetailRequest
    ): MainGenericResponse<RouteDetailResponse>

    suspend fun trip(
        request: TripRequest
    ): MainGenericResponse<TripResponse>

    suspend fun mapsRoute(
        request: MapsRouteRequest
    ): MainGenericResponse<MapsRouteResponse>

}
