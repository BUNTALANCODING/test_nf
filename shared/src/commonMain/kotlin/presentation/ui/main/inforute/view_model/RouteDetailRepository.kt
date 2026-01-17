package presentation.ui.main.inforute.view_model

import presentation.ui.main.inforute.RouteStatus
import androidx.compose.runtime.Immutable

@Immutable
data class StopInfo(
    val id: String,
    val name: String,
    val badges: List<Int> = emptyList(),
    val nearestInfo: String? = null // contoh: "Halte terdekat (500 m)"
)

@Immutable
data class RouteDetail(
    val id: String,
    val number: Int,
    val title: String,
    val status: RouteStatus,
    val operationalHours: String,
    val fareText: String,
    val from: String,
    val to: String,
    val stopCount: Int,
    val activeBusCount: Int,
    val stops: List<StopInfo>
)

interface RouteDetailRepository {
    suspend fun fetchRouteDetail(routeId: String): RouteDetail
}

class FakeRouteDetailRepository : RouteDetailRepository {
    override suspend fun fetchRouteDetail(routeId: String): RouteDetail {
        return RouteDetail(
            id = routeId,
            number = 1,
            title = "Terminal Bubulak – Cidangiang",
            status = RouteStatus.ACTIVE,
            operationalHours = "05:00 - 22:00",
            fareText = "Rp 3.000",
            from = "Terminal Bubulak",
            to = "Cidangiang",
            stopCount = 25,
            activeBusCount = 4,
            stops = listOf(
                StopInfo("s1", "Terminal Bubulak", badges = listOf(5, 1)),
                StopInfo("s2", "Ruko Yasmin 1"),
                StopInfo("s3", "Radar Bogor", nearestInfo = "Halte terdekat (500 m)"),
                StopInfo("s4", "Semplak"),
                StopInfo("s5", "Transmart"),
                StopInfo("s6", "Cimanggu 1"),
                StopInfo("s7", "Ramayana Jalan Baru"),
                StopInfo("s8", "UIKA 1"),
                StopInfo("s9", "Tugu Narkoba 2"),
                StopInfo("s10", "Dinas Pendidikan"),
                StopInfo("s11", "Bantar Jati 2"),
                StopInfo("s12", "SMKN 3"),
                StopInfo("s13", "IPB MM"),
                StopInfo("s14", "PMI"),
                StopInfo("s15", "Kebun Raya"),
            )
        )
    }
}
