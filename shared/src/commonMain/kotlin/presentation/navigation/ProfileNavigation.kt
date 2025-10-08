package presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface ProfileNavigation {

    @Serializable
    data object ChangePassword : ProfileNavigation

    @Serializable
    data object DetailProfile : ProfileNavigation

    @Serializable
    data object AlamatSaya : ProfileNavigation

    @Serializable
    data object UbahPin : ProfileNavigation

    @Serializable
    data object UbahSandi : ProfileNavigation

    @Serializable
    data object PusatBantuan : ProfileNavigation

    @Serializable
    data object SyaratDanKetentuan : ProfileNavigation

    @Serializable
    data object KebijakanPrivasi : ProfileNavigation

    @Serializable
    data object TutupAkun : ProfileNavigation

    @Serializable
    data object Logout : ProfileNavigation

    @Serializable
    data object EditProfile : ProfileNavigation

    @Serializable
    data object Settings : ProfileNavigation

}

