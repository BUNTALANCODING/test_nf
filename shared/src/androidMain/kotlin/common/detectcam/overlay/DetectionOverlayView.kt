package common.detectcam.overlay

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import common.detectcam.analyzer.DetectionResult

class DetectionOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var detections: List<DetectionResult> = emptyList()
    private var imageWidth: Float = 0f
    private var imageHeight: Float = 0f

    private val boxPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        color = Color.RED
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.RED
        textSize = 36f
        isAntiAlias = true
    }

    fun updateDetections(detections: List<DetectionResult>, imageWidth: Float, imageHeight: Float) {
        this.detections = detections
        this.imageWidth = imageWidth
        this.imageHeight = imageHeight
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (imageWidth <= 0f || imageHeight <= 0f) return

        val scaleX = width / imageWidth
        val scaleY = height / imageHeight

        for (det in detections) {
            val r = det.rect
            val left = r.left * scaleX
            val top = r.top * scaleY
            val right = r.right * scaleX
            val bottom = r.bottom * scaleY

            canvas.drawRect(left, top, right, bottom, boxPaint)

            val text = "${det.label} ${(det.score * 100).toInt()}%"
            canvas.drawText(text, left, top - 8f, textPaint)
        }
    }
}
