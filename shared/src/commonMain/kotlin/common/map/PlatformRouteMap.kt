package common.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import presentation.ui.main.inforute.view_model.BusMapUi
import presentation.ui.main.inforute.view_model.GeoPoint
import presentation.ui.main.inforute.view_model.StopMapUi

@Composable
expect fun PlatformRouteMap(
    polyline: List<GeoPoint>,
    stops: List<StopMapUi>,
    buses: List<BusMapUi>,
    selectedBusId: String?,
    onBusClick: (String) -> Unit,
    onMapClick: () -> Unit,
    onBusAnchorChanged: (Offset) -> Unit,
    modifier: Modifier = Modifier
)

