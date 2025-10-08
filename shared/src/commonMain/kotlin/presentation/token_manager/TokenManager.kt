package presentation.token_manager

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import business.core.DataState
import business.datasource.network.main.request.UpdateDeviceTokenRequestDTO
import business.interactors.main.LogoutUseCase
import business.interactors.main.UpdateDeviceTokenUseCase
import business.interactors.splash.CheckFCMTokenUseCase
import business.interactors.splash.CheckTokenUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import presentation.ui.main.home.view_model.HomeEvent

class TokenManager(
    private val checkTokenUseCase: CheckTokenUseCase,
    private val checkFCMTokenUseCase: CheckFCMTokenUseCase,
    private val logoutUseCase: LogoutUseCase,
) {

    private val sessionScope = CoroutineScope(Dispatchers.Main)

    val state: MutableState<TokenState> = mutableStateOf(TokenState())

    fun onTriggerEvent(event: TokenEvent) {
        when (event) {
            is TokenEvent.CheckToken -> {
                checkToken()
            }

            is TokenEvent.Logout -> {
                logout()
            }

        }
    }

    private fun checkToken() {
        checkTokenUseCase.execute(Unit).onEach { dataState ->
            when (dataState) {
                is DataState.NetworkStatus -> {}
                is DataState.Response -> {}
                is DataState.Data -> {
                    state.value = state.value.copy(isTokenAvailable = dataState.data ?: false)
                }

                is DataState.Loading -> {}
            }
        }.launchIn(sessionScope)
    }

    private fun checkFCMToken() {
        checkFCMTokenUseCase.execute(Unit).onEach { dataState ->
            when (dataState) {
                is DataState.NetworkStatus -> {}
                is DataState.Response -> {}
                is DataState.Data -> {
                    state.value = state.value.copy(isFCMTokenAvailable = dataState.data ?: false)
                }

                is DataState.Loading -> {}
            }
        }.launchIn(sessionScope)
    }

    private fun logout() {
        logoutUseCase.execute(Unit).onEach { dataState ->
            when (dataState) {
                is DataState.NetworkStatus -> {}
                is DataState.Response -> {}
                is DataState.Data -> {
                    checkToken()
                }

                is DataState.Loading -> {}
            }
        }.launchIn(sessionScope)
    }

}