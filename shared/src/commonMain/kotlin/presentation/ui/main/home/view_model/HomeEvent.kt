package presentation.ui.main.home.view_model

import androidx.compose.ui.graphics.ImageBitmap
import business.core.NetworkState
import business.core.UIComponentState
import business.core.ViewEvent
import business.datasource.network.main.request.AnswersItem
import business.datasource.network.main.responses.GetLocationDTO
import business.datasource.network.main.responses.SubcategoryResponse
import presentation.ui.main.riwayat.viewmodel.RiwayatEvent


sealed class HomeEvent : ViewEvent {

    data object ShowSendEmailDialog : HomeEvent()

    data object HideSendEmailDialog : HomeEvent()

    data class SendEmailBA(val emails: List<String>, val sendToMyEmail: Boolean) : HomeEvent()

    data object GetHomeContent : HomeEvent()

    data object GetLocation : HomeEvent()

    data object RampcheckStart : HomeEvent()

    data object SubmitSignature : HomeEvent()

    data object LoadCard : HomeEvent()

    data object UploadOfficerImage : HomeEvent()

    data object GetVehicle : HomeEvent()

    data object NegativeAnswerUji : HomeEvent()

    data object NegativeAnswerKPReguler : HomeEvent()

    data object NegativeAnswerKPCadangan : HomeEvent()

    data object NegativeAnswerSIM : HomeEvent()

    data object IdentifyKartuUji : HomeEvent()

    data object IdentifySIM : HomeEvent()

    data object SubmitQuestion : HomeEvent()

    data object SubmitQuestionSIM : HomeEvent()

    data object SubmitQuestionTeknisUtama : HomeEvent()

    data object SubmitQuestionTeknisPenunjang : HomeEvent()

    data object CheckQR : HomeEvent()

    data object PlatKIR : HomeEvent()

    data object VehiclePhoto : HomeEvent()

    data object SetStateValue : HomeEvent()

    data object PreviewBA : HomeEvent()

    data object Logout : HomeEvent()

    data class OnUpdateCityCode(val value: String) : HomeEvent()

    data class OnShowDialogDatePicker(val value: UIComponentState) : HomeEvent()

    data class OnUpdateTanggalPemeriksaan(val value: String) : HomeEvent()

    data class OnShowDialogLocation(val value: UIComponentState) : HomeEvent()

    data class OnUpdateLocation(val value: String) : HomeEvent()

    data class OnUpdateLocationId(val value: String) : HomeEvent()

    data class OnUpdateVehiclePlatNumber(val value: String) : HomeEvent()

    data class OnShowDropdownVehiclePicker(val value: UIComponentState) : HomeEvent()

    data class OnShowDialogKartuTidakAda(val value: UIComponentState) : HomeEvent()

    data class OnUpdateKeteranganKartuTidakAda(val value: String) : HomeEvent()

    data class OnUpdateTidakSesuai(val value: String) : HomeEvent()

    data class OnUpdateSIMTidakSesuaiBase64(val value: String) : HomeEvent()

    data class OnUpdateSIMTidakSesuaiBitmap(val value: ImageBitmap) : HomeEvent()

    data class OnUpdateTidakSesuaiBitmap(val value: ImageBitmap) : HomeEvent()

    data class OnUpdateTidakSesuaiBase64(val value: String) : HomeEvent()

    data class OnUpdateListSubmitQuestion(val value: List<AnswersItem>) : HomeEvent()

    data class OnUpdateListSubmitQuestionSIM(val value: List<AnswersItem>) : HomeEvent()

    data class OnUpdateTypeCard(val value: String) : HomeEvent()

    data class OnUpdateCardAvailable(val value: Int) : HomeEvent()

    data class OnShowDialogNotMatch(val value: UIComponentState) : HomeEvent()

    data class OnUpdateListLocation(val value: List<GetLocationDTO>): HomeEvent()

    data class OnUpdateLatitude(val value: String) : HomeEvent()

    data class OnUpdateLongitude(val value: String) : HomeEvent()

    data class OnLocationTrigger(val value: Boolean) : HomeEvent()

    data class OnUpdateStatusMessage(val value: String) : HomeEvent()

    data class OnUpdateClearTrigger(val value: Boolean) : HomeEvent()

    data class OnUpdateMiddleCode(val value: String) : HomeEvent()

    data class OnUpdateLastCode(val value: String) : HomeEvent()

    data class OnUpdateNoRangka(val value: String) : HomeEvent()

    data class OnUpdateSelectedOption(val value: Int) : HomeEvent()

    data class OnUpdateConditionSelection(val cardId: String, val selection: Int) : HomeEvent()

    data class OnUpdateSelectedTab(val value: Int) : HomeEvent()

    data class OnUpdateSelectedTabListrik(val value: Int) : HomeEvent()

    data class OnUpdateSelectedMethod(val value: Pair<Int, Int>) : HomeEvent()

    data class OnUpdateNik(val value: String) : HomeEvent()

    data class OnUpdateQrUrl(val value: String) : HomeEvent()

    data class OnUpdateSearch(val value: String) : HomeEvent()

    data class OnUpdateNamaLengkap(val value: String) : HomeEvent()

    data class OnShowDialogTandaTanganPenguji(val value: UIComponentState) : HomeEvent()

    data class OnShowDialogTandaTanganPengemudi(val value: UIComponentState) : HomeEvent()

    data class OnShowDialogTandaTanganKemenhub(val value: UIComponentState) : HomeEvent()

    data class OnUpdateTTDPenguji(val value: String) : HomeEvent()

    data class OnUpdateTTDPengemudi(val value: String) : HomeEvent()

    data class OnUpdateTTDKemenhub(val value: String) : HomeEvent()

