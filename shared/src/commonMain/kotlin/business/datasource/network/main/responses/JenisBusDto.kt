import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BusTypeResponseDto(
    val status: Boolean,
    val code: String,
    val message: String,
    val data: List<BusTypeDto>? = null
)

@Serializable
data class BusTypeDto(
    @SerialName("bus_type_id")
    val id: Int,

    @SerialName("bus_type_name")
    val name: String
)

