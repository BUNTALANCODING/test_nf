package common.camera

import android.annotation.SuppressLint
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel

import com.google.mlkit.vision.face.Face
import common.camera.facedetection.CameraLivenessManager
import common.camera.facedetection.GraphicOverlay
import org.jetbrains.compose.resources.painterResource
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_check_white
import rampcheck.shared.generated.resources.ic_img_cam_front
import rampcheck.shared.generated.resources.ic_info_fill
import java.io.File

@SuppressLint("RememberReturnType")
@Composable
actual fun CameraLivenessScreen(
    modifier: Modifier,
    onPhotoCaptured: (ImageBitmap) -> Unit
) {

    val viewModel: LivenessViewModel = viewModel()

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context).apply { scaleType = PreviewView.ScaleType.FILL_CENTER } }
    val overlay = remember { GraphicOverlay(context, null) }
    val uiState by viewModel.uiState.collectAsState()

    // ✅ Create a remembered manager instance so you can call methods on it
    val manager = remember {
        CameraLivenessManager(
            context = context,
            finderView = previewView,
            lifecycleOwner = lifecycleOwner,
            graphicOverlay = overlay
        ).apply {
            faceValidationListener = object : CameraLivenessManager.FaceValidationListener {
                override fun onFaceInBoundingBox(face: Face, inBox: Boolean) {
                    viewModel.processImage(face, inBox)
                }

                override fun onImageSaved(file: File) {
                    viewModel.handleImageSaved(context, file)
                }

                override fun onFaceNotFound() {
                    viewModel.faceNotFound()
                }
            }
        }
    }

    // ✅ Start camera
    DisposableEffect(Unit) {
        manager.startCameraForLiveness()
        onDispose { manager.stopCamera() }
    }

    //RESET buat foto ulang
    LaunchedEffect(Unit) {
        viewModel.reset()
    }

    // ✅ COLLECT takePictureEvent HERE
    // Collect take picture event here
    LaunchedEffect(Unit) {
        viewModel.takePictureEvent.collect {
            manager.takePhoto()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.photoSavedEvent.collect { base64 ->
            onPhotoCaptured(base64)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )
        AndroidView(
            factory = { overlay },
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x7F000000))
        )

        // Top instructions
        Column(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Text(
                text = "Ambil Wajah Anda",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Posisi wajah anda di garis putus-putus",
                color = Color.White,
                fontSize = 14.sp
            )
        }

        // Center actions
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.showCheck) {
                Icon(
                    painter = painterResource(Res.drawable.ic_check_white),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
            Text(
                text = when (uiState.currentStep) {
                    FaceState.INBOX -> "Tegak Lurus"
                    FaceState.TAKE_PICTURE -> "Mengambil Foto"
                    FaceState.BLINK -> "Kedipkan Mata"
                    FaceState.SMILE -> "Tersenyum"
                    FaceState.HOLD -> ""
                },
                color = Color(0xFF5B86E5),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(8.dp))
            Image(
                painter = painterResource(
                    Res.drawable.ic_img_cam_front
                ),
                contentDescription = null,
                modifier = Modifier.size(72.dp)
            )
        }

        // Error banner
        uiState.errorMessage?.let { error ->
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color(0xFFD32F2F))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_info_fill),
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = error,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}