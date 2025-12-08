package common.detectcam.analyzer

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import common.detectcam.overlay.ClassificationOverlayView
import common.detectcam.overlay.DetectionOverlayView
import common.detectcam.LoadedTfModel
import common.detectcam.process.ModelKind

class MultiModelAnalyzer(
    context: Context,
    models: List<LoadedTfModel>,
    private val overlayBoxes: DetectionOverlayView?,
    private val overlayText: ClassificationOverlayView?
) : ImageAnalysis.Analyzer {

    private val classifierAnalyzers: List<ClassifierAnalyzer>
    private val detectorAnalyzers: List<DetectorAnalyzer>

    init {
        val clfs = mutableListOf<ClassifierAnalyzer>()
        val dets = mutableListOf<DetectorAnalyzer>()

        for (model in models) {
            when (model.info.kind) {
                ModelKind.CLASSIFIER -> {
                    val ovText = overlayText ?: continue
                    clfs += ClassifierAnalyzer(
                        tflite = model.interpreter,
                        labels = model.labels,
                        overlayView = ovText,
                        modelInfo = model.info
                    )
                }
                ModelKind.DETECTOR -> {
                    val ov = overlayBoxes ?: continue
                    dets += DetectorAnalyzer(
                        context = context,
                        tflite = model.interpreter,
                        labels = model.labels,
                        overlayView = ov,
                        modelInfo = model.info
                    )
                }
                ModelKind.UNKNOWN -> {
                    Log.w("MultiModelAnalyzer", "Model ${model.id} kind UNKNOWN, skip")
                }
            }
        }

        classifierAnalyzers = clfs
        detectorAnalyzers = dets
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        try {
            var bitmap: Bitmap = imageProxy.toBitmap() ?: return

            val rotation = imageProxy.imageInfo.rotationDegrees
            if (rotation != 0) {
                val matrix = Matrix().apply { postRotate(rotation.toFloat()) }
                bitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    matrix,
                    true
                )
            }

            // jalankan semua model ke bitmap yang sama
            classifierAnalyzers.forEach { it.analyzeBitmap(bitmap) }
            detectorAnalyzers.forEach { it.analyzeBitmap(bitmap) }

        } catch (e: Exception) {
            Log.e("MultiModelAnalyzer", "err: ${e.message}", e)
        } finally {
            imageProxy.close()
        }
    }
}
