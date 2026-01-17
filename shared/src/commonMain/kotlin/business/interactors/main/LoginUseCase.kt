package business.domain.usecase

import business.constants.DataStoreKeys
import business.core.AppDataStore
import business.datasource.network.main.MainService
import business.datasource.network.main.dto.request.GoogleLoginRequest
import business.datasource.network.main.dto.response.GoogleLoginResponse

class LoginUseCase(
    private val service: MainService,
    private val dataStore: AppDataStore
) {

    suspend operator fun invoke(firebaseIdToken: String): GoogleLoginResponse {
        require(firebaseIdToken.isNotBlank()) { "Firebase ID Token kosong" }

        val response = service.loginWithGoogle(
            GoogleLoginRequest(id_token = firebaseIdToken)
        )

        val ok = response.status == true && (response.code == "200" || response.code == "201")
        if (!ok) {
            throw IllegalStateException(response.message ?: "Login gagal")
        }

        val result = response.data
            ?: throw IllegalStateException("Response result kosong: ${response.message}")

        val apiToken = result.token
            ?: throw IllegalStateException("Token API kosong di response")

        dataStore.setValue(DataStoreKeys.TOKEN, apiToken)

        return result
    }

    suspend fun getToken(): String? {
        return dataStore.readValue(DataStoreKeys.TOKEN)?.takeIf { it.isNotBlank() }
    }

    suspend fun clearToken() {
        dataStore.setValue(DataStoreKeys.TOKEN, "")
    }
}
