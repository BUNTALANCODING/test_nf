package common.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CameraPreview(modifier: Modifier) {
    UIKitView(
        modifier = modifier,
        factory = { IOSCameraPreviewView() },
        update = { it.startIfNeeded() }
    )
}
