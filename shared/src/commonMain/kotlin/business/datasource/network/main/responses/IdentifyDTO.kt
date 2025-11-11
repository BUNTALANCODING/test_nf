package business.datasource.network.main.responses

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IdentifyDTO(

	@SerialName("IdentifyDTO")
	val identifyDTO: List<IdentifyDTOItem?>? = null
)

@Serializable
data class IdentifyDTOItem(

	@SerialName("answer_name")
	val answerName: String? = null,

	@SerialName("question_id")
	val questionId: Int? = null,

	@SerialName("answer_id")
	val answerId: Int? = null
)
