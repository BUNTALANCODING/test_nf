package business.datasource.network.main.request

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SendEmailBARequestDTO(

	@SerialName("rampcheck_id")
	val rampcheckId: Int? = null,

	@SerialName("emails")
	val emails: List<String?>? = null
)
