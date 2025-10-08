package business.datasource.network.main.responses

import business.domain.main.History
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoryDTO(
    @SerialName("categories") val categories: List<CargoCategoryDTO>?,
)

fun HistoryDTO.toHistory() = History(
    historyTransaction = listOf()
)

