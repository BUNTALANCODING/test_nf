package business.datasource.network.main.responses

import common.Format
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentDTO(
    @SerialName("booking_id") var bookingId: Int? = null,
    @SerialName("booking_code") var bookingCode: String? = null,
    @SerialName("payment_reff") var paymentReff: String? = null,
    @SerialName("payment_code") var paymentCode: String? = null,
    @SerialName("nmid") var nmid: String? = null,
    @SerialName("nns") var nns: String? = null,
    @SerialName("rrn") var rrn: String? = null,
    @SerialName("merchant_name") var merchantName: String? = null,
    @SerialName("terminal") var terminalName: String? = null,
    @SerialName("total_price") var totalPrice: Int? = null,
    @SerialName("expire_time") var expireTime: String? = null
) {
    fun getPrice() = "Rp ${Format(totalPrice ?: 0)}"
}
