package common.camera

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@Composable
actual fun ActualCameraVideoView(
    onBack: () -> Unit,
    onCaptureClick: () -> Unit,
    isRecording: Boolean,
    label: String,
    onVideoRecorded: (String) -> Unit
) {
    // 1. Dapatkan Context, Lifecycle, dan Executor
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mainExecutor = remember { ContextCompat.getMainExecutor(context) }

    // 2. Deklarasikan Objek View CameraX
    val previewView = remember { PreviewView(context) }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    // 3. State untuk Use Case dan Recording
    // Objek VideoCapture, diinisialisasi di LaunchedEffect
    val videoCaptureState = remember { mutableStateOf<VideoCapture<Recorder>?>(null) }
    // Objek Recording aktif, di-set saat START dan di-null-kan saat STOP
    val activeRecording = remember { mutableStateOf<Recording?>(null) }
    LaunchedEffect(Unit) {
        // A. Mendapatkan CameraProvider (ListenableFuture ke Suspend Function)
        val cameraProvider = suspendCoroutine<ProcessCameraProvider> { continuation ->
            cameraProviderFuture.addListener({
                continuation.resume(cameraProviderFuture.get())
            }, mainExecutor)
        }

        // B. Setup Use Case
        // 1. Preview (untuk menampilkan feed kamera)
        val preview = Preview.Builder().build().apply {
            setSurfaceProvider(previewView.surfaceProvider)
        }

        // 2. VideoCapture (untuk merekam)
        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
            .build()
        val videoCapture = VideoCapture.withOutput(recorder)
        videoCaptureState.value = videoCapture // Simpan objek ini

        // C. Binding ke Lifecycle
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                videoCapture // Binding VideoCapture use case
            )
        } catch (e: Exception) {
            Log.e("CameraX", "Binding failed", e)
        }
    }

    LaunchedEffect(isRecording) {
        val videoCapture = videoCaptureState.value
        val executor = mainExecutor // Gunakan Executor yang sama

        if (videoCapture == null) return@LaunchedEffect

        if (isRecording) {
            // === LOGIKA START RECORDING ===
            if (activeRecording.value != null) return@LaunchedEffect // Sudah merekam, abaikan

//            val file = File(context.filesDir, "video-${System.currentTimeMillis()}.mp4")
//            val outputOptions = FileOutputOptions.Builder(file).build()
            val contentResolver = context.contentResolver
            val fileName = "video_teknis_${System.currentTimeMillis()}.mp4"

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")

                put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + "/MyAppVideos")
            }

            val outputOptions = MediaStoreOutputOptions.Builder(
                contentResolver,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            )
                .setContentValues(contentValues)
                .build()

            // 2. Persiapan dan Mulai Perekaman
//            @SuppressLint("MissingPermission") // Mengharuskan izin RECORD_AUDIO di Manifest
            val recording = videoCapture.output
                .prepareRecording(context, outputOptions)
//                .withAudioEnabled() // WAJIB: Aktifkan perekaman audio
                .start(executor) { event ->
                    when (event) {
                        is VideoRecordEvent.Start -> Log.d("CameraX", "Recording Started")
                        is VideoRecordEvent.Finalize -> {
                            // Video Selesai atau Dihentikan
                            if (!event.hasError()) {
                                val videoUri = event.outputResults.outputUri.toString()
                                Log.d("CameraX", "Video saved: $videoUri")
                                onVideoRecorded(videoUri)
                            } else {
                                Log.e("CameraX", "Video Error: ${event.error}")
                            }
                            activeRecording.value = null // Reset state
                        }
                    }
                }
            activeRecording.value = recording // Simpan objek Recording aktif

        } else {
            // === LOGIKA STOP RECORDING ===
            activeRecording.value?.stop()
        }
    }

    // ... (Lanjutkan ke Langkah 4)

// 4. Struktur UI dengan AndroidView
    Box(modifier = Modifier.fillMaxSize()) {
        // A. BACKGROUND: Menampilkan Preview Kamera (View berbasis Android)
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

        // B. FOREGROUND: UI Kontrol (Overlay di atas kamera)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                // Background semi-transparan agar UI terlihat jelas
                .background(Color.Black.copy(alpha = 0.8f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Label Status
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Bar Tombol (Tutup dan Rekam)
            Box(modifier = Modifier.fillMaxWidth()) {

                // 1. Tombol Tutup (Kiri)
                TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Tutup",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Tutup", color = Color.White)
                }

                // 2. Tombol Rekam/Stop (Tengah)
                IconButton(modifier = Modifier.align(Alignment.Center), onClick = onCaptureClick) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .border(4.dp, Color.White, shape = CircleShape)
                            // Warna lingkaran dalam berubah menjadi Merah saat merekam
                            .background(if (isRecording) Color.Red else Color(0xFF001F3F), shape = CircleShape)
                    )
                }

            }
        }
    }
}