package common.camera.facedetection

import android.graphics.*
import android.util.Log
import com.google.mlkit.vision.face.Face

/**
 * this code belongs to https://github.com/happysingh23828/CameraX-FaceDetection-MlKit
 */
class FaceContourGraphic(
    overlay: GraphicOverlay,
    private val face: Face,
    private val imageRect: Rect
) : GraphicOverlay.Graphic(overlay) {

    private val facePositionPaint: Paint
    private val idPaint: Paint
    private val boxPaint: Paint

    private var mappedBox : Rect? = null

    init {
        val selectedColor = Color.WHITE

        facePositionPaint = Paint()
        facePositionPaint.color = selectedColor

        idPaint = Paint()
        idPaint.color = selectedColor

        boxPaint = Paint()
        boxPaint.color = selectedColor
        boxPaint.style = Paint.Style.STROKE
        boxPaint.strokeWidth = BOX_STROKE_WIDTH
    }

    override fun draw(canvas: Canvas?) {
//        val rect = calculateRect(
//            imageRect.height().toFloat(),
//            imageRect.width().toFloat(),
//            face.boundingBox
//        )
//        canvas?.drawRect(rect, boxPaint)
    }

    fun isFaceInBoundingBox(): Boolean {
        val bounding = getBoundingBoxPosition()
        if (bounding.width() == 0 || bounding.height() == 0) {
            Log.d("INBOX", "Bounding box not ready.")
            return false
        }

        val rect = calculateRect(
            imageRect.height().toFloat(),
            imageRect.width().toFloat(),
            face.boundingBox
        )

        Log.d("INBOX", "Bounding: $bounding, Rect: $rect")

        return RectF.intersects(rect, RectF(bounding))
    }

    companion object {
        private const val BOX_STROKE_WIDTH = 5.0f
    }

}