//package presentation.util
//
//import androidx.compose.runtime.*
//import androidx.compose.ui.graphics.ImageBitmap
//import common.*
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//@Composable
//fun rememberImageHandler(
//    onImageSelected: (String) -> Unit
//): ImageHandlerState {
//    val coroutineScope = rememberCoroutineScope()
//
//    var launchCamera by remember { mutableStateOf(false) }
//    var launchGallery by remember { mutableStateOf(false) }
//    var launchSetting by remember { mutableStateOf(false) }
//    var showLoading by remember { mutableStateOf(false) }
//    var showDialogSuccess by remember { mutableStateOf(false) }
//    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
//
//    val permissionsManager = createPermissionsManager(object : PermissionCallback {
//        override fun onPermissionStatus(
//            permissionType: PermissionType,
//            status: PermissionStatus
//        ) {
//            when (status) {
//                PermissionStatus.GRANTED -> {
//                    when (permissionType) {
//                        PermissionType.CAMERA -> launchCamera = true
//                        PermissionType.GALLERY -> launchGallery = true
//                        else -> Unit
//                    }
//                }
//                else -> launchSetting = true
//            }
//        }
//    })
//
//    val cameraManager = rememberCameraManager {
//        coroutineScope.launch {
//            showLoading = true
//            showDialogSuccess = true
//            val bitmap = withContext(Dispatchers.Default) { it?.toImageBitmap() }
//            imageBitmap = bitmap
//        }
//    }
//
//    val galleryManager = rememberGalleryManager {
//        coroutineScope.launch {
//            val bitmap = withContext(Dispatchers.Default) { it?.toImageBitmap() }
//            imageBitmap = bitmap
//        }
//    }
//
//    // Handle image selection
//    LaunchedEffect(imageBitmap) {
//        imageBitmap?.let { bitmap ->
//            val base64 = withContext(Dispatchers.Default) {
//                bitmap.toBytes().toBase64()
//            }
//            onImageSelected(base64)
//            imageBitmap = null
//        }
//    }
//
//    // Handle permission launches
//    LaunchedEffect(launchCamera, launchGallery, launchSetting) {
//        when {
//            launchGallery -> {
//                if (permissionsManager.isPermissionGranted(PermissionType.GALLERY)) {
//                    galleryManager.launch()
//                } else {
//                    permissionsManager.AskPermission(PermissionType.GALLERY)
//                }
//                launchGallery = false
//            }
//            launchCamera -> {
//                if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
//                    cameraManager.launch()
//                } else {
//                    permissionsManager.AskPermission(PermissionType.CAMERA)
//                }
//                launchCamera = false
//            }
//            launchSetting -> {
//                permissionsManager.LaunchSettings()
//                launchSetting = false
//            }
//        }
//    }
//
//    return ImageHandlerState(
//        launchCamera = { launchCamera = true },
//        launchGallery = { launchGallery = true },
//        showLoading = showLoading,
//        showDialogSuccess = showDialogSuccess,
//        updateLoading = { showLoading = it },
//        updateDialogSuccess = { showDialogSuccess = it }
//    )
//}
//
//data class ImageHandlerState(
//    val launchCamera: () -> Unit,
//    val launchGallery: () -> Unit,
//    val showLoading: Boolean,
//    val showDialogSuccess: Boolean,
//    val updateLoading: (Boolean) -> Unit,
//    val updateDialogSuccess: (Boolean) -> Unit
//)
