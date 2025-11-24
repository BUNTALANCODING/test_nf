package business.domain.model

data class TechnicalCondition(
    val section: String,
    val questionId: Int,
    val questionName: String,
    val answerId: Int?,
    val answerName: String?
)

data class TeknisResult(
    val rampcheckId: Int,
    val filename: String,
    val status: String,
    val conditions: List<TechnicalCondition>
)
