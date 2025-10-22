package presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeNavigation {

    @Serializable
    data object Forgot : HomeNavigation

    @Serializable
    data object ForgotSuccess : HomeNavigation

    @Serializable
    data object Settings : HomeNavigation

    @Serializable
    data object Login : HomeNavigation

    @Serializable
    data object Register : HomeNavigation

    @Serializable
    data object SuccessRegister : HomeNavigation

    @Serializable
    data object Pemeriksaan : HomeNavigation

    @Serializable
    data object GuideFotoPetugas : HomeNavigation

    @Serializable
    data object CameraFotoPetugas : HomeNavigation

    @Serializable
    data object VerifyFotoPetugas : HomeNavigation

    @Serializable
    data object DataKendaraanKIR : HomeNavigation

    @Serializable
    data object CameraFotoKIR : HomeNavigation

    @Serializable
    data object QRKIRScreen : HomeNavigation

    @Serializable
    data object DetailHasilFotoKIR : HomeNavigation

    @Serializable
    data object FotoKendaraan : HomeNavigation

    @Serializable
    data object CameraVehicle : HomeNavigation


}

