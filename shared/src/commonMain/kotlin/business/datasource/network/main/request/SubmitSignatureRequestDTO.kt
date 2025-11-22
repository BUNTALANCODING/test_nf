package business.datasource.network.main.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubmitSignatureRequestDTO(
    @SerialName("rampcheck_officer_nip") val rampcheckOfficerNip: String?,
    @SerialName("rampcheck_officer_name") val rampcheckOfficerName: String?,
    @SerialName("rampcheck_officer_signature") val rampcheckOfficerSignature: String?,
    @SerialName("driver_name") val driverName: String?,
    @SerialName("driver_signature") val driverSignature: String?,
    @SerialName("kemenhub_officer_nip") val kemenhubNip: String?,
    @SerialName("kemenhub_officer_name") val kemenhubName: String?,
    @SerialName("kemenhub_officer_signature") val kemenhubSignature: String?,
    @SerialName("step") val step: String?,
    )