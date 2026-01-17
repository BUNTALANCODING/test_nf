package common.picklocation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLPlacemark
import platform.Foundation.NSError
import kotlin.coroutines.resume

@Composable
actual fun rememberReverseGeocoder(): ReverseGeocoder {
    return remember { IosReverseGeocoder() }
}

private class IosReverseGeocoder : ReverseGeocoder {

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun reverse(lat: Double, lng: Double): ReverseGeocodeResult? =
        withContext(Dispatchers.Main) {
            suspendCancellableCoroutine { cont ->
                val geocoder = CLGeocoder()
                val location = CLLocation(latitude = lat, longitude = lng)

                geocoder.reverseGeocodeLocation(location) { placemarks, error: NSError? ->
                    if (error != null) {
                        cont.resume(null)
                        return@reverseGeocodeLocation
                    }

                    val p = placemarks?.firstOrNull() as? CLPlacemark ?: run {
                        cont.resume(null)
                        return@reverseGeocodeLocation
                    }

                    val street = p.thoroughfare ?: ""
                    val subStreet = p.subThoroughfare ?: ""
                    val name = p.name ?: ""
                    val city = p.locality ?: ""
                    val admin = p.administrativeArea ?: ""
                    val country = p.country ?: ""

                    val title = when {
                        street.isNotBlank() && subStreet.isNotBlank() -> "$street $subStreet"
                        street.isNotBlank() -> street
                        name.isNotBlank() -> name
                        city.isNotBlank() -> city
                        admin.isNotBlank() -> admin
                        country.isNotBlank() -> country
                        else -> "Lokasi dipilih"
                    }

                    val subtitleParts = listOfNotNull(
                        city.takeIf { it.isNotBlank() },
                        admin.takeIf { it.isNotBlank() },
                        country.takeIf { it.isNotBlank() }
                    ).distinct()

                    val subtitle = subtitleParts
                        .joinToString(", ")
                        .takeIf { it.isNotBlank() && it != title }

                    cont.resume(
                        ReverseGeocodeResult(
                            title = title,
                            subtitle = subtitle
                        )
                    )
                }

                cont.invokeOnCancellation {
                    geocoder.cancelGeocode()
                }
            }
        }
}
