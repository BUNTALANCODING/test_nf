package business.datasource.network.main.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FerryRequestDTO(
    @SerialName("route_id") var routeId: Int? = null,
    @SerialName("date") var date: String? = null,
    @SerialName("time") var time: String? = null,
    @SerialName("cargo_category_id") var cargoCategoryId: Int? = null,
    @SerialName("passenger") var passenger: List<Passenger> = listOf()
)

@Serializable
data class Passenger(
    @SerialName("category_id") var categoryId: Int? = null,
    @SerialName("qty") var qty: Int? = null
)
