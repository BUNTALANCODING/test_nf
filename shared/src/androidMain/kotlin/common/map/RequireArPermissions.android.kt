package common.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

@Composable
actual fun RequireArPermissions(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    val ctx = LocalContext.current

    val required = remember {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    fun hasAllNow(): Boolean =
        required.all { p -> ContextCompat.checkSelfPermission(ctx, p) == PackageManager.PERMISSION_GRANTED }

    var granted by remember { mutableStateOf(hasAllNow()) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { res ->
        val camOk = res[Manifest.permission.CAMERA] == true
        val fineOk = res[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseOk = res[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        granted = camOk && (fineOk || coarseOk)
    }

    LaunchedEffect(Unit) {
        if (!granted) launcher.launch(required)
    }

    if (granted) {
        content()
    } else {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Surface(shape = RoundedCornerShape(18.dp), color = Color(0xEEFFFFFF)) {
                Column(Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Butuh izin Kamera & Lokasi untuk AR", color = Color.Black)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { launcher.launch(required) }) { Text("Izinkan") }
                }
            }
        }
    }
}
