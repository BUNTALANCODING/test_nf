package business.domain.main

import androidx.compose.ui.graphics.vector.ImageVector
import business.datasource.network.main.responses.Details

data class Ferry(
    var scheduleId: Int? = 0,
    var routeId: Int? = 0,
    var departureTime: String? = "",
    var shipId: Int? = 0,
    var availableCapacity: Int? = 0,
    var shipImages: List<String> = listOf(),
    var details: List<Details> = listOf(),
    var totalAmount: Int? = 0
)

data class FerryFacility(
    val icon: ImageVector,
    val label: String
)
