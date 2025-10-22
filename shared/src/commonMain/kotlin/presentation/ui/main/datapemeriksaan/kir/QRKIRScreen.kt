package presentation.ui.main.datapemeriksaan.kir

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import business.core.UIComponent
import com.kashif.cameraK.controller.CameraController
import com.kashif.cameraK.enums.Directory
import com.kashif.cameraK.permissions.Permissions
import com.kashif.cameraK.permissions.providePermissions
import com.kashif.cameraK.result.ImageCaptureResult
import com.kashif.imagesaverplugin.ImageSaverConfig
import com.kashif.imagesaverplugin.ImageSaverPlugin
import com.kashif.imagesaverplugin.rememberImageSaverPlugin
import common.PermissionCallback
import common.PermissionStatus
import common.PermissionType
import common.createPermissionsManager
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.ExperimentalResourceApi
import presentation.component.DefaultScreenUI
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import qrscanner.QrScanner
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun QRKIRScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToVerifyPhotoKTP: () -> Unit
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Foto Petugas",
        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
        onClickStartIconToolbar = { popup() },
        isCamera = true
    ) {
        QRKIRScreen(
            state = state,
            events = events,
            popup = popup,
            navigateToVerifyPhotoKTP = navigateToVerifyPhotoKTP
        )

    }
}

@Composable
private fun QRKIRScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    popup: () -> Unit,
    navigateToVerifyPhotoKTP: () -> Unit
) {

    val permissions: Permissions = providePermissions()
    val cameraController = remember { mutableStateOf<CameraController?>(null) }
    val coroutineScope = rememberCoroutineScope()


    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
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

// Loading overlay
    if (isLoading) {

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
        CameraView(
            state = state,
            events = events
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

@Composable
private fun CameraView(
    state: HomeState,
    events: (HomeEvent) -> Unit
) {

    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
        Column(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .clip(shape = RoundedCornerShape(size = 14.dp))
                    .clipToBounds()
                    .border(2.dp, Color.Gray, RoundedCornerShape(size = 14.dp)),
                contentAlignment = Alignment.Center
            ) {
                QrScanner(
                    modifier = Modifier
                        .clipToBounds()
                        .clip(shape = RoundedCornerShape(size = 14.dp)),
                    flashlightOn = false,
                    openImagePicker = false,
                    onCompletion = {
                        val delimiter = "/rfid/"
                        val rfid = it.substringAfter(delimiter)
                        println(rfid)
                        events(HomeEvent.OnUpdateQrUrl(rfid))
                        if (state.qrUrl.isNotEmpty()){
                            events(HomeEvent.CheckQR)
                        }
                    },
                    imagePickerHandler = {

                    },
                    onFailure = {
                    }
                )

            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class, ExperimentalUuidApi::class)
private suspend fun handleImageCapture(
    cameraController: CameraController,
    imageSaverPlugin: ImageSaverPlugin,
    onImageCaptured: suspend (ImageBitmap) -> Unit
) {
    when (val result = cameraController.takePicture()) {
        is ImageCaptureResult.Success -> {
            val bitmap = result.byteArray.decodeToImageBitmap()
            onImageCaptured(bitmap)

            if (!imageSaverPlugin.config.isAutoSave) {
                val customName = "Manual_${Uuid.random().toHexString()}"
                imageSaverPlugin.saveImage(
                    byteArray = result.byteArray,
                    imageName = customName
                )?.let { path ->
                    println("Image saved at: $path")
                }
            }
        }

        is ImageCaptureResult.Error -> {
            println("Image Capture Error: ${result.exception.message}")
        }
    }
}

@Composable
fun RectangleFocusOverlay(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val rectWidth = canvasWidth * 0.8f
        val rectHeight = canvasHeight * 0.3f

        val topLeft = Offset(
            x = (canvasWidth - rectWidth) / 2f,
            y = canvasHeight * 0.35f
        )

        val dashStroke = Stroke(
            width = 4.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )

        drawRect(
            color = Color.Yellow,
            topLeft = topLeft,
            size = Size(rectWidth, rectHeight),
            style = dashStroke
        )
    }
}
