package common

import androidx.compose.runtime.Composable

@Composable
expect fun MapComponent(
    context: Context?,
    onLatitude: (Double) -> Unit,
    onLongitude: (Double) -> Unit,
)


data class CommonLatLng(
    val latitude: Double,
    val longitude: Double
)

@Composable
expect fun LocationFetcher(
    onLocationReceived: (CommonLatLng) -> Unit,
    onError: (String) -> Unit
)