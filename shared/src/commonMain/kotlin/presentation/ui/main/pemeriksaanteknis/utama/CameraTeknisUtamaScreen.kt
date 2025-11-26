package presentation.ui.main.pemeriksaanteknis.utama

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import business.constants.GetContext
import business.core.UIComponent
import com.kashif.cameraK.permissions.Permissions
import com.kashif.cameraK.permissions.providePermissions
import common.PermissionCallback
import common.PermissionStatus
import common.PermissionType
import common.camera.ActualCameraVideoView
import common.FileContainer
import common.createPermissionsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import presentation.component.DefaultScreenUI
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import presentation.ui.main.uploadChunk.UploadViewModel

@Composable
fun CameraTeknisUtamaScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToQuestionTeknisUtama: (String) -> Unit,
    uploadViewModel: UploadViewModel,
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
            navigateToQuestionTeknisUtama = navigateToQuestionTeknisUtama,
            uploadViewModel = uploadViewModel
        )
    }
}

@Composable
private fun CameraTeknisUtamaContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    popup: () -> Unit,
    navigateToQuestionTeknisUtama: (String) -> Unit,
    uploadViewModel: UploadViewModel
) {
    val permissions: Permissions = providePermissions()

    var isRecording by remember { mutableStateOf(false) }
    var showPermissionRequest by remember { mutableStateOf(false) }
    var isCameraGranted by remember { mutableStateOf(permissions.hasCameraPermission()) }
    var showDeniedDialog by remember { mutableStateOf(false) }
    var launchCamera by remember { mutableStateOf(false) }

    val context = GetContext.context
    val scope = rememberCoroutineScope()

    val uploadState by uploadViewModel.state.collectAsState()
    var showUploadDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uploadState.isUploading) {
        if (!uploadState.isUploading && showUploadDialog) {
            showUploadDialog = false
            uploadState.uniqueKey?.let { key ->
                navigateToQuestionTeknisUtama(key)
            }
        }
    }
    val permissionsManager = createPermissionsManager(object : PermissionCallback {
        override fun onPermissionStatus(permissionType: PermissionType, status: PermissionStatus) {
            when (status) {
                PermissionStatus.GRANTED -> {
                    if (permissionType == PermissionType.CAMERA) {
                        launchCamera = true
                    }
                }
                else -> showDeniedDialog = true
            }
        }
    })

    if (launchCamera) {
        if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
            isCameraGranted = true
        } else {
            permissionsManager.AskPermission(PermissionType.CAMERA)
        }
        launchCamera = false
    }

    if (showDeniedDialog) {
        AlertDialog(
            onDismissRequest = { showDeniedDialog = false },
            title = { Text("Camera Permission Denied") },
            text = { Text("Camera access is required!") },
            confirmButton = {
                TextButton(onClick = {
                    showDeniedDialog = false
                    showPermissionRequest = true
                }) { Text("Okay") }
            }
        )
    }

    if (isCameraGranted) {
        ActualCameraVideoView(
            onBack = { popup() },
            onCaptureClick = {
                isRecording = !isRecording
            },
            isRecording = isRecording,
            label = if (isRecording) "Merekam video..." else "Pastikan video terlihat jelas.",
            onVideoRecorded = { videoUri ->
                println("video saved: $videoUri")

                scope.launch {
                    try {
                        // DI SINI: bikin FileContainer dari Uri (String)
                        val container = FileContainer(
                            context = context,
                            rawUri = videoUri.toString()
                        )

                        uploadViewModel.setFile(container)
                        uploadViewModel.startUploadInterior()

                        showUploadDialog = true

                    } catch (e: Exception) {
                        e.printStackTrace()
                        // TODO: tampilkan snackbar / dialog error kalau mau
                    }
                }
            }
        )
    }

    if (showUploadDialog) {
        AlertDialog(
            onDismissRequest = { /* jangan di-close manual */ },
            title = { Text("Uploading ${uploadState.fileName}") },
            text = {
                Column {
                    LinearProgressIndicator(
                        progress = (uploadState.progress.coerceIn(0, 100)) / 100f,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("${uploadState.progress}% Completed")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(uploadState.status)
                }
            },
            confirmButton = {
                Row {
                    Button(onClick = { uploadViewModel.pause() }) { Text("Pause") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { uploadViewModel.resume() }) { Text("Resume") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        uploadViewModel.cancel()
                        showUploadDialog = false
                    }) { Text("Cancel") }
                }
            }
        )
    }
}
