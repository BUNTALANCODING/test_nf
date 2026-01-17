package common.picklocation

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

@Composable
actual fun rememberReverseGeocoder(): ReverseGeocoder {
    val ctx = LocalContext.current.applicationContext
    return remember(ctx) { AndroidReverseGeocoder(ctx) }
}

private class AndroidReverseGeocoder(
    private val context: Context
) : ReverseGeocoder {

    override suspend fun reverse(lat: Double, lng: Double): ReverseGeocodeResult? =
        withContext(Dispatchers.IO) {
            try {
                if (!Geocoder.isPresent()) return@withContext null

                val geocoder = Geocoder(context, Locale.getDefault())
                val list = geocoder.getFromLocation(lat, lng, 1)
                val a = list?.firstOrNull() ?: return@withContext null

                a.toResult()
            } catch (_: Throwable) {
                null
            }
        }

    private fun Address.toResult(): ReverseGeocodeResult {
        val street = listOfNotNull(thoroughfare, subThoroughfare)
            .joinToString(" ")
            .trim()
            .ifBlank { null }

        val place = featureName?.takeIf { it.isNotBlank() }
        val city = locality ?: subAdminArea
        val admin = adminArea
        val country = countryName

        val title = when {
            street != null -> street
            place != null -> place
            city != null -> city
            admin != null -> admin
            country != null -> country
            else -> "Lokasi dipilih"
        }

        val subtitleParts = listOfNotNull(city, admin, country)
            .distinct()
            .filter { it.isNotBlank() }

        val subtitle = subtitleParts
            .joinToString(", ")
            .takeIf { it.isNotBlank() && it != title }

        return ReverseGeocodeResult(title = title, subtitle = subtitle)
    }
}
