package presentation.ui.main.auth.view_model

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import business.domain.usecase.LoginUseCase
import common.auth.AuthRepository
import common.auth.AuthUiContext
import common.auth.AuthUserProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repo: AuthRepository,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.Google -> loginWithGoogle(action.ui)
            LoginAction.ErrorShown -> _state.update { it.copy(error = null) }
        }
    }

    private fun loginWithGoogle(ui: AuthUiContext) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val firebaseUser = repo.signInWithGoogle(ui)
                val apiUser = loginUseCase(firebaseUser.googleIdToken)
                AuthUserProvider.refresh()
                _state.update {
                    it.copy(
                        isLoading = false,
                        user = firebaseUser,
                        apiUser = apiUser
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Login gagal"
                    )
                }
            }
        }
    }
}
