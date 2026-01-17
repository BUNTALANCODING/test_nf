package common.map


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import platform.AVFoundation.*
import platform.CoreLocation.*
import platform.darwin.NSObject

@Composable
actual fun RequireArPermissions(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    var cameraGranted by remember { mutableStateOf(isCameraGranted()) }
    var locationGranted by remember { mutableStateOf(isLocationGranted()) }

    val manager = remember { CLLocationManager() }

    val delegate = remember {
        object : NSObject(), CLLocationManagerDelegateProtocol {
            override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
                locationGranted = isLocationGranted()
            }

            @Suppress("OVERRIDE_DEPRECATION")
            override fun locationManager(
                manager: CLLocationManager,
                didChangeAuthorizationStatus: CLAuthorizationStatus
            ) {
                locationGranted = isLocationGranted()
            }
        }
    }

    LaunchedEffect(Unit) {
        manager.delegate = delegate
        requestIosPermissions(manager) { camOk ->
            cameraGranted = camOk
            locationGranted = isLocationGranted()
        }
    }

    if (cameraGranted && locationGranted) {
        content()
    } else {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Surface(shape = RoundedCornerShape(18.dp), color = Color(0xEEFFFFFF)) {
                Column(Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Butuh izin Kamera & Lokasi untuk AR", color = Color.Black)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = {
                        requestIosPermissions(manager) { camOk ->
                            cameraGranted = camOk
                            locationGranted = isLocationGranted()
                        }
                    }) {
                        Text("Izinkan")
                    }
                }
            }
        }
    }
}

private fun isCameraGranted(): Boolean {
    return AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo) == AVAuthorizationStatusAuthorized
}

private fun isLocationGranted(): Boolean {
    val s = CLLocationManager.authorizationStatus()
    return s == kCLAuthorizationStatusAuthorizedWhenInUse || s == kCLAuthorizationStatusAuthorizedAlways
}

private fun requestIosPermissions(
    manager: CLLocationManager,
    onCameraResult: (Boolean) -> Unit
) {
    val camStatus = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
    if (camStatus == AVAuthorizationStatusNotDetermined) {
        AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
            onCameraResult(granted)
        }
    } else {
        onCameraResult(camStatus == AVAuthorizationStatusAuthorized)
    }

    val locStatus = CLLocationManager.authorizationStatus()
    if (locStatus == kCLAuthorizationStatusNotDetermined) {
        manager.requestWhenInUseAuthorization()
    }
}
