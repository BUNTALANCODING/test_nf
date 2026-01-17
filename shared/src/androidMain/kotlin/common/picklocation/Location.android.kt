package common.picklocation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import presentation.ui.main.inforute.view_model.GeoPoint

@SuppressLint("MissingPermission")
@Composable
actual fun rememberUserLocation(): State<GeoPoint?> {
    val context = LocalContext.current
    val locationState = remember { mutableStateOf<GeoPoint?>(null) }

    DisposableEffect(Unit) {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                locationState.value = GeoPoint(location.latitude, location.longitude)
            }

            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }

        val last = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (last != null) {
            locationState.value = GeoPoint(last.latitude, last.longitude)
        }

        manager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            2000L, 5f, listener
        )

        onDispose { manager.removeUpdates(listener) }
    }

    return locationState
}
