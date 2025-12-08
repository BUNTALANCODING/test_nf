package common.camera
//
//import android.annotation.SuppressLint
//import android.content.ContentValues
//import android.os.Environment
//import android.provider.MediaStore
//import android.util.Log
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.camera.video.FileOutputOptions
//import androidx.camera.video.MediaStoreOutputOptions
//import androidx.camera.video.Quality
//import androidx.camera.video.QualitySelector
//import androidx.camera.video.Recorder
//import androidx.camera.video.Recording
//import androidx.camera.video.VideoCapture
//import androidx.camera.video.VideoRecordEvent
//import androidx.camera.view.PreviewView
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.content.ContextCompat
//import androidx.lifecycle.compose.LocalLifecycleOwner
//import java.io.File
//import kotlin.coroutines.resume
//import kotlin.coroutines.suspendCoroutine
//
//
//@Composable
//actual fun ActualCameraVideoView(
//    onBack: () -> Unit,
//    onCaptureClick: () -> Unit,
//    isRecording: Boolean,
//    label: String,
//    onVideoRecorded: (String) -> Unit
//) {
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val mainExecutor = remember { ContextCompat.getMainExecutor(context) }
//
//    val previewView = remember { PreviewView(context) }
//    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
//
//    val videoCaptureState = remember { mutableStateOf<VideoCapture<Recorder>?>(null) }
//    val activeRecording = remember { mutableStateOf<Recording?>(null) }
//    LaunchedEffect(Unit) {
//        val cameraProvider = suspendCoroutine<ProcessCameraProvider> { continuation ->
//            cameraProviderFuture.addListener({
//                continuation.resume(cameraProviderFuture.get())
//            }, mainExecutor)
//        }
//
//        val preview = Preview.Builder().build().apply {
//            setSurfaceProvider(previewView.surfaceProvider)
//        }
//
//        val recorder = Recorder.Builder()
//            .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
//            .build()
//        val videoCapture = VideoCapture.withOutput(recorder)
//        videoCaptureState.value = videoCapture
//
//        try {
//            cameraProvider.unbindAll()
//            cameraProvider.bindToLifecycle(
//                lifecycleOwner,
//                CameraSelector.DEFAULT_BACK_CAMERA,
//                preview,
//                videoCapture
//            )
//        } catch (e: Exception) {
//            Log.e("CameraX", "Binding failed", e)
//        }
//    }
//
//    LaunchedEffect(isRecording) {
//        val videoCapture = videoCaptureState.value
//        val executor = mainExecutor
//
//        if (videoCapture == null) return@LaunchedEffect
//
//        if (isRecording) {
//            if (activeRecording.value != null) return@LaunchedEffect
//
//            val contentResolver = context.contentResolver
//            val fileName = "video_teknis_${System.currentTimeMillis()}.mp4"
//
//            val contentValues = ContentValues().apply {
//                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
//                put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
//
//                put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + "/MyAppVideos")
//            }
//
//            val outputOptions = MediaStoreOutputOptions.Builder(
//                contentResolver,
//                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
//            )
//                .setContentValues(contentValues)
//                .build()
//
//            val recording = videoCapture.output
//                .prepareRecording(context, outputOptions)
//                .start(executor) { event ->
//                    when (event) {
//                        is VideoRecordEvent.Start -> Log.d("CameraX", "Recording Started")
//                        is VideoRecordEvent.Finalize -> {
//                            if (!event.hasError()) {
//                                val videoUri = event.outputResults.outputUri.toString()
//                                Log.d("CameraX", "Video saved: $videoUri")
//                                onVideoRecorded(videoUri)
//                            } else {
//                                Log.e("CameraX", "Video Error: ${event.error}")
//                            }
//                            activeRecording.value = null
//                        }
//                    }
//                }
//            activeRecording.value = recording
//
//        } else {
//            activeRecording.value?.stop()
//        }
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
//
//        Column(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .background(Color.Black.copy(alpha = 0.8f))
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = label,
//                style = MaterialTheme.typography.labelLarge.copy(
//                    color = Color.White,
//                    textAlign = TextAlign.Center
//                )
//            )
//            Spacer(modifier = Modifier.height(26.dp))
//
//            Box(modifier = Modifier.fillMaxWidth()) {
//
//IconButton(modifier = Modifier.align(Alignment.Center).size(90.dp), onClick = onCaptureClick) {
//                    Box(
//                        modifier = Modifier
//                            .size(80.dp)
//                            .border(4.dp, Color.White, shape = CircleShape)
//                            .background(if (isRecording) Color.Red else Color(0xFF001F3F), shape = CircleShape)
//                    )
//                }
//
//            }
//        }
//    }
//}

