package business.datasource.network.main.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TripResponse(
    @SerialName("bus_active")
    val busActive: Int = 0,

    @SerialName("total_halte")
    val totalHalte: Int = 0,

    @SerialName("halte_start")
    val halteStart: String? = null,

    @SerialName("halte_end")
    val halteEnd: String? = null,

    @SerialName("current_route")
    val currentRoute: String? = null,

    @SerialName("next_route")
    val nextRoute: String? = null,

    @SerialName("list_halte")
    val listHalte: List<TripHalte> = emptyList()
)

@Serializable
data class TripHalte(
    @SerialName("c_halte") val cHalte: String? = null,
    @SerialName("n_halte") val nHalte: String? = null,
    @SerialName("i_seq") val iSeq: Int? = null,
    @SerialName("distance") val distance: String? = null,
    @SerialName("is_nearest") val isNearest: Boolean? = null,
    @SerialName("other_corridors") val otherCorridors: List<String> = emptyList()
)
