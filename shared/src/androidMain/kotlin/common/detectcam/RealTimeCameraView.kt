package common.detectcam

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.core.content.ContextCompat
import common.detectcam.analyzer.MultiModelAnalyzer
import common.detectcam.overlay.ClassificationOverlayView
import common.detectcam.overlay.DetectionOverlayView
import common.detectcam.process.ModelInfo
import common.detectcam.process.inspectModel
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.concurrent.Executors


data class LoadedTfModel(
    val id: String,
    val interpreter: Interpreter,
    val labels: List<String>,
    val info: ModelInfo
)

data class TfModelConfig(
    val id: String,
    val modelAsset: String,
    val labelsAsset: String
)

val ALL_MODELS = listOf(
    TfModelConfig(
        id = "ban",
        modelAsset = "ban.tflite",
        labelsAsset = "labels_ban.txt"
    ),
    TfModelConfig(
        id = "yolo",
        modelAsset = "yolov11detection.tflite",
        labelsAsset = "labels.txt"
    )
)

private val SELECTED_MODEL_IDS = listOf("ban", "yolo")


@Composable
actual fun RealTimeCameraView() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val previewViewState = remember { mutableStateOf<PreviewView?>(null) }
    val overlayBoxesState = remember { mutableStateOf<DetectionOverlayView?>(null) }
    val overlayTextState = remember { mutableStateOf<ClassificationOverlayView?>(null) }

    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    val tfliteState = remember { mutableStateOf<Interpreter?>(null) }
    val labelsState = remember { mutableStateOf<List<String>>(emptyList()) }
    val modelInfoState = remember { mutableStateOf<ModelInfo?>(null) }
    val cameraProviderState = remember { mutableStateOf<ProcessCameraProvider?>(null) }

    val modelsState = remember { mutableStateOf<List<LoadedTfModel>>(emptyList()) }


    LaunchedEffect(Unit) {
        try {
            val selectedConfigs = ALL_MODELS.filter { it.id in SELECTED_MODEL_IDS }

            val loadedModels = mutableListOf<LoadedTfModel>()

            for (config in selectedConfigs) {
                Log.d("RealTimeCameraView", "Loading model=${config.modelAsset}, labels=${config.labelsAsset}")

                val labels = try {
                    FileUtil.loadLabels(context, config.labelsAsset)
                } catch (e: Exception) {
                    Log.e("RealTimeCameraView", "Gagal load labels ${config.id}: ${e.message}", e)
                    emptyList()
                }

                val model = loadModelFile(context, config.modelAsset)

                val options = Interpreter.Options().apply {
                    setNumThreads(2)
                }
                val interpreter = Interpreter(model, options)

                val info = interpreter.inspectModel()

                Log.d("RealTimeCameraView", "Model ${config.id} inspected: kind=${info.kind}, info=$info")

                loadedModels += LoadedTfModel(
                    id = config.id,
                    interpreter = interpreter,
                    labels = labels,
                    info = info
                )
            }

            modelsState.value = loadedModels
        } catch (e: Exception) {
            Log.e("RealTimeCameraView", "Error init TFLite multi model: ${e.message}", e)
        }
    }


    DisposableEffect(Unit) {
        onDispose {
            try {
                cameraProviderState.value?.unbindAll()
            } catch (e: Exception) {
                Log.e("RealTimeCameraView", "Error unbind camera: ${e.message}", e)
            }

            cameraExecutor.shutdown()
        }
    }


    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx: Context ->
            FrameLayout(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                val previewView = PreviewView(ctx).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }

                val overlayBoxes = DetectionOverlayView(ctx).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                }

                val overlayText = ClassificationOverlayView(ctx).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                }

                addView(previewView)
                addView(overlayBoxes)
                addView(overlayText)

                previewViewState.value = previewView
                overlayBoxesState.value = overlayBoxes
                overlayTextState.value = overlayText
            }
        }
    )

    LaunchedEffect(
        previewViewState.value,
        overlayBoxesState.value,
        overlayTextState.value,
        modelsState.value
    ) {
        val previewView = previewViewState.value
        val overlayBoxes = overlayBoxesState.value
        val overlayText = overlayTextState.value

        if (previewView == null) {
            Log.w("RealTimeCameraView", "previewView masih null, skip bind")
            return@LaunchedEffect
        }

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                cameraProviderState.value = cameraProvider

                val models = modelsState.value

                Log.d(
                    "RealTimeCameraView",
                    "Bind camera. models=${models.size}"
                )

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                val analyzer: ImageAnalysis.Analyzer =
                    if (models.isNotEmpty()) {
                        Log.d("RealTimeCameraView", "Using MultiModelAnalyzer")

                        MultiModelAnalyzer(
                            context = context,
                            models = models,
                            overlayBoxes = overlayBoxes,
                            overlayText = overlayText
                        )
                    } else {
                        Log.w(
                            "RealTimeCameraView",
                            "Model TFLite belum siap, pakai dummy analyzer (preview tetap jalan)"
                        )
                        ImageAnalysis.Analyzer { proxy -> proxy.close() }
                    }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(
                            cameraExecutor,
                            analyzer
                        )
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )

                Log.d("RealTimeCameraView", "Camera bindToLifecycle OK")
            } catch (e: Exception) {
                Log.e("RealTimeCameraView", "bind failed: ${e.message}", e)
            }
        }, ContextCompat.getMainExecutor(context))
    }
}

private fun loadModelFile(context: Context, modelName: String): MappedByteBuffer {
    context.assets.openFd(modelName).use { fd ->
        FileInputStream(fd.fileDescriptor).use { inputStream ->
            val fileChannel = inputStream.channel
            return fileChannel.map(
                FileChannel.MapMode.READ_ONLY,
                fd.startOffset,
                fd.declaredLength
            )
        }
    }
}
