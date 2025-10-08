package business.datasource.network.main.responses

import common.Format
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckoutDTO(
    @SerialName("booking_id") var ticketId: Int? = null,
    @SerialName("booking_code") var ticketCode: String? = null,
    @SerialName("total_passenger") var totalPassenger: Int? = null,
    @SerialName("total_price") var totalPrice: Int? = 0,
    @SerialName("expire_time") var expireTime: String? = null
) {
    fun getPrice() = "Rp ${Format(totalPrice ?: 0)}"
}