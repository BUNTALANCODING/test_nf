package business.datasource.network.main.request

import business.constants.TrxStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionRequestDTO(
    @SerialName("status_transaction") var statusTrx: Int? = TrxStatus.ALL,
)
