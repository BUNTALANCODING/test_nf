package presentation.navigation

import kotlinx.serialization.Serializable

sealed interface AppNavigation {
    @Serializable data object Splash : AppNavigation
}
