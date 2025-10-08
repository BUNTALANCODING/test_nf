package business.datasource.network.main.responses

import business.domain.main.Passenger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CargoCategoryDTO(
    @SerialName("cargo_category_id") var cargoCategoryId: Int? = null,
    @SerialName("cargo_category_code") var cargoCategoryCode: String? = null,
    @SerialName("cargo_category_name") var cargoCategoryName: String? = null,
    @SerialName("cargo_category_alias") var cargoCategoryAlias: String? = null,
    @SerialName("min_passenger") var minPassenger: Int? = null,
    @SerialName("max_passenger") var maxPassenger: Int? = null,
    @SerialName("cargo_category_img") var cargoCategoryImg: String? = null
)

@Serializable
data class PassengerCategory(
    @SerialName("passenger_category_id") var passengerCategoryId: Int? = null,
    @SerialName("passenger_category_code") var passengerCategoryCode: String? = null,
    @SerialName("passenger_category_name") var passengerCategoryName: String? = null,
    @SerialName("passenger_category_qty") var passengerCategoryQty: Int? = 0,
)

fun PassengerCategory.toPassenger() = Passenger(
    passengerCategoryId = passengerCategoryId ?: 0,
    passengerCategoryCode = passengerCategoryCode ?: "",
    passengerCategoryName = passengerCategoryName ?: "",
    passengerCategoryQty = passengerCategoryQty ?: 0
)

fun PassengerCategory.getQty() : Int {
    return passengerCategoryQty!!
}

fun PassengerCategory.toInc() : Int {
    return passengerCategoryQty!!.inc()
}

fun PassengerCategory.toDec() : Int {
    return passengerCategoryQty!!.dec()
}