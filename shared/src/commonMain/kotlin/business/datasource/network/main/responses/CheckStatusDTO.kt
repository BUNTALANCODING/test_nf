package business.datasource.network.main.responses

import common.Format
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckStatusDTO(
    @SerialName("booking_id") var bookingId: Int? = null,
    @SerialName("booking_code") var bookingCode: String? = null,
    @SerialName("payment_reff") var paymentReff: String? = null,
    @SerialName("total_price") var totalPrice: Int? = null,
    @SerialName("expire_time") var expireTime: String? = null,
    @SerialName("payment_status") var paymentStatus: String? = null,
    @SerialName("payment_message") var paymentMessage: String? = null
) {
    fun getPrice() = "Rp ${Format(totalPrice ?: 0)}"
}
