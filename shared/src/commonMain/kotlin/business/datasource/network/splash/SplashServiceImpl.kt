package business.datasource.network.splash

import business.constants.BASE_URL
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.splash.responses.LoginDTO
import business.datasource.network.splash.responses.LoginRequestDTO
import business.datasource.network.splash.responses.RegisterRequestDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom

class SplashServiceImpl(
    private val httpClient: HttpClient
) : SplashService {
    override suspend fun login(username: String, password: String): MainGenericResponse<LoginDTO> {
        return httpClient.post {
            url {
                takeFrom(BASE_URL)
                encodedPath += SplashService.LOGIN
            }
            contentType(ContentType.Application.Json)
            setBody(LoginRequestDTO(username = username, password = password))
        }.body()
    }

    override suspend fun register(
        request: RegisterRequestDTO
    ): MainGenericResponse<List<String>> {
        return httpClient.post {
            url {
                takeFrom(BASE_URL)
                encodedPath += SplashService.REGISTER
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}