package common.detectcam.analyzer

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import common.detectcam.overlay.ClassificationOverlayView
import common.detectcam.process.FlexibleInputPreprocessor
import common.detectcam.process.ModelInfo
import org.tensorflow.lite.Interpreter

class ClassifierAnalyzer(
    private val tflite: Interpreter,
    private val labels: List<String>,
    private val overlayView: ClassificationOverlayView,
    modelInfo: ModelInfo
) : ImageAnalysis.Analyzer {

    private val inputPre = FlexibleInputPreprocessor(tflite)

    private val classHead = modelInfo.classificationHead
        ?: throw IllegalArgumentException("ModelInfo tanpa classificationHead")

    private val numClasses = classHead.shape[1]
    private val outputScores = Array(1) { FloatArray(numClasses) }
    private val outputs: HashMap<Int, Any> = hashMapOf(
        classHead.index to outputScores
    )

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

            analyzeBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("ClassifierAnalyzer", "err: ${e.message}", e)
        } finally {
            imageProxy.close()
        }
    }

    fun analyzeBitmap(bitmap: Bitmap) {
        try {
            val inputBuffer = try {
                inputPre.prepareInput(bitmap)
            } catch (e: Exception) {
                Log.e("ClassifierAnalyzer", "prepareInput error: ${e.message}", e)
                return
            }

            tflite.runForMultipleInputsOutputs(arrayOf(inputBuffer), outputs)

            val scores = outputScores[0]
            var bestIdx = 0
            var bestScore = scores[0]
            for (i in 1 until numClasses) {
                if (scores[i] > bestScore) {
                    bestScore = scores[i]
                    bestIdx = i
                }
            }

            val label = labels.getOrNull(bestIdx) ?: "class_$bestIdx"

            Log.d("ClassifierAnalyzer", "label=$label score=$bestScore")

            overlayView.post {
                overlayView.updateStatus(label, bestScore)
            }
        } catch (e: Exception) {
            Log.e("ClassifierAnalyzer", "analyzeBitmap err: ${e.message}", e)
        }
    }
}

