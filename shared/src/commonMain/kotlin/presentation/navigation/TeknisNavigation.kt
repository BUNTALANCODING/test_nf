package presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface TeknisNavigation {

    @Serializable
    data object GuidePemeriksaanTeknisUtama : TeknisNavigation

    @Serializable
    data object CameraTeknisUtama : TeknisNavigation

    @Serializable
    data object QuestionTeknisUtama : TeknisNavigation

    @Serializable
    data object GuidePemeriksaanTeknisPenunjang : TeknisNavigation

    @Serializable
    data object CameraTeknisPenunjang : TeknisNavigation

    @Serializable
    data object QuestionTeknisPenunjang : TeknisNavigation


}

