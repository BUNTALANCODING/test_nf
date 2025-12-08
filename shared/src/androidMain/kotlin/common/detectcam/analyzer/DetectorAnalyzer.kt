//package common.detectcam
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.Matrix
//import android.graphics.RectF
//import android.util.Log
//import androidx.camera.core.ImageAnalysis
//import androidx.camera.core.ImageProxy
//import org.tensorflow.lite.Interpreter
//
//data class DetectionResult(
//    val rect: RectF,
//    val score: Float,
//    val label: String
//)
//
//class DetectorAnalyzer(
//    private val context: Context,
//    private val tflite: Interpreter,
//    private val labels: List<String>,
//    private val overlayView: DetectionOverlayView,
//    modelInfo: ModelInfo
//) : ImageAnalysis.Analyzer {
//
//    private val inputPre = FlexibleInputPreprocessor(tflite)
//    private val inputSpec = inputPre.spec
//    private val modelInputWidth = inputSpec.width
//    private val modelInputHeight = inputSpec.height
//
//    private data class YoloOutputLayout(
//        val boxesIndex: Int,
//        val scoresIndex: Int?,
//        val classesIndex: Int?,
//        val batchSize: Int,
//        val numBoxes: Int,
//        val boxDim: Int
//    )
//
//    private val layout: YoloOutputLayout
//    private var outputBoxes: Array<Array<FloatArray>>
//    private var outputScores: Array<FloatArray>? = null
//    private var outputClasses: Array<FloatArray>? = null
//
//    private val outputs: HashMap<Int, Any> = hashMapOf()
//
//    @Volatile
//    private var frameIndex: Int = 0
//
//    private data class RawDetection(
//        val x1: Float,
//        val y1: Float,
//        val x2: Float,
//        val y2: Float,
//        val score: Float,
//        val classId: Int
//    )
//
//    private var lastDetections: List<DetectionResult> = emptyList()
//    private val smoothAlpha = 0.6f
//
//    init {
//        val infos = tflite.getAllOutputInfos()
//        val boxInfo = modelInfo.detectionHead
//            ?: infos.first { it.shape.size == 3 && it.shape[0] == 1 && it.shape[2] >= 4 }
//
//        val numBoxes = boxInfo.shape[1]
//        val boxDim = boxInfo.shape[2]
//        val batchSize = boxInfo.shape[0]
//
//        outputBoxes = Array(batchSize) { Array(numBoxes) { FloatArray(boxDim) } }
//        outputs[boxInfo.index] = outputBoxes as Any
//
//        val scoreInfo = infos.firstOrNull {
//            it.index != boxInfo.index &&
//                    it.shape.size == 2 &&
//                    it.shape[0] == 1 &&
//                    it.shape[1] == numBoxes
//        }
//        if (scoreInfo != null) {
//            outputScores = Array(1) { FloatArray(numBoxes) }
//            outputs[scoreInfo.index] = outputScores as Any
//        }
//
//        val classInfo = infos.firstOrNull {
//            it.index != boxInfo.index &&
//                    it.index != scoreInfo?.index &&
//                    it.shape.size == 2 &&
//                    it.shape[0] == 1 &&
//                    it.shape[1] == numBoxes
//        }
//        if (classInfo != null) {
//            outputClasses = Array(1) { FloatArray(numBoxes) }
//            outputs[classInfo.index] = outputClasses as Any
//        }
//
//        layout = YoloOutputLayout(
//            boxesIndex = boxInfo.index,
//            scoresIndex = scoreInfo?.index,
//            classesIndex = classInfo?.index,
//            batchSize = batchSize,
//            numBoxes = numBoxes,
//            boxDim = boxDim
//        )
//
//        Log.d(
//            "DetectorAnalyzer",
//            "layout: boxes=${layout.boxesIndex}, scores=${layout.scoresIndex}, classes=${layout.classesIndex}, numBoxes=${layout.numBoxes}, boxDim=${layout.boxDim}"
//        )
//    }
//
//    private val localScoreThreshold = 0.25f
//    private val minBoxSizePx = 12f
//    private val maxDetectionsPerFrame = 50
//
//    @SuppressLint("UnsafeOptInUsageError")
//    override fun analyze(imageProxy: ImageProxy) {
//        try {
//            val currentIndex = frameIndex++
//            if (currentIndex % 2 != 0) {
//                return
//            }
//
//            var bitmap: Bitmap = imageProxy.toBitmap() ?: return
//
//            val rotation = imageProxy.imageInfo.rotationDegrees
//            if (rotation != 0) {
//                val matrix = Matrix().apply { postRotate(rotation.toFloat()) }
//                bitmap = Bitmap.createBitmap(
//                    bitmap,
//                    0,
//                    0,
//                    bitmap.width,
//                    bitmap.height,
//                    matrix,
//                    true
//                )
//            }
//
//            val inputBuffer = try {
//                inputPre.prepareInput(bitmap)
//            } catch (e: Exception) {
//                Log.e("DetectorAnalyzer", "prepareInput error: ${e.message}", e)
//                return
//            }
//
//            tflite.runForMultipleInputsOutputs(arrayOf(inputBuffer), outputs)
//
//            val imgW = bitmap.width.toFloat()
//            val imgH = bitmap.height.toFloat()
//            val imgArea = imgW * imgH
//
//            val boxesArray = outputBoxes
//            val scoresArray = outputScores
//            val classesArray = outputClasses
//
//            val numBoxes = layout.numBoxes
//            val boxDim = layout.boxDim
//
//            var maxCoord = 0f
//            for (i in 0 until numBoxes) {
//                val b = boxesArray[0][i]
//                if (b.size < 4) continue
//                maxCoord = maxCoord.coerceAtLeast(maxOf(b[0], b[1], b[2], b[3]))
//            }
//            val coordsAreNormalized = maxCoord <= 2f
//
//            val rawList = mutableListOf<RawDetection>()
//
//            for (i in 0 until numBoxes) {
//                val box = boxesArray[0][i]
//                if (box.size < 4) continue
//
//                val score = if (scoresArray != null) {
//                    scoresArray[0][i]
//                } else {
//                    if (boxDim > 4) box[4] else 1f
//                }
//                if (score < localScoreThreshold) continue
//
//                val classId = if (classesArray != null) {
//                    classesArray[0][i].toInt().coerceAtLeast(0)
//                } else {
//                    0
//                }
//
//                // ASUMSI: box = [x1,y1,x2,y2] di ruang input model
//                val (x1Model, y1Model, x2Model, y2Model) = if (coordsAreNormalized) {
//                    floatArrayOf(
//                        box[0] * modelInputWidth,
//                        box[1] * modelInputHeight,
//                        box[2] * modelInputWidth,
//                        box[3] * modelInputHeight
//                    )
//                } else {
//                    floatArrayOf(box[0], box[1], box[2], box[3])
//                }
//
//                val mapped = inputPre.mapBoxToOriginal(
//                    x1Model, y1Model, x2Model, y2Model,
//                    imgW, imgH
//                )
//
//                var x1 = mapped.left
//                var y1 = mapped.top
//                var x2 = mapped.right
//                var y2 = mapped.bottom
//
//                if (x2 <= x1 || y2 <= y1) continue
//
//                x1 = x1.coerceIn(0f, imgW)
//                y1 = y1.coerceIn(0f, imgH)
//                x2 = x2.coerceIn(0f, imgW)
//                y2 = y2.coerceIn(0f, imgH)
//
//                val wPx = x2 - x1
//                val hPx = y2 - y1
//                if (wPx < minBoxSizePx || hPx < minBoxSizePx) continue
//
//                val area = wPx * hPx
//                val relArea = area / imgArea
//                if (relArea < 0.001f || relArea > 0.95f) continue
//
//                rawList.add(
//                    RawDetection(
//                        x1 = x1,
//                        y1 = y1,
//                        x2 = x2,
//                        y2 = y2,
//                        score = score,
//                        classId = classId
//                    )
//                )
//            }
//
//            if (rawList.isEmpty()) {
//                overlayView.post {
//                    overlayView.updateDetections(emptyList(), imgW, imgH)
//                }
//                return
//            }
//
//            val topRaw = rawList.sortedByDescending { it.score }
//                .take(maxDetectionsPerFrame * 3)
//
//            val detections = topRaw.map { r ->
//                val label = if (r.classId in labels.indices) {
//                    labels[r.classId]
//                } else r.classId.toString()
//
//                DetectionResult(
//                    rect = RectF(r.x1, r.y1, r.x2, r.y2),
//                    score = r.score,
//                    label = label
//                )
//            }
//
//            val afterNms = nonMaxSuppression(detections, 0.45f)
//            val finalDetections = afterNms.sortedByDescending { it.score }
//                .take(maxDetectionsPerFrame)
//
//            val smoothed = smoothWithPrevious(finalDetections)
//
//            overlayView.post {
//                overlayView.updateDetections(smoothed, imgW, imgH)
//            }
//        } catch (e: Exception) {
//            Log.e("DetectorAnalyzer", "err: ${e.message}", e)
//        } finally {
//            imageProxy.close()
//        }
//    }
//
//    private fun smoothWithPrevious(current: List<DetectionResult>): List<DetectionResult> {
//        if (lastDetections.isEmpty()) {
//            lastDetections = current
//            return current
//        }
//
//        val result = mutableListOf<DetectionResult>()
//        val usedPrev = BooleanArray(lastDetections.size)
//
//        for (d in current) {
//            var bestIdx = -1
//            var bestIou = 0f
//            for ((idx, p) in lastDetections.withIndex()) {
//                if (usedPrev[idx]) continue
//                if (d.label != p.label) continue
//                val iouVal = iou(d.rect, p.rect)
//                if (iouVal > bestIou) {
//                    bestIou = iouVal
//                    bestIdx = idx
//                }
//            }
//            if (bestIdx >= 0 && bestIou > 0.3f) {
//                val p = lastDetections[bestIdx]
//                usedPrev[bestIdx] = true
//                val newRect = RectF(
//                    lerp(p.rect.left,   d.rect.left,   smoothAlpha),
//                    lerp(p.rect.top,    d.rect.top,    smoothAlpha),
//                    lerp(p.rect.right,  d.rect.right,  smoothAlpha),
//                    lerp(p.rect.bottom, d.rect.bottom, smoothAlpha)
//                )
//                result.add(d.copy(rect = newRect))
//            } else {
//                result.add(d)
//            }
//        }
//
//        lastDetections = result
//        return result
//    }
//
//    private fun lerp(a: Float, b: Float, alpha: Float): Float =
//        a + (b - a) * alpha
//
//    private fun nonMaxSuppression(
//        boxes: List<DetectionResult>,
//        iouThreshold: Float
//    ): List<DetectionResult> {
//        val out = mutableListOf<DetectionResult>()
//        val list = boxes.sortedByDescending { it.score }.toMutableList()
//        while (list.isNotEmpty()) {
//            val best = list.removeAt(0)
//            out.add(best)
//            val iter = list.iterator()
//            while (iter.hasNext()) {
//                val other = iter.next()
//                if (iou(best.rect, other.rect) > iouThreshold) {
//                    iter.remove()
//                }
//            }
//        }
//        return out
//    }
//
//    private fun iou(a: RectF, b: RectF): Float {
//        val interLeft = maxOf(a.left, b.left)
//        val interTop = maxOf(a.top, b.top)
//        val interRight = minOf(a.right, b.right)
//        val interBottom = minOf(a.bottom, b.bottom)
//
//        val interW = maxOf(0f, interRight - interLeft)
//        val interH = maxOf(0f, interBottom - interTop)
//        val interArea = interW * interH
//
//        val areaA = (a.right - a.left) * (a.bottom - a.top)
//        val areaB = (b.right - b.left) * (b.bottom - b.top)
//        val union = areaA + areaB - interArea
//        return if (union <= 0f) 0f else interArea / union
//    }
//}

