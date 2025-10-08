package business.datasource.network.main.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailTransactionRequestDTO(
    @SerialName("booking_id") var idBooking: Int? = 0,
)
