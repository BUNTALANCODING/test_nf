package common.camera

import androidx.compose.runtime.Composable

@Composable
expect fun ActualCameraVideoView(
    onBack: () -> Unit,
    onCaptureClick: () -> Unit,
    isRecording: Boolean,
    label: String,
    onVideoRecorded: (String) -> Unit,
)