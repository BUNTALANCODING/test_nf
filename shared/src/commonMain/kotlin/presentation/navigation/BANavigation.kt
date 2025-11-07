package presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface BANavigation {

    @Serializable
    data object FormBeritaAcara : BANavigation

    @Serializable
    data object PengemudiBeritaAcara : BANavigation

    @Serializable
    data object KemenhubBeritaAcara : BANavigation

    @Serializable
    data object PreviewBeritaAcara : BANavigation

}

