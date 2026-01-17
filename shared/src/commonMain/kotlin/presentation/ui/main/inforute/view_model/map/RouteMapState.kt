package presentation.ui.main.inforute.view_model.map

import presentation.ui.main.inforute.view_model.GeoPoint
import presentation.ui.main.inforute.view_model.StopMapUi


data class RouteMapState(
    val isLoading: Boolean = false,
    val polyline: List<GeoPoint> = emptyList(),
    val stops: List<StopMapUi> = emptyList(),
    val error: String? = null,
    val routeCode: String? = null,
    val topic: String? = null,
)
