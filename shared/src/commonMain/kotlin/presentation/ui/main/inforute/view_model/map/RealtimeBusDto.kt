package presentation.ui.main.inforute.view_model.map

// commonMain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RealtimeBusDto(
    @SerialName("bus_code") val busCode: String,
    val route: String,
    val lat: Double,
    @SerialName("long") val lng: Double,
    val corridor: String,
    @SerialName("next_halte") val nextHalte: String,
    val eta: String
)
