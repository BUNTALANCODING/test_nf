package presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeNavigation {

    @Serializable
    data object Search : HomeNavigation

    @Serializable
    data object Notification : HomeNavigation

    @Serializable
    data object Forgot : HomeNavigation

    @Serializable
    data object ForgotSuccess : HomeNavigation

    @Serializable
    data object DaftarKendaraan : HomeNavigation

    @Serializable
    data object DashboardSaldo : HomeNavigation

    @Serializable
    data object News : HomeNavigation

    @Serializable
    data object DetailNews : HomeNavigation

    @Serializable
    data object SamsatCeria : HomeNavigation

    @Serializable
    data object DetailPajak : HomeNavigation

    @Serializable
    data object VerifyGuideKTP : HomeNavigation

    @Serializable
    data object CameraKTP : HomeNavigation

    @Serializable
    data object VerifyPhotoKTP : HomeNavigation

    @Serializable
    data object VerifyGuideFace : HomeNavigation

    @Serializable
    data object CameraFace : HomeNavigation

    @Serializable
    data object VerifyPhotoFace : HomeNavigation

    @Serializable
    data object PembayaranPajak : HomeNavigation

    @Serializable
    data object MenungguPembayaran : HomeNavigation

    @Serializable
    data object PilihAlamat : HomeNavigation

    @Serializable
    data object TambahAlamat : HomeNavigation

    @Serializable
    data object MetodePembayaran : HomeNavigation

    @Serializable
    data object MilikOrangLain : HomeNavigation

    @Serializable
    data object DaftarKendaraanSaya : HomeNavigation

    @Serializable
    data object DetailKendaraanSaya : HomeNavigation

    @Serializable
    data object TambahKendaraanSaya : HomeNavigation

    @Serializable
    data object RiwayatPembayaran : HomeNavigation

    @Serializable
    data object DetailRiwayat : HomeNavigation

    @Serializable
    data object DetailMenungguPembayaran : HomeNavigation

    @Serializable
    data object Etbpkp : HomeNavigation

    @Serializable
    data object DetailEtbpkp : HomeNavigation

    @Serializable
    data object EPengesahan : HomeNavigation

    @Serializable
    data object DetailEPengesahan : HomeNavigation

    @Serializable
    data object EKD : HomeNavigation

    @Serializable
    data object DetailEKD : HomeNavigation

    @Serializable
    data object TopUpListrik : HomeNavigation

    @Serializable
    data object DetailTopUpListrik : HomeNavigation

    @Serializable
    data object PinListrikScreen : HomeNavigation

    @Serializable
    data object BuktiTransaksi : HomeNavigation

    @Serializable
    data object AllArticle : HomeNavigation

    @Serializable
    data class DetailArticle(val id: Int) : HomeNavigation

    @Serializable
    data object Settings : HomeNavigation

    @Serializable
    data object Booking : HomeNavigation

    @Serializable
    data object Checkout : HomeNavigation

    @Serializable
    data object PaymentDetail : HomeNavigation

    @Serializable
    data object Login : HomeNavigation

    @Serializable
    data object Register : HomeNavigation

    @Serializable
    data object SuccessRegister : HomeNavigation

    @Serializable
    data object AturPin : HomeNavigation

    @Serializable
    data object KonfirmasiPin : HomeNavigation

    @Serializable
    data object SuccessPin : HomeNavigation

}

