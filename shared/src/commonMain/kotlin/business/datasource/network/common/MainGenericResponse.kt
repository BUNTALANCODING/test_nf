package business.datasource.network.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MainGenericResponse<T>(
    @SerialName("status") var status: Boolean?,
    @SerialName("code") var code: String?,
    @SerialName("message") var message: String?,
    @SerialName("data") var data: T?,
    @SerialName("alert") var alert: JAlertResponse? = JAlertResponse(),
)

@Serializable
data class ValidateResponse<T>(
    @SerialName("status") var status: Boolean?,
    @SerialName("code") var code: String?,
    @SerialName("message") var message: String?,
)