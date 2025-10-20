package presentation.ui.main.home.view_model

import androidx.compose.ui.graphics.ImageBitmap
import business.core.NetworkState
import business.core.UIComponentState
import business.core.ViewEvent
import business.datasource.network.main.responses.GetLocationDTO

sealed class HomeEvent : ViewEvent {

    data object GetHomeContent : HomeEvent()

    data object GetLocation : HomeEvent()

    data object RampcheckStart : HomeEvent()

    data object UploadOfficerImage : HomeEvent()

    data class OnUpdateCityCode(val value: String) : HomeEvent()

    data class OnShowDialogDatePicker(val value: UIComponentState) : HomeEvent()

    data class OnUpdateTanggalPemeriksaan(val value: String) : HomeEvent()

    data class OnShowDialogLocation(val value: UIComponentState) : HomeEvent()

    data class OnUpdateLocation(val value: String) : HomeEvent()

    data class OnUpdateLocationId(val value: String) : HomeEvent()

    data class OnUpdateListLocation(val value: List<GetLocationDTO>): HomeEvent()

    data class OnUpdateLatitude(val value: String) : HomeEvent()

    data class OnUpdateLongitude(val value: String) : HomeEvent()

    data class OnLocationTrigger(val value: Boolean) : HomeEvent()

    data class OnUpdateStatusMessage(val value: String) : HomeEvent()

    data class OnUpdateClearTrigger(val value: Boolean) : HomeEvent()

    data class OnUpdateSelectedVehicle(val value: String) : HomeEvent()

    data class OnUpdateMiddleCode(val value: String) : HomeEvent()

    data class OnUpdateLastCode(val value: String) : HomeEvent()

    data class OnUpdateNoRangka(val value: String) : HomeEvent()

    data class OnUpdateSelectedOption(val value: Int) : HomeEvent()

    data class OnUpdateConditionSelection(val cardId: String, val selection: Int) : HomeEvent()

    data class OnUpdateSelectedTab(val value: Int) : HomeEvent()

    data class OnUpdateSelectedTabListrik(val value: Int) : HomeEvent()

    data class OnUpdateSelectedMethod(val value: Pair<Int, Int>) : HomeEvent()

    data class OnUpdateNik(val value: String) : HomeEvent()

    data class OnUpdateSearch(val value: String) : HomeEvent()

    data class OnUpdateNamaLengkap(val value: String) : HomeEvent()

    data class OnShowDialogPajak(val value: UIComponentState) : HomeEvent()

    data class OnUpdatePin(val value: String) : HomeEvent()

    data class OnUpdateOfficerImage(val value: ByteArray) : HomeEvent()

    data class OnUpdateOfficerImageImageBitmap(val value: ImageBitmap) : HomeEvent()

    data class OnUpdateTokenFCM(val value: String) : HomeEvent()

    data object OnValidateField : HomeEvent()

    /**
     * Booking Screen
     */


    data class OnUpdateNetworkState(
        val networkState: NetworkState
    ) : HomeEvent()

}
