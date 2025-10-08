package presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface DetailNavigation {

    @Serializable
    data object Detail : DetailNavigation

}

