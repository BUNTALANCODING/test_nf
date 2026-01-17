package presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeNavigation {

    @Serializable
    data object Home : HomeNavigation

    @Serializable
    data object InfoRute : HomeNavigation

    @Serializable
    data class DetailRute(val corridorCode: String) : HomeNavigation

    @Serializable data object ArCam : HomeNavigation

    @Serializable data class DetailBus(val busId: String) : HomeNavigation

    @Serializable data object CariHalte : HomeNavigation

    @Serializable data class DetailHalte(val halteId: String) : HomeNavigation

    @Serializable
    data class ArahkanRute(
        val origin: String,
        val destination: String
    ) : HomeNavigation


    @Serializable data object KedatanganBuss : HomeNavigation

    @Serializable data object UbahLokasi : HomeNavigation

    @Serializable data object MauKemana : HomeNavigation

    @Serializable data object PilihPeta : HomeNavigation


}