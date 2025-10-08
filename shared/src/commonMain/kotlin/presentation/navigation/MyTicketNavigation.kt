package presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface MyTicketNavigation {

    @Serializable
    data object HistoryTransaction : MyTicketNavigation

    @Serializable
    data object BoardingPass : MyTicketNavigation

    @Serializable
    data class PayNow(val ids: Int) : MyTicketNavigation

    @Serializable
    data object PayNowFromDetail : MyTicketNavigation

    @Serializable
    data class Detail(val id: Int) : MyTicketNavigation

}

