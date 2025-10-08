package presentation.ui.main.home.view_model

import business.core.NetworkState
import business.core.ProgressBarState
import business.core.UIComponentState
import business.core.ViewState
import business.datasource.network.common.JAlertResponse
import business.datasource.network.main.request.DataPassengerCO
import business.datasource.network.main.request.PassengerCheckout
import business.datasource.network.main.responses.CargoCategoryDTO
import business.datasource.network.main.responses.CheckStatusDTO
import business.datasource.network.main.responses.CheckoutDTO
import business.datasource.network.main.responses.City
import business.datasource.network.main.responses.FerryDTO
import business.datasource.network.main.responses.Gender
import business.datasource.network.main.responses.IdentityType
import business.datasource.network.main.responses.PassengerCategory
import business.datasource.network.main.responses.PaymentDTO
import business.datasource.network.main.responses.ProfileDTO
import business.datasource.network.main.responses.Route
import business.domain.main.Home
import business.domain.main.SearchTicket
import business.domain.main.TicketType
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class HomeState(

    val cityCodeValue : String = "",
    val middleCodeValue : String = "",
    val lastCodeValue : String = "",
    val noRangka : String = "",
    val clearTrigger: Boolean = false,
    val selectedOption: Int = 1,
    val nikValue: String = "",
    val namaLengkap: String = "",
    val tanggalPemeriksaan : String = "",
    val selectedTab: Int = 1,
    val selectedTabListrik: Int = 1,
    val searchValue : String = "",
    val selectedFilter : String = "",
    val selectedVehicle: String = "",
    val selectedMethod: Pair<Int, Int> = Pair(0,0),
    val pin: String = "",
    val showDialogPajak: UIComponentState = UIComponentState.Hide,

    val home: Home = Home(),
    val isTokenValid: Boolean = false,
    val updateTokenFCM: String = "",
    val profile: ProfileDTO = ProfileDTO(),
    val ticketType: List<TicketType> = listOf(),
    val onSearchTicket: SearchTicket = SearchTicket(),
    val selectedOrigin: String = "",
    val selectedDestination: String = "",
    val selectedDate: String = "",
    val selectedTime: String = "",
    val selectedTicketType: CargoCategoryDTO = CargoCategoryDTO(),
    val selectedPassenger: PassengerCategory = PassengerCategory(),
    val selectedPassengerList: List<business.datasource.network.main.request.Passenger> = listOf(),
    val selectedPassengerList2: List<PassengerCategory> = listOf(),
    val selectedRoute: Route = Route(),
    val typeRoute: String = "",
    val ferryItem: List<FerryDTO> = listOf(),
    val ferryItemDetail: FerryDTO = FerryDTO(),
    val navigateToFerry: Boolean = false,
    val navigateToSelectPayment: Boolean = false,
    val isOrigin: Int = 1,
    val onUpdateCount: Int = 0,
    val onUpdatePassengerInc: PassengerCategory = PassengerCategory(),
    val onUpdatePassengerDec: PassengerCategory = PassengerCategory(),
    val incDecPassenger: Int = 0,
    val editScheduleBSheetState: UIComponentState = UIComponentState.Hide,
    val searchDialogState: UIComponentState = UIComponentState.Hide,
    val dateDialogState: UIComponentState = UIComponentState.Hide,
    val birthdayDialogState: UIComponentState = UIComponentState.Hide,
    val timeDialogState: UIComponentState = UIComponentState.Hide,
    val passengerDialogState: UIComponentState = UIComponentState.Hide,
    val ticketTypeDialogState: UIComponentState = UIComponentState.Hide,

    val selectedTitle: String = "Tuan",
    val selectedName: String = "",
    val selectedEmail: String = "",
    val selectedGender: Gender = Gender(),
    val selectedCity: City = City(),
    val selectedAge: String = "",
    val selectedIdentity: String = "",
    val selectedIdentityType: IdentityType = IdentityType(),
    val selectedPhone: String = "",
    val selectedBirthday: String = "",
    val selectedVehicleNumber: String = "",
    val priceDetailState: UIComponentState = UIComponentState.Hide,
    val identityState: UIComponentState = UIComponentState.Hide,
    val cityState: UIComponentState = UIComponentState.Hide,
    val genderState: UIComponentState = UIComponentState.Hide,
    val passengerDetailState: UIComponentState = UIComponentState.Hide,
    val bookingDetailState: UIComponentState = UIComponentState.Hide,

    val selectedPassengerCO: List<PassengerCheckout> = listOf(),
    val selectedPassengerCODetail: List<DataPassengerCO> = listOf(),
    val dataSuccessCheckout: CheckoutDTO = CheckoutDTO(
        ticketId = 1,
        ticketCode = "43535673445",
        totalPassenger = 1,
        totalPrice = 10000,
        expireTime = "23-07-2025 15:14:00"
    ),

    val idPassengerCO: Int = 0,
    val idIndexDetail: Int = 0,

    val checkStatusResponse: CheckStatusDTO = CheckStatusDTO(),
    val paymentResponse: PaymentDTO = PaymentDTO(),

    val errorResult: JAlertResponse = JAlertResponse(),
    val errorDialogState: UIComponentState = UIComponentState.Hide,
    val time: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val networkState: NetworkState = NetworkState.Good,
) : ViewState
