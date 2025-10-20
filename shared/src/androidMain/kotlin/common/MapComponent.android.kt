package common

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng


@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun LocationFetcher(
    onLocationReceived: (CommonLatLng) -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(permissionState.status) {
        if (permissionState.status.isGranted) {

            getLastKnownLocation(
                context = context,
                fusedLocationClient = fusedLocationClient,

                // Argumen ke-3: onLocationReceived
                onLocationReceivedLocation = { latLng ->
                    // Konversi LatLng Android ke CommonLatLng sebelum dikirim ke callback
                    val commonLatLng = CommonLatLng(latLng.latitude, latLng.longitude)
                    onLocationReceived(commonLatLng)
                },

                // Argumen ke-4: onLocationUnavailable (lambda baru)
                onLocationUnavailable = {
                    onError("Could not retrieve last known location. Try enabling GPS or moving.")
                }
            )
            // ------------------------------------------

        } else {
            if (!permissionState.status.shouldShowRationale) {
                permissionState.launchPermissionRequest()
            } else {
                onError("Location permission is required.")
            }
        }
    }
}

// Pastikan fungsi helper getLastKnownLocation Anda di androidMain juga memiliki 4 argumen:
@SuppressLint("MissingPermission")
private fun getLastKnownLocation(
    context: android.content.Context,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationReceivedLocation: (LatLng) -> Unit,
    onLocationUnavailable: () -> Unit // <-- Pastikan ini ada
) {
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                onLocationReceivedLocation(LatLng(it.latitude, it.longitude))
            } ?: run {
                onLocationUnavailable()
            }
        }.addOnFailureListener {
            onLocationUnavailable()
        }
    }
}