    data class OnShowDialogSubmitSignature(val value: UIComponentState) : HomeEvent()

    data class OnUpdateTTDPengujiBitmap(val value: ImageBitmap) : HomeEvent()

    data class OnUpdateTTDPengemudiBitmap(val value: ImageBitmap) : HomeEvent()

    data class OnUpdateTTDKemenhubBitmap(val value: ImageBitmap) : HomeEvent()

    data class OnShowDialogPajak(val value: UIComponentState) : HomeEvent()

    data class OnUpdatePin(val value: String) : HomeEvent()

    data class OnUpdateOfficerImage(val value: ByteArray) : HomeEvent()

    data class OnUpdateOfficerImageImageBitmap(val value: ImageBitmap) : HomeEvent()

    data class OnUpdateKIRImageBitmap(val value: ImageBitmap) : HomeEvent()

    data class OnUpdateFrontImageBitmap(val value: ImageBitmap) : HomeEvent()

    data class OnUpdateBackImageBitmap(val value: ImageBitmap) : HomeEvent()

    data class OnUpdateLeftImageBitmap(val value: ImageBitmap) : HomeEvent()

    data class OnUpdateRightImageBitmap(val value: ImageBitmap) : HomeEvent()

    data class OnUpdateNrkbImageBitmap(val value: ImageBitmap) : HomeEvent()

    data class UploadVideo(val value: String) : HomeEvent()

    data class OnUpdateDriverName(val value: String) : HomeEvent()

    data class OnUpdateKemenhubName(val value: String) : HomeEvent()

    data class OnUpdateKemenhubNIP(val value: String) : HomeEvent()

    data class OnUpdateOfficerName(val value: String) : HomeEvent()

    data class OnUpdateOfficerNIP(val value: String) : HomeEvent()

    data class OnUpdateRampcheckId(val value: Int) : HomeEvent()

    data class OnUpdateImageTypes(val value: String) : HomeEvent()

    data class OnUpdateTokenFCM(val value: String) : HomeEvent()

    data class OnUpdateKartuUjiBase64(val value: String) : HomeEvent()

    data class OnUpdateKartuUjiImageBitmap(val value: ImageBitmap) : HomeEvent()

    data class OnUpdateSimPengemudiBase64(val value: String) : HomeEvent()

    data class OnUpdateSimPengemudiImageBitmap(val value: ImageBitmap) : HomeEvent()

    data class OnUpdateSelectionKartuUji(val value: Int) : HomeEvent()

    data class OnUpdateSelectionSIM(val value: Int) : HomeEvent()

    data class OnShowDialogSuccessAdministrasi(val value: UIComponentState) : HomeEvent()

    data class OnSuccessTeknisUtama(val value: UIComponentState) : HomeEvent()

    data class OnSuccessTeknisPenunjang(val value: UIComponentState) : HomeEvent()

    data class OnUpdateSelection(
        val questionId: Int,
        val selection: Int
    ) : HomeEvent()

    data class OnUpdateCondition(
        val questionId: Int,
        val value: String
    ) : HomeEvent()

    data class OnUpdateImage(
        val questionId: Int,
        val base64: String
    ) : HomeEvent()

    data class OnSaveImage(val questionId: Int, val base64: String) : HomeEvent()

    data class OnUpdateSelectionPenunjang(
        val questionId: Int,
        val selection: Int
    ) : HomeEvent()

    data class OnUpdateConditionPenunjang(
        val questionId: Int,
        val value: String
    ) : HomeEvent()


    data class OnSaveImagePenunjang(val questionId: Int, val base64: String) : HomeEvent()


    data object OnValidateField : HomeEvent()

    data class ApplyTeknisResultFromApi(
        val apiSubcategories: List<SubcategoryResponse>
    ) : HomeEvent()

    data class ApplyPenunjangResult(
        val apiSubcategories: List<SubcategoryResponse>
    ) : HomeEvent()

    data class OnUpdateTidakSesuaiListBitmap(
        val questionId: Int,
        val bitmap: ImageBitmap
    ) : HomeEvent()

    data class OnUpdateTidakSesuaiListBitmapPenunjang(
        val questionId: Int,
        val bitmap: ImageBitmap
    ) : HomeEvent()

    data class OnSetActiveQuestion(val questionId: Int) : HomeEvent()

    data class OnSetActiveQuestionPenunjang(val questionId: Int) : HomeEvent()

    data class OnUpdateSelectedOptionKPReguler(val id: Int) : HomeEvent()
    data class OnUpdateSelectedOptionKPCadangan(val id: Int) : HomeEvent()

    data class OnUpdateKeteranganKPReguler(val value: String) : HomeEvent()
    data class OnUpdateKeteranganKPCadangan(val value: String) : HomeEvent()

    data class OnUpdateImageKPReguler(val bitmap: ImageBitmap) : HomeEvent()
    data class OnUpdateImageKPCadangan(val bitmap: ImageBitmap) : HomeEvent()

    data class OnUpdateImageKPRegulerBase64(val value: String) : HomeEvent()
    data class OnUpdateImageKPCadanganBase64(val value: String) : HomeEvent()

    data class OnUpdateKpType(val value: Int) : HomeEvent()

    data class ListSubmitQuestionKpReguler(val value: AnswersItem) : HomeEvent()

    data class ListSubmitQuestionKpCadangan(val value: AnswersItem) : HomeEvent()

    data class SubmitQuestionKp(val value: List<AnswersItem>) : HomeEvent()


    object LoadJenisBus : HomeEvent()

    data class OnJenisBusSelected(val jenisBusId: Int) : HomeEvent()



    /**
     * Booking Screen
     */


    data class OnUpdateNetworkState(
        val networkState: NetworkState
    ) : HomeEvent()

}
