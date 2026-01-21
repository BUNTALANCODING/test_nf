package presentation.navigation

import kotlinx.serialization.Serializable

sealed interface StudentNavigation {
    @Serializable data object Register : StudentNavigation
    @Serializable data object List : StudentNavigation
    @Serializable data object Detail : StudentNavigation
}
