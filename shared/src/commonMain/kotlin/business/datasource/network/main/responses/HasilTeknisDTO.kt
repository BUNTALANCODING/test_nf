// business/datasource/network/main/responses/HasilTeknisDTO.kt
package business.datasource.network.main.responses

import kotlinx.serialization.Serializable

@Serializable
data class HasilTeknisDTO(
    val status: Boolean,
    val code: String,
    val message: String,
    val data: HasilTeknisData
)

@Serializable
data class HasilTeknisData(
    val rampcheck_id: Int? = null,
    val filename: String,
    val status: String,
    val response: List<SubcategoryResponse>? = null,
    val created_at: String,
    val updated_at: String? = null
)

@Serializable
data class SubcategoryResponse(
    val subcategory_id: Int,
    val subcategory_name: String,
    val questions: List<QuestionResponse>
)

@Serializable
data class QuestionResponse(
    val question_id: Int,
    val question_name: String,
    val answer_id: Int,
    val answer_name: String
)
