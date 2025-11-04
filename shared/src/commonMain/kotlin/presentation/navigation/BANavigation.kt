package presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface BANavigation {

    @Serializable
    data object FormBeritaAcara : BANavigation

    @Serializable
    data object PreviewBeritaAcara : BANavigation

}

