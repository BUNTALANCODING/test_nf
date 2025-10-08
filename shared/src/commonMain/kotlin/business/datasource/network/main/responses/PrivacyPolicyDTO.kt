package business.datasource.network.main.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PrivacyPolicyDTO(
    @SerialName("cms_id") var cmsId: Int? = null,
    @SerialName("cms_code") var cmsCode: String? = null,
    @SerialName("cms_name") var cmsName: String? = null,
    @SerialName("cms_content") var cmsContent: String? = null
)