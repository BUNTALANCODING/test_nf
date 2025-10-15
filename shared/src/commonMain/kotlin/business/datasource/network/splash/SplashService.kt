package business.datasource.network.splash

import business.datasource.network.common.MainGenericResponse
import business.datasource.network.splash.responses.LoginDTO
import business.datasource.network.splash.responses.RegisterRequestDTO

interface SplashService {
    companion object {
        const val REGISTER = "register"
        const val LOGIN = "login"
    }

    suspend fun login(username: String, password: String): MainGenericResponse<LoginDTO>

    suspend fun register(request: RegisterRequestDTO): MainGenericResponse<List<String>>

}