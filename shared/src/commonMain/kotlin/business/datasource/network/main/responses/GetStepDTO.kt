package business.datasource.network.main.responses

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GetStepDTO(

	@SerialName("questions")
	val questions: List<QuestionsItem?>? = null,

	@SerialName("step")
	val step: String? = null
)

@Serializable
data class QuestionsItem(

	@SerialName("subcategory_id")
	val subcategoryId: Int? = null,

	@SerialName("check_category_id")
	val checkCategoryId: Int? = null,

	@SerialName("question_name")
	val questionName: String? = null,

	@SerialName("subcategory_name")
	val subcategoryName: String? = null,

	@SerialName("step")
	val step: Int? = null,

	@SerialName("question_id")
	val questionId: Int? = null,

	@SerialName("check_category_name")
	val checkCategoryName: String? = null
)
