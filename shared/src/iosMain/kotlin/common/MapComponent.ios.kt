package common

import androidx.compose.runtime.Composable

@Composable
actual fun LocationFetcher(
    onLocationReceived: (CommonLatLng) -> Unit,
    onError: (String) -> Unit
) {

}