package common.detectcam.analyzer

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import common.detectcam.overlay.DetectionOverlayView
import common.detectcam.process.FlexibleInputPreprocessor
import common.detectcam.process.ModelInfo
import common.detectcam.process.getAllOutputInfos
import org.tensorflow.lite.Interpreter

data class DetectionResult(
    val rect: RectF,
    val score: Float,
    val label: String
)

class DetectorAnalyzer(
    private val context: Context,
    private val tflite: Interpreter,
    private val labels: List<String>,
    private val overlayView: DetectionOverlayView,
    modelInfo: ModelInfo
) : ImageAnalysis.Analyzer {

    private val inputPre = FlexibleInputPreprocessor(tflite)
    private val inputSpec = inputPre.spec
    private val modelInputWidth = inputSpec.width
    private val modelInputHeight = inputSpec.height

    private data class YoloOutputLayout(
        val boxesIndex: Int,
        val scoresIndex: Int?,
        val classesIndex: Int?,
        val batchSize: Int,
        val numBoxes: Int,
        val boxDim: Int
    )

    private val layout: YoloOutputLayout
    private var outputBoxes: Array<Array<FloatArray>>
    private var outputScores: Array<FloatArray>? = null
    private var outputClasses: Array<FloatArray>? = null

    private val outputs: HashMap<Int, Any> = hashMapOf()

    @Volatile
    private var frameIndex: Int = 0

    private data class RawDetection(
        val x1: Float,
        val y1: Float,
        val x2: Float,
        val y2: Float,
        val score: Float,
        val classId: Int
    )

    private var lastDetections: List<DetectionResult> = emptyList()
    private val smoothAlpha = 0.6f

    init {
        val infos = tflite.getAllOutputInfos()
        val boxInfo = modelInfo.detectionHead
            ?: infos.first { it.shape.size == 3 && it.shape[0] == 1 && it.shape[2] >= 4 }

        val numBoxes = boxInfo.shape[1]
        val boxDim = boxInfo.shape[2]
        val batchSize = boxInfo.shape[0]

        outputBoxes = Array(batchSize) { Array(numBoxes) { FloatArray(boxDim) } }
        outputs[boxInfo.index] = outputBoxes as Any

        val scoreInfo = infos.firstOrNull {
            it.index != boxInfo.index &&
                    it.shape.size == 2 &&
                    it.shape[0] == 1 &&
                    it.shape[1] == numBoxes
        }
        if (scoreInfo != null) {
            outputScores = Array(1) { FloatArray(numBoxes) }
            outputs[scoreInfo.index] = outputScores as Any
        }

        val classInfo = infos.firstOrNull {
            it.index != boxInfo.index &&
                    it.index != scoreInfo?.index &&
                    it.shape.size == 2 &&
                    it.shape[0] == 1 &&
                    it.shape[1] == numBoxes
        }
        if (classInfo != null) {
            outputClasses = Array(1) { FloatArray(numBoxes) }
            outputs[classInfo.index] = outputClasses as Any
        }

        layout = YoloOutputLayout(
            boxesIndex = boxInfo.index,
            scoresIndex = scoreInfo?.index,
            classesIndex = classInfo?.index,
            batchSize = batchSize,
            numBoxes = numBoxes,
            boxDim = boxDim
        )

        Log.d(
            "DetectorAnalyzer",
            "layout: boxes=${layout.boxesIndex}, scores=${layout.scoresIndex}, classes=${layout.classesIndex}, numBoxes=${layout.numBoxes}, boxDim=${layout.boxDim}"
        )
    }

    private val localScoreThreshold = 0.25f
    private val minBoxSizePx = 12f
    private val maxDetectionsPerFrame = 50

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

            // semua logic deteksi dipindah ke sini
            analyzeBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("DetectorAnalyzer", "err: ${e.message}", e)
        } finally {
            imageProxy.close()
        }
    }

    /**
     * Versi yang pakai Bitmap saja.
     * Ini yang nanti dipanggil dari MultiModelAnalyzer.
     */
    fun analyzeBitmap(bitmap: Bitmap) {
        try {
            val currentIndex = frameIndex++
            if (currentIndex % 2 != 0) {
                // skip sebagian frame (throttling) biar nggak terlalu berat
                return
            }

            val imgW = bitmap.width.toFloat()
            val imgH = bitmap.height.toFloat()
            val imgArea = imgW * imgH

            val inputBuffer = try {
                inputPre.prepareInput(bitmap)
            } catch (e: Exception) {
                Log.e("DetectorAnalyzer", "prepareInput error: ${e.message}", e)
                return
            }

            tflite.runForMultipleInputsOutputs(arrayOf(inputBuffer), outputs)

            val boxesArray = outputBoxes
            val scoresArray = outputScores
            val classesArray = outputClasses

            val numBoxes = layout.numBoxes
            val boxDim = layout.boxDim

            var maxCoord = 0f
            for (i in 0 until numBoxes) {
                val b = boxesArray[0][i]
                if (b.size < 4) continue
                maxCoord = maxCoord.coerceAtLeast(maxOf(b[0], b[1], b[2], b[3]))
            }
            val coordsAreNormalized = maxCoord <= 2f

            val rawList = mutableListOf<RawDetection>()

            for (i in 0 until numBoxes) {
                val box = boxesArray[0][i]
                if (box.size < 4) continue

                val score = if (scoresArray != null) {
                    scoresArray[0][i]
                } else {
                    if (boxDim > 4) box[4] else 1f
                }
                if (score < localScoreThreshold) continue

                val classId = if (classesArray != null) {
                    classesArray[0][i].toInt().coerceAtLeast(0)
                } else {
                    0
                }

                // ASUMSI: box = [x1,y1,x2,y2] di ruang input model
                val (x1Model, y1Model, x2Model, y2Model) = if (coordsAreNormalized) {
                    floatArrayOf(
                        box[0] * modelInputWidth,
                        box[1] * modelInputHeight,
                        box[2] * modelInputWidth,
                        box[3] * modelInputHeight
                    )
                } else {
                    floatArrayOf(box[0], box[1], box[2], box[3])
                }

                val mapped = inputPre.mapBoxToOriginal(
                    x1Model, y1Model, x2Model, y2Model,
                    imgW, imgH
                )

                var x1 = mapped.left
                var y1 = mapped.top
                var x2 = mapped.right
                var y2 = mapped.bottom

                if (x2 <= x1 || y2 <= y1) continue

                x1 = x1.coerceIn(0f, imgW)
                y1 = y1.coerceIn(0f, imgH)
                x2 = x2.coerceIn(0f, imgW)
                y2 = y2.coerceIn(0f, imgH)

                val wPx = x2 - x1
                val hPx = y2 - y1
                if (wPx < minBoxSizePx || hPx < minBoxSizePx) continue

                val area = wPx * hPx
                val relArea = area / imgArea
                if (relArea < 0.001f || relArea > 0.95f) continue

                rawList.add(
                    RawDetection(
                        x1 = x1,
                        y1 = y1,
                        x2 = x2,
                        y2 = y2,
                        score = score,
                        classId = classId
                    )
                )
            }

            if (rawList.isEmpty()) {
                overlayView.post {
                    overlayView.updateDetections(emptyList(), imgW, imgH)
                }
                return
            }

            val topRaw = rawList.sortedByDescending { it.score }
                .take(maxDetectionsPerFrame * 3)

            val detections = topRaw.map { r ->
                val label = if (r.classId in labels.indices) {
                    labels[r.classId]
                } else r.classId.toString()

                DetectionResult(
                    rect = RectF(r.x1, r.y1, r.x2, r.y2),
                    score = r.score,
                    label = label
                )
            }

            val afterNms = nonMaxSuppression(detections, 0.45f)
            val finalDetections = afterNms.sortedByDescending { it.score }
                .take(maxDetectionsPerFrame)

            val smoothed = smoothWithPrevious(finalDetections)

            overlayView.post {
                overlayView.updateDetections(smoothed, imgW, imgH)
            }
        } catch (e: Exception) {
            Log.e("DetectorAnalyzer", "analyzeBitmap err: ${e.message}", e)
        }
    }

    private fun smoothWithPrevious(current: List<DetectionResult>): List<DetectionResult> {
        if (lastDetections.isEmpty()) {
            lastDetections = current
            return current
        }

        val result = mutableListOf<DetectionResult>()
        val usedPrev = BooleanArray(lastDetections.size)

        for (d in current) {
            var bestIdx = -1
            var bestIou = 0f
            for ((idx, p) in lastDetections.withIndex()) {
                if (usedPrev[idx]) continue
                if (d.label != p.label) continue
                val iouVal = iou(d.rect, p.rect)
                if (iouVal > bestIou) {
                    bestIou = iouVal
                    bestIdx = idx
                }
            }
            if (bestIdx >= 0 && bestIou > 0.3f) {
                val p = lastDetections[bestIdx]
                usedPrev[bestIdx] = true
                val newRect = RectF(
                    lerp(p.rect.left,   d.rect.left,   smoothAlpha),
                    lerp(p.rect.top,    d.rect.top,    smoothAlpha),
                    lerp(p.rect.right,  d.rect.right,  smoothAlpha),
                    lerp(p.rect.bottom, d.rect.bottom, smoothAlpha)
                )
                result.add(d.copy(rect = newRect))
            } else {
                result.add(d)
            }
        }

        lastDetections = result
        return result
    }

    private fun lerp(a: Float, b: Float, alpha: Float): Float =
        a + (b - a) * alpha

    private fun nonMaxSuppression(
        boxes: List<DetectionResult>,
        iouThreshold: Float
    ): List<DetectionResult> {
        val out = mutableListOf<DetectionResult>()
        val list = boxes.sortedByDescending { it.score }.toMutableList()
        while (list.isNotEmpty()) {
            val best = list.removeAt(0)
            out.add(best)
            val iter = list.iterator()
            while (iter.hasNext()) {
                val other = iter.next()
                if (iou(best.rect, other.rect) > iouThreshold) {
                    iter.remove()
                }
            }
        }
        return out
    }

    private fun iou(a: RectF, b: RectF): Float {
        val interLeft = maxOf(a.left, b.left)
        val interTop = maxOf(a.top, b.top)
        val interRight = minOf(a.right, b.right)
        val interBottom = minOf(a.bottom, b.bottom)

        val interW = maxOf(0f, interRight - interLeft)
        val interH = maxOf(0f, interBottom - interTop)
        val interArea = interW * interH

        val areaA = (a.right - a.left) * (a.bottom - a.top)
        val areaB = (b.right - b.left) * (b.bottom - b.top)
        val union = areaA + areaB - interArea
        return if (union <= 0f) 0f else interArea / union
    }
}

