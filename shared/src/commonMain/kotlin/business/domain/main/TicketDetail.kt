package business.domain.main

import kotlinx.serialization.Serializable

@Serializable
data class TicketDetail(
    val order: TicketOrder = TicketOrder(),
    val passengerName: String = "",
    val idNumber: String = "",
    val ferryName: String = "",
    val seatNumber: String = "",
    val passengerType: String = ""
)
