package common.picklocation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import presentation.ui.main.inforute.view_model.GeoPoint

@Composable
expect fun PlatformPickLocationMap(
    initial: GeoPoint,
    onCameraMoved: (GeoPoint) -> Unit,
    modifier: Modifier = Modifier,
    focusLocation: GeoPoint? = null,
    initialZoom: Double = 16.0,
    focusZoom: Double = 18.0
)
