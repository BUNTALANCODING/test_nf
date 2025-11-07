package presentation.ui.main.pemeriksaanteknis

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import business.constants.GetContext
import business.core.UIComponent
import com.kashif.cameraK.enums.Directory
import com.kashif.cameraK.permissions.Permissions
import com.kashif.cameraK.permissions.providePermissions
import com.kashif.imagesaverplugin.ImageSaverConfig
import com.kashif.imagesaverplugin.rememberImageSaverPlugin
import common.PermissionCallback
import common.PermissionStatus
import common.PermissionType
import common.PlatformFile
import common.VideoUploader
import common.camera.ActualCameraVideoView
import common.copyUriToTempFile
import common.createPermissionsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import presentation.component.DefaultScreenUI
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState

@Composable
fun CameraTeknisUtamaScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToQuestionTeknisUtama: () -> Unit
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
            navigateToQuestionTeknisUtama = navigateToQuestionTeknisUtama
        )

    }
}

@Composable
private fun CameraTeknisUtamaContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    popup: () -> Unit,
    navigateToQuestionTeknisUtama: () -> Unit
) {

    val permissions: Permissions = providePermissions()


    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var isRecording by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showPermissionRequest by remember { mutableStateOf(false) }
    var isCameraGranted by remember { mutableStateOf(permissions.hasCameraPermission()) }
    var showDeniedDialog by remember { mutableStateOf(false) }
    var launchCamera by mutableStateOf(false)

    val context = GetContext.context
    val scope = rememberCoroutineScope()


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

                scope.launch {
                    try {

                        val tempFile = copyUriToTempFile(videoUri)

                        // Dapatkan path yang dibutuhkan oleh WorkManager
                        val filePath = tempFile

                        println("File path for upload: $filePath")

                        // **LANGKAH 2: Panggil Event ViewModel**
                        // Ini akan memicu HomeViewModel untuk mendapatkan token dan menjadwalkan WorkManager.
                        events(HomeEvent.UploadVideo(filePath))

                        // **LANGKAH 3: Navigasi**
                        // Setelah WorkManager diantrikan, kita bisa navigasi atau menutup kamera.
                        navigateToQuestionTeknisUtama()

                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Tampilkan error ke pengguna (misal: "Gagal menyiapkan file untuk unggah")
                    }
                    /*try {
                        val tempFile = copyUriToTempFile(videoUri)
                        val uploader = VideoUploader(context)
                        val uploadToken =

                        val workId = uploader.uploadVideo(tempFile, uploadToken)
                        println("Upload started with WorkId = $workId")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }*/
                }
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
