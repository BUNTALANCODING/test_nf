package business.datasource.network.main.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoryRampcheckRequestDTO(
    @SerialName("status") val status: Int?,
)
