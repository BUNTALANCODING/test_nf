package common.camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap

@Composable
expect fun CameraLivenessScreen (modifier: Modifier, onPhotoCaptured: (ImageBitmap) -> Unit)