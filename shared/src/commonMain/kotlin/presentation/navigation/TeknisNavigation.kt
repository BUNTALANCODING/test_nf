package presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface TeknisNavigation {

    @Serializable
    data object GuidePemeriksaanTeknisUtama : TeknisNavigation

    @Serializable
    data object CameraTeknisUtama : TeknisNavigation

    @Serializable
    data class QuestionTeknisUtama(
        val uniqueKey: String      // ⬅️ argumen yang mau dibawa
    ) : TeknisNavigation

    @Serializable
    data object CameraTidakSesuaiTeknisUtama : TeknisNavigation

    @Serializable
    data object GuidePemeriksaanTeknisPenunjang : TeknisNavigation

    @Serializable
    data object CameraTeknisPenunjang : TeknisNavigation

    @Serializable
    data object QuestionTeknisPenunjang : TeknisNavigation


}

