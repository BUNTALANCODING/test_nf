package common.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import arhud.LatLng
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun KmpMapView(
    modifier: Modifier,
    routePoints: List<LatLng>,
    user: LatLng?,
    followUser: Boolean,
    showUserMarker: Boolean,
    userHeadingDeg: Float
) {
    UIKitView(
        modifier = modifier,
        factory = { IOSMapKitView() },
        update = { view ->
            (view as IOSMapKitView).update(
                routePoints = routePoints,
                user = user,
                followUser = followUser,
                showUserMarker = showUserMarker,
                userHeadingDeg = userHeadingDeg
            )
        }
    )
}

