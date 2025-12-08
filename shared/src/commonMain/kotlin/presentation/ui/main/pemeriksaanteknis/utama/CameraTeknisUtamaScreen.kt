package presentation.ui.main.pemeriksaanteknis.utama

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import business.constants.GetContext
import business.core.UIComponent
import com.kashif.cameraK.permissions.Permissions
import com.kashif.cameraK.permissions.providePermissions
import common.FileContainer
import common.KeepScreenOn
import common.PermissionCallback
import common.PermissionStatus
import common.PermissionType
import common.camera.ActualCameraVideoView
import common.createPermissionsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import presentation.component.DefaultScreenUI
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import presentation.ui.main.pemeriksaanteknis.utama.viewmodel.UploadViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow


private enum class CameraUiState {
    PREVIEW,
    RECORDING,
    REVIEW
}

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

    var cameraUiState by remember { mutableStateOf(CameraUiState.PREVIEW) }
    var recordedVideoUri by remember { mutableStateOf<String?>(null) }
    val isRecording = cameraUiState == CameraUiState.RECORDING

    var recordSeconds by remember { mutableStateOf(0) }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            while (true) {
                delay(1_000)
                recordSeconds++
            }
        }
    }

    var isCameraGranted by remember { mutableStateOf(permissions.hasCameraPermission()) }
    var showDeniedDialog by remember { mutableStateOf(false) }
    var launchCamera by remember { mutableStateOf(false) }

    val context = GetContext.context
    val scope = rememberCoroutineScope()

    val uploadState by uploadViewModel.state.collectAsState()
    var showUploadDialog by remember { mutableStateOf(false) }

    val stillWaiting = isRecording || uploadState.isUploading || showUploadDialog
    KeepScreenOn(keepOn = stillWaiting)

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
                TextButton(onClick = { showDeniedDialog = false }) {
                    Text("Okay")
                }
            }
        )
    }

    if (uploadState.showLimitDialog) {
        AlertDialog(
            onDismissRequest = {
                uploadViewModel.dismissLimitDialog()
                cameraUiState = CameraUiState.PREVIEW
                recordedVideoUri = null
                recordSeconds = 0
            },
            title = { Text("Video tidak bisa diupload") },
            text = { Text(uploadState.limitDialogMessage) },
            confirmButton = {
                TextButton(
                    onClick = {
                        uploadViewModel.dismissLimitDialog()
                        cameraUiState = CameraUiState.PREVIEW
                        recordedVideoUri = null
                        recordSeconds = 0
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    if (isCameraGranted) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            ActualCameraVideoView(
                labels = "EXTERIOR",
                onBack = { popup() },
                onCaptureClick = {
                    if (cameraUiState != CameraUiState.REVIEW) {
                        cameraUiState = if (cameraUiState == CameraUiState.RECORDING) {
                            CameraUiState.PREVIEW
                        } else {
                            recordSeconds = 0
                            CameraUiState.RECORDING
                        }
                    }
                },
                isRecording = isRecording,
                label = when (cameraUiState) {
                    CameraUiState.PREVIEW -> "Pastikan video terlihat jelas."
                    CameraUiState.RECORDING -> "Merekam video..."
                    CameraUiState.REVIEW -> "Video selesai direkam."
                },
                onVideoRecorded = { videoUri ->
                    println("video saved: $videoUri")
                    recordedVideoUri = videoUri.toString()
                    cameraUiState = CameraUiState.REVIEW
                }
            )

            if (cameraUiState == CameraUiState.RECORDING || cameraUiState == CameraUiState.REVIEW) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp)
                        .background(
                            color = Color.Red,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = formatRecordTime(recordSeconds),
                        color = Color.White
                    )
                }
            }

            if (cameraUiState == CameraUiState.REVIEW) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color.Black)
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Pastikan video sudah sesuai.",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            border = BorderStroke(1.dp, Color.White),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            ),
                            onClick = {
                                recordedVideoUri = null
                                cameraUiState = CameraUiState.PREVIEW
                                recordSeconds = 0
                            }
                        ) {
                            Text("Ambil Ulang")
                        }

                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            ),
                            onClick = {
                                recordedVideoUri?.let { uri ->
                                    scope.launch {
                                        try {
                                            val container = FileContainer(
                                                context = context,
                                                rawUri = uri
                                            )

                                            uploadViewModel.setFile(container)
                                            uploadViewModel.startUploadInterior()

                                            val current = uploadViewModel.state.value
                                            if (!current.showLimitDialog && current.isUploading) {
                                                showUploadDialog = true
                                            } else if (current.showLimitDialog) {
                                                cameraUiState = CameraUiState.PREVIEW
                                                recordedVideoUri = null
                                                recordSeconds = 0
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }
                                }
                            }
                        ) {
                            Text("Unggah")
                        }
                    }
                }
            }
        }
    }

    if (showUploadDialog) {

        AlertDialog(
            onDismissRequest = { },
            title = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Uploading",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = uploadState.fileName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ModernProgressBar(
                            progress = uploadState.progress,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(999.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${uploadState.progress}%",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Text(
                        text = uploadState.status,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    FilledIconButton(
                        onClick = {
                            if (uploadState.isPaused) {
                                uploadViewModel.resume()
                            } else {
                                uploadViewModel.pause()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (uploadState.isPaused) {
                                Icons.Filled.PlayArrow
                            } else {
                                Icons.Filled.Pause
                            },
                            contentDescription = if (uploadState.isPaused) {
                                "Resume upload"
                            } else {
                                "Pause upload"
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedIconButton(
                        onClick = {
                            uploadViewModel.cancel()
                            showUploadDialog = false
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "Cancel upload"
                        )
                    }
                }
            }
        )

    }
}

private fun formatRecordTime(totalSeconds: Int): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    fun twoDigits(value: Int): String =
        if (value < 10) "0$value" else value.toString()

    return "${twoDigits(hours)}:${twoDigits(minutes)}:${twoDigits(seconds)}"
}

@Composable
private fun ModernProgressBar(
    progress: Int,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0, 100) / 100f,
        label = "uploadProgress"
    )

    Box(
        modifier = modifier
            .height(8.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.tertiary
                        )
                    )
                )
        )
    }
}


