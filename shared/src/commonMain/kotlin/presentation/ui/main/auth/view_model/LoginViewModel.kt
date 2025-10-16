package presentation.ui.main.auth.view_model

import business.core.BaseViewModel
import business.core.NetworkState
import business.interactors.splash.CheckTokenUseCase
import business.interactors.splash.LoginUseCase
import business.interactors.splash.RegisterUseCase
import com.mmk.kmpnotifier.notification.NotifierManager
import presentation.ui.main.home.view_model.HomeEvent

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val checkTokenUseCase: CheckTokenUseCase,
//    private val updateDeviceTokenUseCase: UpdateDeviceTokenUseCase,
) : BaseViewModel<LoginEvent, LoginState, LoginAction>() {

    init {
        checkToken()
    }

    override fun setInitialState() = LoginState()

    override fun onTriggerEvent(event: LoginEvent) {
        when (event) {

            is LoginEvent.Login -> {
                login()
            }

            is LoginEvent.Register -> {
                register()
            }

            is LoginEvent.ForgotPassword -> {
//                forgotPassword(event.value)
            }

            is LoginEvent.OnUpdateNameRegister -> {
                onUpdateNameRegister(event.value)
            }

            is LoginEvent.OnUpdatePasswordLogin -> {
                onUpdatePasswordLogin(event.value)
            }

            is LoginEvent.OnUpdateUsernameLogin -> {
                onUpdateUsernameLogin(event.value)
            }

            is LoginEvent.OnRetryNetwork -> {
                onRetryNetwork()
            }

            is LoginEvent.OnUpdateNetworkState -> {
                onUpdateNetworkState(event.networkState)
            }

            is LoginEvent.OnUpdateAddressRegister -> {
                onUpdateAddressRegister(event.value)
            }

            is LoginEvent.OnUpdateConfirmPasswordRegister -> {
                onUpdateConfirmPasswordRegister(event.value)
            }

            is LoginEvent.OnUpdatePhoneRegister -> {
                onUpdatePhoneRegister(event.value)
            }

            is LoginEvent.OnUpdateEmailRegister -> {
                onUpdateEmailRegister(event.value)
            }

            is LoginEvent.ResetPasswordField -> {
                resetPasswordField()
            }

            is LoginEvent.IsSuccessResend -> {
                onUpdateSuccessResend(event.value)
            }

            is LoginEvent.OnUpdateFCMToken -> {
                onUpdateFCMToken(event.value)
            }

            is LoginEvent.ResendForgotPassword -> {
//                resendForgotPassword()
            }
        }
    }

    private fun resetPasswordField() {
        setState {
            copy(passwordLogin = "", confirmPasswordRegister = "")
        }
    }

//    private fun updateTokenFCM(token: String) {
//        executeUseCase(
//            updateDeviceTokenUseCase.execute(
//                UpdateDeviceTokenRequestDTO(
//                    token = token
//                )
//            ), onSuccess = { data, status ->
//                /*data?.let {
//                    setState { copy(fcmToken = it) }
//                }*/
//            }, onLoading = {
//                setState { copy(progressBarState = it) }
//            }, onNetworkStatus = {
//                setEvent(LoginEvent.OnUpdateNetworkState(it))
//            }
//        )
//    }

    /*private fun forgotPassword(email: String) {
        executeUseCase(
            forgotPasswordUseCase.execute(params = ForgotPasswordUseCase.Params(email)),
            onSuccess = { data, status ->
                status?.let { s ->
                    if (s) {
                        setAction {
                            LoginAction.Navigation.NavigateToSuccess
                        }
                    }
                }
            }, onLoading = {
                setState { copy(progressBarState = it) }
            }, onNetworkStatus = {
                setEvent(LoginEvent.OnUpdateNetworkState(it))
            }
        )
    }

    private fun resendForgotPassword() {
        executeUseCase(
            forgotPasswordUseCase.execute(params = ForgotPasswordUseCase.Params(state.value.usernameLogin)),
            onSuccess = { data, status ->
                *//*status?.let { s ->
                    if (s) {
                        setAction {
                            LoginAction.Navigation.NavigateToSuccess
                        }
                    }
                }*//*
            }, onLoading = {
                setState { copy(progressBarState = it) }
            }, onNetworkStatus = {
                setEvent(LoginEvent.OnUpdateNetworkState(it))
            }
        )
    }*/

    private fun checkToken() {
        executeUseCase(
            checkTokenUseCase.execute(Unit),
            onSuccess = { data, status ->
                data?.let {

                    setState {
                        copy(isTokenValid = it)
                    }

                    setAction {
                        if (it) {
                            LoginAction.Navigation.NavigateToMain
                        } else {
                            LoginAction.Navigation.NavigateToLogin
                        }
                    }
                }
            }, onLoading = {
                setState { copy(progressBarState = it) }
            }
        )
    }

    private fun login() {
        executeUseCase(
            loginUseCase.execute(
                LoginUseCase.Params(
                    email = state.value.usernameLogin,
                    password = state.value.passwordLogin,
                )
            ),
            onSuccess = { data, status ->
                data?.let {
                    setAction {
                        if (it.token.toString().isNotEmpty()) {
//                            state.value.fcmToken.let { t -> updateTokenFCM(t) }
                            LoginAction.Navigation.NavigateToMain
                        } else {
                            LoginAction.Navigation.NavigateToLogin
                        }
                    }
                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            }
        )
    }

    private fun loginDummy() {
        setAction {
            LoginAction.Navigation.NavigateToMain
        }
    }

    private fun register() {
        executeUseCase(
            registerUseCase.execute(
                RegisterUseCase.Params(
                    address = state.value.addressRegister,
                    email = state.value.usernameLogin,
                    phone = state.value.phoneRegister,
                    password = state.value.passwordLogin,
                    confirmPassword = state.value.confirmPasswordRegister,
                    name = state.value.nameRegister
                )
            ), onSuccess = { data, status ->
                data?.let {
                    status?.let { s ->
                        if (s) {
                            setState { copy(isSuccessRegister = true) }
                            setAction {
                                if (it.isBlank()){
                                    LoginAction.Navigation.NavigateToLogin
                                } else{
                                    LoginAction.Navigation.NavigateToMain
                                }
                            }
                        } else {
                            setAction { LoginAction.Navigation.NavigateToRegister }
                        }
                    }
                }.run {
                    setState { copy(isSuccessRegister = false) }
                    setAction { LoginAction.Navigation.NavigateToRegister }
                }
            }, onLoading = {
                setState { copy(progressBarState = it) }
            }
        )
    }

    private fun registerDummy() {
        setAction {
            LoginAction.Navigation.NavigateToMain
        }
    }

    private fun onUpdateSuccessResend(value: Boolean) {
        setState {
            copy(isSuccessResend = value)
        }
    }

    private fun onUpdateFCMToken(value: String) {
        setState {
            copy(fcmToken = value)
        }
    }

    private fun onUpdateNameRegister(value: String) {
        setState {
            copy(nameRegister = value)
        }
    }

    private fun onUpdateEmailRegister(value: String) {
        setState {
            copy(emailRegister = value)
        }
    }

    private fun onUpdateAddressRegister(value: String) {
        setState {
            copy(addressRegister = value)
        }
    }

    private fun onUpdatePhoneRegister(value: String) {
        setState {
            copy(phoneRegister = value)
        }
    }

    private fun onUpdateConfirmPasswordRegister(value: String) {
        setState {
            copy(confirmPasswordRegister = value)
        }
    }

    private fun onUpdatePasswordLogin(value: String) {
        setState {
            copy(passwordLogin = value)
        }
    }

    private fun onUpdateUsernameLogin(value: String) {
        setState {
            copy(usernameLogin = value)
        }
    }

    private fun onRetryNetwork() {
    }

    private fun onUpdateNetworkState(networkState: NetworkState) {
        setState {
            copy(networkState = networkState)
        }
    }

}