package business.datasource.network.main.responses

import common.Format
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionDTO(
    @SerialName("booking_id") var bookingId: Int? = null,
    @SerialName("booking_code") var bookingCode: String? = "-",
    @SerialName("ticket_status_id") var ticketStatusId: Int? = null,
    @SerialName("payment_expire_time") var paymentExpireTime: String? = "-",
    @SerialName("origin_port_name") var originPortName: String? = "-",
    @SerialName("destination_port_name") var destinationPortName: String? = "-",
    @SerialName("departure_time") var departureTime: String? = "-",
    @SerialName("total_price") var totalPrice: Int? = null
) {
    fun getPrice() = "Rp ${Format(totalPrice ?: 0)}"
}

@Serializable
data class Facility(
    @SerialName("facility_name") var facilityName: String? = null,
    @SerialName("facility_icon") var facilityIcon: String? = null
)

@Serializable
data class PassengerTransaction(
    @SerialName("ticket_detail_code") var ticketDetailCode: String? = null,
    @SerialName("passenger_name") var passengerName: String? = null,
    @SerialName("identity_number") var identityNumber: String? = null,
    @SerialName("identity_type_name") var identityTypeName: String? = null,
    @SerialName("passenger_category_id") var passengerCategoryId: Int? = null,
    @SerialName("passenger_category_name") var passengerCategoryName: String? = null,
    @SerialName("gender_name") var genderName: String? = null,
    @SerialName("brithday") var brithday: String? = null
)

@Serializable
data class DetailTransaction(
    @SerialName("booking_id") var bookingId: Int? = null,
    @SerialName("booking_code") var bookingCode: String? = null,
    @SerialName("ticket_status_id") var ticketStatusId: Int? = null,
    @SerialName("payment_expire_time") var paymentExpireTime: String? = null,
    @SerialName("origin_port_name") var originPortName: String? = null,
    @SerialName("origin_port_description") var originPortDescription: String? = null,
    @SerialName("desination_port_name") var desinationPortName: String? = null,
    @SerialName("desination_port_description") var desinationPortDescription: String? = null,
    @SerialName("departure_time") var departureTime: String? = "2025-07-21 20:21:53",
    @SerialName("cargo_category_name") var cargoCategoryName: String? = null,
    @SerialName("nrkb") var nrkb: String? = null,
    @SerialName("ship_type_name") var shipTypeName: String? = null,
    @SerialName("duration") var duration: Int? = null,
    @SerialName("total_price") var totalPrice: Int? = null,
    @SerialName("payment_type_id") var paymentTypeId: Int? = null,
    @SerialName("payment_reff") var paymentReff: String? = null,
    @SerialName("payment_code") var paymentCode: String? = null,
    @SerialName("ship_id") var shipId: Int? = null,
    @SerialName("facility") var facility: ArrayList<Facility> = arrayListOf(),
    @SerialName("passenger") var passenger: ArrayList<PassengerTransaction> = arrayListOf()
) {
    val time = duration ?: 0
    private val seconds = (time % 60).toString()
    private val minute = (time / 60)
    private val minutes = (minute % 60).toString()
    private val hours = (minute / 60).toString()
    fun getEstimate() = if (minutes != "0") "$hours jam $minutes menit" else "$hours jam"
    fun getPrice() = "Rp ${Format(totalPrice ?: 0)}"
}
