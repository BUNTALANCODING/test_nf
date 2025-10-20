package business.datasource.network.main.responses


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RampcheckStartDTO(

	@SerialName("rampcheck_id") val rampcheckId: Int? = 0,

	@SerialName("status") val status: String? = "",
)
