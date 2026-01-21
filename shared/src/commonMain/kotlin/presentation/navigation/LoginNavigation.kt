package presentation.navigation

import kotlinx.serialization.Serializable

sealed interface LoginNavigation {
    @Serializable data object Login : LoginNavigation
}
