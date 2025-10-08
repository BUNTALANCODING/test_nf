package business.datasource.network.main.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationRequestDTO(
    @SerialName("notification_id") var id: Int? = null
)
