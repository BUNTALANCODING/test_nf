package presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AdministrasiNavigation {

    @Serializable
    data object GuideAdministrasi : AdministrasiNavigation


}

