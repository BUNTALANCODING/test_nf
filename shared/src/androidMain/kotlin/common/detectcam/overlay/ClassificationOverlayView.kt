package common.detectcam.overlay

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ClassificationOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var label: String = ""
    private var score: Float = 0f

    private val textPaint = Paint().apply {
        color = Color.GREEN
        textSize = 48f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    fun updateStatus(label: String, score: Float) {
        this.label = label
        this.score = score
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (label.isEmpty()) return

        val text = "$label ${(score * 100).toInt()}%"
        val x = width / 2f
        val y = height * 0.1f + 48f
        canvas.drawText(text, x, y, textPaint)
    }
}
