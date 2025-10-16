package business.datasource.network.main.responses


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetLocationDTO(

	@SerialName("rampcheck_location_name") val rampcheckLocationName: String? = "",

	@SerialName("rampcheck_location_id") val rampcheckLocationId: String? = "",
)
