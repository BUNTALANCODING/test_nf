package business.datasource.network.main.responses

import business.domain.main.ArticleType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleTypeDTO(
    @SerialName("article_type_name") val articleType: String?,
    @SerialName("article_type_code") val articleTypeCode: String?,
    @SerialName("article_type_id") val id: Int?
)

fun ArticleTypeDTO.toArticleType() = ArticleType(articleType = articleType ?: "", articleTypeCode = articleTypeCode ?: "", id = id ?: 0)