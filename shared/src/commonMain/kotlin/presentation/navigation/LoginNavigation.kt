package presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface LoginNavigation {

    @Serializable
    data object NavigateToHome : LoginNavigation

    @Serializable
    data object Login : LoginNavigation

}