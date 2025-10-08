package business.datasource.network.main.responses

import business.domain.main.Ferry
import common.Format
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FerryDTO(
    @SerialName("schedule_id") var scheduleId: Int? = null,
    @SerialName("route_id") var routeId: Int? = null,
    @SerialName("departure_time") var departureTime: String? = null,
    @SerialName("ship_id") var shipId: Int? = null,
    @SerialName("duration") var duration: Int? = null,
    @SerialName("ship_type_name") var shipTypeName: Int? = null,
    @SerialName("available_capacity") var availableCapacity: Int? = null,
    @SerialName("ship_images") var shipImages: List<String> = listOf(),
    @SerialName("details") var details: List<Details> = listOf(),
    @SerialName("total_amount") var totalAmount: Int? = null
) {
    val time = duration ?: 0
    private val seconds = (time % 60).toString()
    private val minute = (time / 60)
    private val minutes = (minute % 60).toString()
    private val hours = (minute / 60).toString()
    fun getEstimate() = if (minutes != "0") "$hours jam $minutes menit" else "$hours jam"
    fun getPrice() = "Rp ${Format(totalAmount ?: 0)}"
}

fun FerryDTO.toFerry() = Ferry(
    scheduleId = scheduleId,
    routeId = routeId,
    departureTime = departureTime,
    shipId = shipId,
    availableCapacity = availableCapacity,
    shipImages = shipImages,
    details = details,
    totalAmount = totalAmount
)

@Serializable
data class Details(
    @SerialName("passenger_category_id") var passengerCategoryId: Int? = null,
    @SerialName("qty") var qty: Int? = null,
    @SerialName("subtotal") var subtotal: Int? = null,
    @SerialName("components") var components: List<Components> = listOf()
)

@Serializable
data class Components(
    @SerialName("fare_component_id") var fareComponentId: Int? = null,
    @SerialName("fare_component_name") var fareComponentName: String? = null,
    @SerialName("unit_price") var unitPrice: Int? = null,
    @SerialName("qty") var qty: Int? = null,
    @SerialName("total_price") var totalPrice: Int? = null,
    @SerialName("by_passenger") var byPassenger: Boolean? = null
)