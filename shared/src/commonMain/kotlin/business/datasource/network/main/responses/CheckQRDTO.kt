package business.datasource.network.main.responses


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckQRDTO(
    @SerialName("plat_number") val platNumber: String? = "",
    @SerialName("operator_name") val operatorName: String? = "",
    @SerialName("cargo_name") val cargoName: String? = "",
    @SerialName("route") val route: String? = "",
    @SerialName("stuk_no") val stukNo: String? = "",
    @SerialName("status") val status: String? = "",
)
