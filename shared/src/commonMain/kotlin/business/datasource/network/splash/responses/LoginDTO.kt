package business.datasource.network.splash.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginDTO(

    @SerialName("token") val token: String? = null,
    @SerialName("officer_code") val officerCode: String? = null,
    @SerialName("officer_name") val officerName: String? = null,
    @SerialName("officer_phone") val officerPhone: String? = null,
    @SerialName("officer_email") val officerEmail: String? = null,

)