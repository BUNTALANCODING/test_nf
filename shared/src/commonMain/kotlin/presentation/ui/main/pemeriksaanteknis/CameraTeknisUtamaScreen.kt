package presentation.ui.main.pemeriksaanteknis

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import business.core.UIComponent
import com.kashif.cameraK.controller.CameraController
import com.kashif.cameraK.enums.CameraLens
import com.kashif.cameraK.enums.Directory
import com.kashif.cameraK.enums.FlashMode
import com.kashif.cameraK.enums.ImageFormat
import com.kashif.cameraK.enums.QualityPrioritization
import com.kashif.cameraK.permissions.Permissions
import com.kashif.cameraK.permissions.providePermissions
import com.kashif.cameraK.result.ImageCaptureResult
import com.kashif.cameraK.ui.CameraPreview
import com.kashif.imagesaverplugin.ImageSaverConfig
import com.kashif.imagesaverplugin.ImageSaverPlugin
import com.kashif.imagesaverplugin.rememberImageSaverPlugin
import common.PermissionCallback
import common.PermissionStatus
import common.PermissionType
import common.camera.ActualCameraVideoView
import common.createPermissionsManager
import common.toBase64
import common.toBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import presentation.component.DefaultScreenUI
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun CameraTeknisUtamaScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToVerifyPhotoKTP: () -> Unit
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "",
        startIconToolbar = Icons.Filled.Close,
        onClickStartIconToolbar = { popup() },
        isCamera = true
    ) {
        CameraTeknisUtamaContent(
            state = state,
            events = events,
            popup = popup,
            navigateToVerifyPhotoKTP = navigateToVerifyPhotoKTP
        )

    }
}

@Composable
private fun CameraTeknisUtamaContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    popup: () -> Unit,
    navigateToVerifyPhotoKTP: () -> Unit
) {

    val permissions: Permissions = providePermissions()


    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var isRecording by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showPermissionRequest by remember { mutableStateOf(false) }
    var isCameraGranted by remember { mutableStateOf(permissions.hasCameraPermission()) }
    var showDeniedDialog by remember { mutableStateOf(false) }
    var launchCamera by mutableStateOf(false)

    val permissionsManager = createPermissionsManager(object : PermissionCallback {
        override fun onPermissionStatus(permissionType: PermissionType, status: PermissionStatus) {
            when (status) {
                PermissionStatus.GRANTED -> {
                    when (permissionType) {
                        PermissionType.CAMERA -> {
                            launchCamera = true
                        }

                        PermissionType.GALLERY -> {}
                        PermissionType.NOTIFICATION -> {}
                    }
                }

                else -> showDeniedDialog = true
            }
        }
    })

// request storage permission sekali
    val storagePermissionState = remember { mutableStateOf(permissions.hasStoragePermission()) }
    if (!storagePermissionState.value) {
        permissions.RequestStoragePermission(
            onGranted = { storagePermissionState.value = true },
            onDenied = { println("Storage Permission Denied") }
        )
    }

    val imageSaverPlugin = rememberImageSaverPlugin(
        config = ImageSaverConfig(
            isAutoSave = false,
            prefix = "MyApp",
            directory = Directory.PICTURES,
            customFolderName = "MyAppPhotos"
        )
    )


    if (launchCamera) {
        if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
            isCameraGranted = true
        } else {
            permissionsManager.AskPermission(PermissionType.CAMERA)
        }
        launchCamera = false
    }


// Custom denied dialog
    if (showDeniedDialog) {
        AlertDialog(
            onDismissRequest = { showDeniedDialog = false },
            title = { Text("Camera Permission Denied") },
            text = { Text("Camera access is required. Would you like to grant it now or open settings?") },
            confirmButton = {
                TextButton(onClick = {
                    showDeniedDialog = false
                    showPermissionRequest = true
                }) { Text("Grant Now") }
            }
        )
    }

// Main camera UI
    if (isCameraGranted) {
        ActualCameraVideoView( // Pastikan Anda mengimpor atau menggunakan fully qualified name
            onBack = { popup() },
            onCaptureClick = {

                isRecording = !isRecording
            },
            isRecording = isRecording,
            label = if (isRecording) "Merekam video..." else "Pastikan video terlihat jelas.",
            onVideoRecorded = {videoUri ->
                println("video saaved: $videoUri")
            }
        )
    } else {
        // fallback UI
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Camera permission is required to proceed.")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { showPermissionRequest = true }) {
                    Text("Request Camera Permission")
                }
            }
        }
    }
}
