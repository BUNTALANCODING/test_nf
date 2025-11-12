package presentation.ui.main.home.view_model

import androidx.compose.ui.graphics.ImageBitmap
import business.core.NetworkState
import business.core.ProgressBarState
import business.core.UIComponentState
import business.core.ViewState
import business.datasource.network.common.JAlertResponse
import business.datasource.network.main.responses.CheckQRDTO
import business.datasource.network.main.responses.GetLocationDTO
import business.datasource.network.main.responses.GetStepDTO
import business.datasource.network.main.responses.GetVehicleDTO
import business.datasource.network.main.responses.ProfileDTO
import coil3.Bitmap
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import presentation.component.ConditionItem

data class HomeState(

    val technicalConditions: List<ConditionItem> = initialTechnicalConditionsList(),
    val cityCodeValue: String = "",
    val middleCodeValue: String = "",
    val lastCodeValue: String = "",
    val noRangka: String = "",
    val clearTrigger: Boolean = false,
    val selectedOption: Int = 0,
    val nikValue: String = "",
    val namaLengkap: String = "",
    val tanggalPemeriksaan: String = "",
    val location: String = "",
    val locationId: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val locationTrigger: Boolean = false,
    val statusMessage : String = "",
    val officer_image : ByteArray? = null,
    val officer_image_bitmap : ImageBitmap? = null,
    val qrUrl: String = "",
    val platNumber : String = "",
    val operatorName : String = "",
    val cargoName : String = "",
    val route : String = "",
    val stukNo : String = "",
    val status : String = "",
    val selectedPlatNumber: String = "",
    val kirImage : ImageBitmap? = null,
    val frontImage : ImageBitmap? = null,
    val backImage : ImageBitmap? = null,
    val rightImage : ImageBitmap? = null,
    val leftImage : ImageBitmap? = null,
    val nrkbImage : ImageBitmap? = null,
    val imageTypes : String? = "",

    val filePath : String? = "",


    val dataHasilEKIR: CheckQRDTO = CheckQRDTO(),

    val listVehicle: List<GetVehicleDTO> = listOf(),

    val keteranganKartuTidakAda : String? = "",
    val kartuUjiBitmap : ImageBitmap? = null,
    val getStepKartuUJi : GetStepDTO = GetStepDTO(),
    val tidakSesuaiKartuUji : String = "",

    val selectedTab: Int = 1,
    val selectedTabListrik: Int = 1,
    val searchValue: String = "",
    val selectedFilter: String = "",
    val selectedMethod: Pair<Int, Int> = Pair(0, 0),
    val pin: String = "",
    val showDialogPajak: UIComponentState = UIComponentState.Hide,
    val showDialogDatePicker: UIComponentState = UIComponentState.Hide,
    val showDialogLocation: UIComponentState = UIComponentState.Hide,
    val showDropdownVehicle: UIComponentState = UIComponentState.Hide,

    val selectedLocationList: List<GetLocationDTO> = listOf(),

    val showDialogTandaTanganPengemudi: UIComponentState = UIComponentState.Hide,
    val showDialogTandaTanganPenguji: UIComponentState = UIComponentState.Hide,
    val showDialogTandaTanganKemenhub: UIComponentState = UIComponentState.Hide,
    val ttdPengemudi : String? = "",
    val ttdPenguji : String? = "",
    val ttdKemenhub : String? = "",

    val ttdPengemudiBitmap : ImageBitmap? = null,
    val ttdPengujiBitmap : ImageBitmap? = null,
    val ttdKemenhubBitmap : ImageBitmap? = null,

    val officerName : String = "",
    val officerNip : String = "",
    val driverName : String = "",
    val kemenhubName : String = "",
    val nipKemenhub : String = "",
    val rampcheckId : Int = 0,
    val base64PreviewBA : String = "",

    val showDialogSuccessSubmitSignature: UIComponentState = UIComponentState.Hide,

    val showDialogKartuTidakAda: UIComponentState = UIComponentState.Hide,

    val isTokenValid: Boolean = false,
    val updateTokenFCM: String = "",
    val profile: ProfileDTO = ProfileDTO(),

    val errorResult: JAlertResponse = JAlertResponse(),
    val errorDialogState: UIComponentState = UIComponentState.Hide,
    val time: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val networkState: NetworkState = NetworkState.Good,
) : ViewState

