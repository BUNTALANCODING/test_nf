package business.datasource.network.main.responses

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class HistoryRampcheckDTO(

	@SerialName("HistoryRampcheckDTO")
	val historyRampcheckDTO: List<HistoryRampcheckDTOItem?>? = null
)

@Serializable
data class HistoryRampcheckDTOItem(

	@SerialName("rampcheck_id")
	val rampcheckId: Int? = null,

	@SerialName("plat_number")
	val platNumber: String? = null,

	@SerialName("inspection_date")
	val inspectionDate: String? = null
)
