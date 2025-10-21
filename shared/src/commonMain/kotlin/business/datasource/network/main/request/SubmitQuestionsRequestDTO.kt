package business.datasource.network.main.request

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SubmitQuestionsRequestDTO(

	@SerialName("answers")
	val answers: List<AnswersItem?>? = null
)

@Serializable
data class AnswersItem(

	@SerialName("answer_option_id")
	val answerOptionId: Int? = null,

	@SerialName("answer_condition")
	val answerCondition: Boolean? = null,

	@SerialName("answer_file")
	val answerFile: String? = null,

	@SerialName("question_id")
	val questionId: Int? = null,

	@SerialName("answer_id")
	val answerId: Int? = null
)