fun initialTechnicalConditionsList(): List<ConditionItem> {
    return listOf(
        ConditionItem(
            id = "lampu_dekat",
            title = "Lampu Utama Dekat : Semua Menyala",
            section = "Sistem Penerangan",
        ),
        ConditionItem(
            id = "lampu_jauh",
            title = "Lampu Utama Jauh : Semua Menyala",
            section = "Sistem Penerangan"
        ),
        ConditionItem(
            id = "sein_depan",
            title = "Lampu Sein Depan : Semua Menyala",
            section = "Sistem Penerangan"
        ),
        ConditionItem(
            id = "sein_belakang",
            title = "Lampu Sein Belakang : Tidak Menyala",
            section = "Sistem Penerangan"
        ),
        ConditionItem(
            id = "lampu_rem",
            title = "Lampu Rem : Semua Menyala",
            section = "Sistem Penerangan"
        ),
        ConditionItem(
            id = "lampu_mundur",
            title = "Lampu Mundur : Semua Menyala",
            section = "Sistem Penerangan"
        ),
        // === SISTEM PENGEREMAN SECTION ===
        ConditionItem(
            id = "rem_utama",
            title = "Kondisi Rem Utama : Berfungsi",
            section = "Sistem Pengereman"
        ),
        ConditionItem(
            id = "rem_parkir",
            title = "Kondisi Rem Parkir : Berfungsi",
            section = "Sistem Pengereman"
        ),

        // === BADAN KENDARAAN SECTION ===
        ConditionItem(
            id = "kaca_depan",
            title = "Kondisi Kaca Depan : Baik",
            section = "Badan Kendaraan"
        ),
        ConditionItem(
            id = "pintu_utama",
            title = "Kondisi Pintu Utama : Tidak Berfungsi",
            section = "Badan Kendaraan"
        ),

        // === KONDISI BAN SECTION ===
        ConditionItem(
            id = "ban_depan",
            title = "Kondisi Ban Depan : Semua Laik",
            section = "Kondisi Ban"
        ),
        ConditionItem(
            id = "ban_belakang",
            title = "Kondisi Ban Belakang : Tidak Laik",
            section = "Kondisi Ban"
        ),

        // === PERLENGKAPAN SECTION ===
        ConditionItem(
            id = "sabuk",
            title = "Sabuk Keselamatan Pengemudi: Ada dan Berfungsi",
            section = "Perlengkapan"
        ),

        // === PENGUKUR KECEPATAN SECTION ===
        ConditionItem(
            id = "speedometer",
            title = "Pengukur Kecepatan : Ada dan Berfungsi",
            section = "Pengukur Kecepatan"
        ),

        // === WIPER SECTION ===
        ConditionItem(
            id = "wiper",
            title = "Penghapus Kaca: Ada dan Berfungsi",
            section = "Wiper"
        ),

        // === TANGGAP DARURAT SECTION ===
        ConditionItem(
            id = "pintu_darurat",
            title = "Pintu Darurat: Ada",
            section = "Tanggap Darurat"
        ),
        ConditionItem(
            id = "pemukul_kaca",
            title = "Alat Pemukul/Pemecah Kaca: Tidak Ada",
            section = "Tanggap Darurat"
        ),

        // === KARTU UJI SECTION ===
        ConditionItem(
            id = "kartu_uji",
            title = "Kartu Uji/STUK : Berlaku",
            section = "Kartu Uji"
        ),
        // === KP REGULER ===
        ConditionItem(
            id = "kp_reguler",
            title = "KP Reguler : Berlaku",
            section = "KP Reguler"
        ),
        // === KP Cadangan SECTION ===
        ConditionItem(
            id = "kp_cadangan",
            title = "KP Cadangan : Berlaku",
            section = "KP Cadangan"
        ),
        // === SIM PENGEMUDI SECTION ===
        ConditionItem(
            id = "sim_pengemudi",
            title = "SIM Pengemudi: SIM A Umum",
            section = "SIM Pengemudi"
        ),

        )
}