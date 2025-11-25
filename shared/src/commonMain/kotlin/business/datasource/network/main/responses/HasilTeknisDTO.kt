// business/datasource/network/main/responses/HasilTeknisDTO.kt
package business.datasource.network.main.responses

import kotlinx.serialization.Serializable

@Serializable
data class HasilTeknisDTO(
    val status: Boolean = false,
    val code: String? = null,
    val message: String? = null,
    val data: HasilTeknisData = HasilTeknisData()
)

@Serializable
data class HasilTeknisData(
    val rampcheck_id: Int? = null,
    val filename: String? = null,
    val status: String? = null,
    val response: List<SubcategoryResponse>? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)

@Serializable
data class SubcategoryResponse(
    val subcategory_id: Int? = null,
    val subcategory_name: String? = null,
    val questions: List<QuestionResponse> = listOf()
)

@Serializable
data class QuestionResponse(
    val question_id: Int? = null,
    val question_name: String? = null,
    val answer_id: Int? = null,
    val answer_name: String? = null
)
