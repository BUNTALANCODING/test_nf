package business.datasource.network.main.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckoutRequestDTO(
    @SerialName("schedule_id") var scheduleId: Int? = null,
    @SerialName("cargo_category_id") var cargoCategoryId: Int? = null,
    @SerialName("nrkb") var nrkb: String? = "",
    @SerialName("passenger") var passenger: List<PassengerCheckout> = listOf()
)

@Serializable
data class DataPassengerCO(
    @SerialName("identity_type_id") var identityTypeId: Int? = null,
    @SerialName("identity_number") var identityNumber: String? = "",
    @SerialName("passenger_name") var passengerName: String? = "",
    @SerialName("age") var age: Int? = null,
    @SerialName("birthday") var birthday: String? = "",
    @SerialName("city_id") var cityId: Int? = null,
    @SerialName("gender_id") var genderId: Int? = null
)

@Serializable
data class PassengerCheckout(
    @SerialName("passenger_category_id") var passengerCategoryId: Int? = null,
    @SerialName("qty") var qty: Int? = null,
    @SerialName("data") var data: List<DataPassengerCO> = listOf()
)
