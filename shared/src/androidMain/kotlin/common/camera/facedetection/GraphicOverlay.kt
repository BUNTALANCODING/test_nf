package common.camera.facedetection

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.camera.core.CameraSelector

class GraphicOverlay(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val lock = Any()
    private val graphics: MutableList<Graphic> = ArrayList()
    var cameraSelector: Int = CameraSelector.LENS_FACING_FRONT
    var position: Rect = Rect(0, 0, 0, 0)

    abstract class Graphic(private val overlay: GraphicOverlay) {
        abstract fun draw(canvas: Canvas?)
        fun getBoundingBoxPosition() = overlay.position

        fun calculateRect(imageHeight: Float, imageWidth: Float, boundingBox: Rect): RectF {
            val overlayWidth = overlay.width.toFloat()
            val overlayHeight = overlay.height.toFloat()

            val scaleX = overlayWidth / imageWidth
            val scaleY = overlayHeight / imageHeight

            val mappedBox = RectF(
                boundingBox.left * scaleX,
                boundingBox.top * scaleY,
                boundingBox.right * scaleX,
                boundingBox.bottom * scaleY
            )

            if (overlay.isFrontMode()) {
                val centerX = overlayWidth / 2
                val mirroredLeft = centerX + (centerX - mappedBox.right)
                val mirroredRight = centerX + (centerX - mappedBox.left)
                mappedBox.left = mirroredLeft
                mappedBox.right = mirroredRight
            }

            return mappedBox
        }
    }

    fun isFrontMode() = cameraSelector == CameraSelector.LENS_FACING_FRONT

    fun clear() {
        synchronized(lock) { graphics.clear() }
        postInvalidate()
    }

    fun add(graphic: Graphic) {
        synchronized(lock) { graphics.add(graphic) }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        synchronized(lock) {
            graphics.forEach { it.draw(canvas) }
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        val w = width
        val h = height

        val usable = (w * 0.8f).coerceAtMost(h * 0.8f)
        val radius = usable / 2
        val cx = w / 2
        val cy = (h * 0.37f).toInt()

        val left = (cx - radius).toInt()
        val top = (cy - radius).toInt()
        val right = (cx + radius).toInt()
        val bottom = (cy + radius).toInt()

        position = Rect(left, top, right, bottom)

        val stroke = Paint().apply {
            isAntiAlias = true
            strokeWidth = 6f
            color = Color.BLUE
            style = Paint.Style.STROKE
            pathEffect = DashPathEffect(floatArrayOf(20f, 15f), 0f)
        }

        canvas.drawCircle(cx.toFloat(), cy.toFloat(), radius, stroke)
    }
}