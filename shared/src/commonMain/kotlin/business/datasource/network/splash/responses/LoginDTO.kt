package business.datasource.network.splash.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginDTO(

    @SerialName("token") val token: String?,
    @SerialName("officer_code") val officerCode: String?,
    @SerialName("officer_name") val officerName: String?,
    @SerialName("officer_phone") val officerPhone: String?,
    @SerialName("officer_email") val officerEmail: String?,

)