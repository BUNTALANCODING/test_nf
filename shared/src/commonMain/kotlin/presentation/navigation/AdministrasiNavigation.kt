package presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AdministrasiNavigation {

    @Serializable
    data object GuideAdministrasi : AdministrasiNavigation

    @Serializable
    data object PemeriksaanKartuUji : AdministrasiNavigation

    @Serializable
    data object CameraKartuUji : AdministrasiNavigation

    @Serializable
    data object HasilPemeriksaanKartuUji : AdministrasiNavigation

    @Serializable
    data object PemeriksaanKPReguler : AdministrasiNavigation

    @Serializable
    data object CameraKartuUjiNegative : AdministrasiNavigation

    @Serializable
    data object CameraSIMNegative : AdministrasiNavigation

    @Serializable
    data object CameraKPReguler : AdministrasiNavigation

    @Serializable
    data object HasilPemeriksaanKPReguler : AdministrasiNavigation

    @Serializable
    data object PemeriksaanKPCadangan : AdministrasiNavigation

    @Serializable
    data object CameraKPCadangan : AdministrasiNavigation

    @Serializable
    data object HasilPemeriksaanKPCadangan : AdministrasiNavigation

    @Serializable
    data object PemeriksaanSIM : AdministrasiNavigation

    @Serializable
    data object CameraSIM : AdministrasiNavigation

    @Serializable
    data object HasilPemeriksaanSIM : AdministrasiNavigation

}

