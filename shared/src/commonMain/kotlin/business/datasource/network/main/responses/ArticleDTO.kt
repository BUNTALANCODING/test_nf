package business.datasource.network.main.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleDTO(
    @SerialName("article_type_id") var articleTypeId: Int? = null,
    @SerialName("article_type_name") var articleTypeName: String? = null,
    @SerialName("article_id") var articleId: Int? = 0,
    @SerialName("article_title") var articleTitle: String? = null,
    @SerialName("article_subtitle") var articleSubtitle: String? = null,
    @SerialName("article_content") var articleContent: String? = null,
    @SerialName("article_date") var articleDate: String? = null,
    @SerialName("article_img") var articleImg: String? = null
)