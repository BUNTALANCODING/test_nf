package business.domain.main

import business.datasource.network.main.responses.CargoCategoryDTO
import business.datasource.network.main.responses.FerryDTO
import business.datasource.network.main.responses.PassengerCategory
import business.datasource.network.main.responses.Route
import kotlinx.serialization.Serializable

@Serializable
data class SearchTicket(
    val id: Int = 0,
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
    val isOrigin: Int = 1,
    val onUpdateCount: Int = 0,
)