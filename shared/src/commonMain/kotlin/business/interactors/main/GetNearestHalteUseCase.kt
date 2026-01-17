package business.interactors.main

import androidx.compose.ui.graphics.Color
import business.constants.DataStoreKeys
import business.core.AppDataStore
import business.datasource.network.main.MainService
import business.datasource.network.main.dto.request.NearestHalteRequest
import business.datasource.network.main.dto.response.BusArrivalDto
import kotlinx.serialization.json.*
import presentation.ui.main.home.BusArrivalUi
import presentation.ui.main.home.HalteTerdekatUi

class GetNearestHalteUseCase(
    private val service: MainService,
    private val dataStore: AppDataStore
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend operator fun invoke(): Pair<HalteTerdekatUi?, List<BusArrivalUi>> {
        val token = dataStore.readValue(DataStoreKeys.TOKEN)?.trim().orEmpty()
        if (token.isBlank()) throw IllegalStateException("TOKEN_EMPTY")

        val lat = dataStore.readValue(DataStoreKeys.USER_LAT)?.toDoubleOrNull()
        val lng = dataStore.readValue(DataStoreKeys.USER_LNG)?.toDoubleOrNull()
        if (lat == null || lng == null) return null to emptyList()

        val res = service.nearestHalte(
            NearestHalteRequest(lat = lat.toString(), lng = lng.toString())
        )

        if (res.status != true || res.data == null) {
            throw IllegalStateException(res.message ?: "Gagal memuat halte")
        }

        val data = res.data

        val halteUi = HalteTerdekatUi(
            title = data?.halte?.halteName ?: "",
            distanceText = data?.halte?.distance ?: "",
            operationalText = data?.halte?.operationalHour ?: "",
            statusText = data?.halte?.statusOperational ?: ""
        )


        val arrivalsDto: List<BusArrivalDto> = when (val raw = data?.busArrival) {
            is JsonArray -> raw.mapNotNull { el ->
                runCatching { json.decodeFromJsonElement(BusArrivalDto.serializer(), el) }.getOrNull()
            }
            else -> emptyList()
        }

        val arrivalsUi = arrivalsDto.map { b ->
            BusArrivalUi(
                routeNo = b.corridorCode.toString(),
                routeColor = corridorColor(b.corridorCode),
                title = b.lastDestination,
                etaMinutes = parseEtaMinutes(b.etaMin),
                clock = b.etaTime,
                routeCode = b.busCode
            )
        }

        return halteUi to arrivalsUi
    }

    private fun parseEtaMinutes(raw: String): Int =
        raw.filter { it.isDigit() }.toIntOrNull() ?: 0

    private fun corridorColor(code: Int): Color = when (code % 5) {
        0 -> Color(0xFF1E88E5)
        1 -> Color(0xFF43A047)
        2 -> Color(0xFFF4511E)
        3 -> Color(0xFF8E24AA)
        else -> Color(0xFF546E7A)
    }
}
