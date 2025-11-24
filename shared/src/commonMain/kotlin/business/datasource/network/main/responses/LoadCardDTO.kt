package business.datasource.network.main.responses

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class LoadCardDTO(

	@SerialName("LoadCardDTO")
	val loadCardDTO: List<LoadCardDTOItem?>? = null
)

@Serializable
data class LoadCardDTOItem(

	@SerialName("name")
	val name: String? = null,

	@SerialName("items")
	val items: List<ItemsItemLoadCard?>? = null
)

@Serializable
data class AnswersItemLoadCard(

	@SerialName("result_id")
	val resultId: Int? = null,

	@SerialName("text")
	val text: String? = null,

	@SerialName("answer_id")
	val answerId: Int? = null
)

@Serializable
data class ItemsItemLoadCard(

	@SerialName("name")
	val name: String? = null,

	@SerialName("answers")
	val answers: List<AnswersItemLoadCard?>? = null,

	@SerialName("question_id")
	val questionId: Int? = null
)
