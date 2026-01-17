package business.datasource.network.main

import business.constants.BASE_URL
import business.constants.DataStoreKeys
import business.core.AppDataStore
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
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class MainServiceImpl(
    private val client: HttpClient,
    private val dataStore: AppDataStore
) : MainService {

    private val APP_ID_VALUE = "QVBQLTAxOkRFUE9L"


    private fun HttpRequestBuilder.auth(token: String) {
        val value = if (token.startsWith("Bearer ")) token else "Bearer $token"
        header(HttpHeaders.Authorization, value)
    }

    override suspend fun loginWithGoogle(
        request: GoogleLoginRequest
    ): MainGenericResponse<GoogleLoginResponse> {
        return client.post("$BASE_URL${MainService.GOOGLE_LOGIN}") {
            contentType(ContentType.Application.Json)
            header("X-APP-ID", APP_ID_VALUE)
            setBody(request)
        }.body()
    }


    private suspend inline fun <reified T> getAuthed(
        token: String,
        path: String,
        query: HttpRequestBuilder.() -> Unit = {}
    ): MainGenericResponse<T> {
        return client.get(path) {
            auth(token)
            query()
        }.body()
    }

    private suspend inline fun <reified Req : Any, reified T> postAuthed(
        token: String,
        path: String,
        bodyObj: Req,
        extra: HttpRequestBuilder.() -> Unit = {}
    ): MainGenericResponse<T> {
        return client.post(path) {
            auth(token)
            contentType(ContentType.Application.Json)
            setBody(bodyObj)
            extra()
        }.body()
    }

    override suspend fun nearestHalte(
        request: NearestHalteRequest
    ): MainGenericResponse<NearestHalteResponse> {

        val token = dataStore.readValue(DataStoreKeys.TOKEN)?.trim().orEmpty()
        if (token.isBlank()) {
            return MainGenericResponse(
                status = false,
                code = "401",
                message = "Token kosong",
                data = null
            )
        }

        return client.post("$BASE_URL${MainService.NEAREST_HALTE}") {
            auth(token)
            header("X-APP-ID", APP_ID_VALUE)
            header(HttpHeaders.Accept, "application/json")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun routeList(
        request: RouteListRequest
    ): MainGenericResponse<List<RouteListResponse>> {

        val token = dataStore.readValue(DataStoreKeys.TOKEN)?.trim().orEmpty()
        if (token.isBlank()) {
            return MainGenericResponse(
                status = false,
                code = "401",
                message = "Token kosong",
                data = null
            )
        }

        return client.post("$BASE_URL${MainService.ROUTE_LIST}") {
            auth(token)
            header("X-APP-ID", APP_ID_VALUE)
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun routeDetail(
        request: RouteDetailRequest
    ): MainGenericResponse<RouteDetailResponse> {

        val token = dataStore.readValue(DataStoreKeys.TOKEN)?.trim().orEmpty()
        if (token.isBlank()) {
            return MainGenericResponse(
                status = false,
                code = "401",
                message = "Token kosong",
                data = null
            )
        }

        return client.post("$BASE_URL${MainService.ROUTE_DETAIL}") {
            auth(token)
            header("X-APP-ID", APP_ID_VALUE)
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun trip(
        request: TripRequest
    ): MainGenericResponse<TripResponse> {
        val token = dataStore.readValue(DataStoreKeys.TOKEN)?.trim().orEmpty()
        if (token.isBlank()) {
            return MainGenericResponse(
                status = false,
                code = "401",
                message = "Token kosong",
                data = null
            )
        }

        return client.post("$BASE_URL${MainService.TRIP}") {
            auth(token)
            header("X-APP-ID", APP_ID_VALUE)
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun mapsRoute(
        request: MapsRouteRequest
    ): MainGenericResponse<MapsRouteResponse> {

        val token = dataStore.readValue(DataStoreKeys.TOKEN)?.trim().orEmpty()
        if (token.isBlank()) {
            return MainGenericResponse(
                status = false,
                code = "401",
                message = "Token kosong",
                data = null
            )
        }

        return client.post("$BASE_URL${MainService.MAPS_ROUTE}") {
            auth(token)
            header("X-APP-ID", APP_ID_VALUE)
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }





}
