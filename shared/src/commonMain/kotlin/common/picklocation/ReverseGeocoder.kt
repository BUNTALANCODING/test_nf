package common.picklocation

import androidx.compose.runtime.Composable

data class ReverseGeocodeResult(
    val title: String,
    val subtitle: String? = null
)

interface ReverseGeocoder {
    suspend fun reverse(lat: Double, lng: Double): ReverseGeocodeResult?
}

@Composable
expect fun rememberReverseGeocoder(): ReverseGeocoder
