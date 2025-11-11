package business.datasource.network.main.responses

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SendEmailBADTO(

	@SerialName("emails")
	val emails: List<String?>? = null,

	@SerialName("file")
	val file: String? = null
)
