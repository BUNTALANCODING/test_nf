package business.datasource.network.main.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentType(
    @SerialName("payment_type_id") var paymentTypeId: Int? = null,
    @SerialName("payment_type_code") var paymentTypeCode: String? = null,
    @SerialName("payment_type_name") var paymentTypeName: String? = null,
    @SerialName("payment_type_img") var paymentTypeImg: String? = null,
    @SerialName("description") var description: String? = null
)