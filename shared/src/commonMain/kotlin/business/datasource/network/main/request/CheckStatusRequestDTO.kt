package business.datasource.network.main.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckStatusRequestDTO(
    @SerialName("booking_id") var bookingId: Int? = null,
    @SerialName("booking_code") var bookingCode: String? = null,
    @SerialName("payment_type_id") var paymentTypeId: Int? = null,
    @SerialName("payment_reff") var paymentReff: String? = null
)
