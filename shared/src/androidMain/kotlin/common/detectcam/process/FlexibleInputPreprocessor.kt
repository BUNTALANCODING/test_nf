package common.detectcam.process

import android.graphics.*
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import java.nio.ByteBuffer

class FlexibleInputPreprocessor(
    interpreter: Interpreter
) {

    data class InputSpec(
        val width: Int,
        val height: Int,
        val channels: Int,
        val dataType: DataType
    )

    data class PreprocessTransform(
        val scale: Float,
        val offsetX: Float,
        val offsetY: Float,
        val srcW: Int,
        val srcH: Int
    )

    val spec: InputSpec

    var lastTransform: PreprocessTransform? = null
        private set

    private val tensorImage = TensorImage(DataType.FLOAT32)
    private val imageProcessor = ImageProcessor.Builder()
        .add(NormalizeOp(0f, 255f)) // 0..255 -> 0..1
        .build()

    init {
        val inputTensor = interpreter.getInputTensor(0)
        val shape = inputTensor.shape() // [1, H, W, C]
        val dataType = inputTensor.dataType()

        if (shape.size != 4) {
            throw IllegalArgumentException("Input tensor shape tidak didukung: ${shape.contentToString()}")
        }

        val height = shape[1]
        val width = shape[2]
        val channels = shape[3]

        spec = InputSpec(
            width = width,
            height = height,
            channels = channels,
            dataType = dataType
        )

        Log.d("FlexibleInputPre", "inputSpec: w=$width, h=$height, c=$channels, type=$dataType")
    }

    fun prepareInput(src: Bitmap): ByteBuffer {
        val srcW = src.width
        val srcH = src.height

        val dstW = spec.width
        val dstH = spec.height

        val scale = minOf(
            dstW / srcW.toFloat(),
            dstH / srcH.toFloat()
        )

        val newW = (srcW * scale).toInt()
        val newH = (srcH * scale).toInt()

        val offsetX = (dstW - newW) / 2f
        val offsetY = (dstH - newH) / 2f

        val dstBitmap = Bitmap.createBitmap(dstW, dstH, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(dstBitmap)
        canvas.drawColor(Color.BLACK)

        val srcRect = Rect(0, 0, srcW, srcH)
        val dstRect = RectF(offsetX, offsetY, offsetX + newW, offsetY + newH)
        canvas.drawBitmap(src, srcRect, dstRect, null)

        lastTransform = PreprocessTransform(
            scale = scale,
            offsetX = offsetX,
            offsetY = offsetY,
            srcW = srcW,
            srcH = srcH
        )

        tensorImage.load(dstBitmap)
        val processed = imageProcessor.process(tensorImage)
        return processed.buffer
    }

    fun mapBoxToOriginal(
        x1Model: Float,
        y1Model: Float,
        x2Model: Float,
        y2Model: Float,
        origW: Float,
        origH: Float
    ): RectF {
        val tf = lastTransform ?: return RectF(0f, 0f, 0f, 0f)

        val x1 = ((x1Model - tf.offsetX) / tf.scale).coerceIn(0f, origW)
        val y1 = ((y1Model - tf.offsetY) / tf.scale).coerceIn(0f, origH)
        val x2 = ((x2Model - tf.offsetX) / tf.scale).coerceIn(0f, origW)
        val y2 = ((y2Model - tf.offsetY) / tf.scale).coerceIn(0f, origH)

        return RectF(x1, y1, x2, y2)
    }
}
