package business.datasource.network.main.responses

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class QuestionDTO(

	@SerialName("checking_id")
	val checkingId: Int? = null,

	@SerialName("categories")
	val categories: List<CategoriesItem?>? = null,

	@SerialName("checking_name")
	val checkingName: String? = null,

	@SerialName("cargo_type_id")
	val cargoTypeId: Int? = null
)

@Serializable
data class ItemsItem(

	@SerialName("name")
	val name: String? = null,

	@SerialName("answers")
	val answers: List<AnswersItem?>? = null,

	@SerialName("question_id")
	val questionId: Int? = null
)

@Serializable
data class UploadType(

	@SerialName("photo")
	val photo: Boolean? = null,

	@SerialName("video")
	val video: Boolean? = null
)

@Serializable
data class CategoriesItem(

	@SerialName("name")
	val name: String? = null,

	@SerialName("subcategories")
	val subcategories: List<SubcategoriesItem?>? = null
)

@Serializable
data class AnswersItem(

	@SerialName("validation_type")
	val validationType: String? = null,

	@SerialName("upload_type")
	val uploadType: UploadType? = null,

	@SerialName("result_id")
	val resultId: Int? = null,

	@SerialName("options")
	val options: List<String?>? = null,

	@SerialName("text")
	val text: String? = null,

	@SerialName("answer_id")
	val answerId: Int? = null
)

@Serializable
data class SubcategoriesItem(

	@SerialName("name")
	val name: String? = null,

	@SerialName("items")
	val items: List<ItemsItem?>? = null
)