//package common.camera

import android.content.ContentValues
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random

// Random code untuk belakang nama file, misal: A9KF, Z7Q2, dll.
fun generateRandomCode(length: Int = 4): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return (1..length)
        .map { chars[Random.nextInt(chars.length)] }
        .joinToString("")
}

@Composable
actual fun ActualCameraVideoView(
    onBack: () -> Unit,
    onCaptureClick: () -> Unit,
    isRecording: Boolean,
    label: String,
    labels: String,
    onVideoRecorded: (String) -> Unit // callback URI video
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mainExecutor = remember { ContextCompat.getMainExecutor(context) }

    val previewView = remember { PreviewView(context) }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val videoCaptureState = remember { mutableStateOf<VideoCapture<Recorder>?>(null) }
    val activeRecording = remember { mutableStateOf<Recording?>(null) }

    // Setup camera & preview
    LaunchedEffect(Unit) {
        val cameraProvider = suspendCoroutine<ProcessCameraProvider> { continuation ->
            cameraProviderFuture.addListener({
                continuation.resume(cameraProviderFuture.get())
            }, mainExecutor)
        }

        val preview = Preview.Builder().build().apply {
            setSurfaceProvider(previewView.surfaceProvider)
        }

        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
            .build()

        val videoCapture = VideoCapture.withOutput(recorder)
        videoCaptureState.value = videoCapture

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                videoCapture
            )
        } catch (e: Exception) {
            Log.e("CameraX", "Binding failed", e)
        }
    }

    // Start / stop recording
    LaunchedEffect(isRecording) {
        val videoCapture = videoCaptureState.value
        val executor = mainExecutor

        if (videoCapture == null) return@LaunchedEffect

        if (isRecording) {
            // Kalau sudah ada recording aktif, nggak usah mulai lagi
            if (activeRecording.value != null) return@LaunchedEffect

            val contentResolver = context.contentResolver

            // Format: VT-<LABEL>-<RANDOM>.mp4
            val randomCode = generateRandomCode(4)
            val fileName = "VT-${labels.uppercase()}-$randomCode.mp4"

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
                put(
                    MediaStore.Video.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_MOVIES + "/MyAppVideos"
                )
            }

            val outputOptions = MediaStoreOutputOptions.Builder(
                contentResolver,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            )
                .setContentValues(contentValues)
                .build()

            val recording = videoCapture.output
                .prepareRecording(context, outputOptions)
                .start(executor) { event ->
                    when (event) {
                        is VideoRecordEvent.Start -> {
                            Log.d("CameraX", "Recording Started")
                        }

                        is VideoRecordEvent.Finalize -> {
                            if (!event.hasError()) {
                                val videoUri = event.outputResults.outputUri.toString()
                                Log.d("CameraX", "Video saved: $videoUri")
                                onVideoRecorded(videoUri)
                            } else {
                                Log.e("CameraX", "Video Error: ${event.error}")
                            }
                            activeRecording.value = null
                        }
                    }
                }

            activeRecording.value = recording
        } else {
            // Stop jika isRecording = false
            activeRecording.value?.stop()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(alpha = 0.8f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(26.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(90.dp),
                    onClick = onCaptureClick
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .border(4.dp, Color.White, shape = CircleShape)
                            .background(
                                if (isRecording) Color.Red else Color(0xFF001F3F),
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}
