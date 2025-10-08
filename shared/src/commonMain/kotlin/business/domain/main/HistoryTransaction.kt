package business.domain.main

import business.constants.OrderStatus
import kotlinx.serialization.Serializable

@Serializable
data class TicketOrder(
    val id: String = "",
    val from: String = "",
    val to: String = "",
    val date: String = "",
    val time: String = "",
    val ticketType: String = "",
    val passengers: Int = 1,
    val status: OrderStatus = OrderStatus.All
)
