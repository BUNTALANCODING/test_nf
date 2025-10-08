package common.camera.facedetection

import android.graphics.Rect
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.IOException

class FaceContourDetectionProcessor(
    private val view: GraphicOverlay, private val listener: CameraLivenessManager.FaceValidationListener?
) : BaseImageAnalyzer<List<Face>>() {

    private val realTimeOpts =
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            // Real-time contour detection
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .build()

    private val detector = FaceDetection.getClient(realTimeOpts)

    override val graphicOverlay: GraphicOverlay
        get() = view

    override fun detectInImage(image: InputImage): Task<List<Face>> {
        return detector.process(image)
    }

    override fun stop() {
        try {
            detector.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception thrown while trying to close Face Detector: $e")
//            FirebaseCrashlytics.getInstance().sendCustomLog {
//                key("log", "Error while setting camera config")
//                key("screen", "${FaceContourDetectionProcessor::class.simpleName}")
//                key("error", "${e.printStackTrace()}")
//            }
        }
    }

    override fun onSuccess(
        results: List<Face>, graphicOverlay: GraphicOverlay, rect: Rect
    ) {
        graphicOverlay.clear()
        if(results.isEmpty()){
            listener?.onFaceNotFound()
        }else {
            results.forEach {
                val faceGraphic = FaceContourGraphic(graphicOverlay, it, rect)
                graphicOverlay.add(faceGraphic)
                listener?.onFaceInBoundingBox(it, faceGraphic.isFaceInBoundingBox())
            }
        }
        graphicOverlay.postInvalidate()

    }


    override fun onFailure(e: Exception) {
        Log.w(TAG, "Face Detector failed.$e")
    }

    companion object {
        private const val TAG = "FaceDetectorProcessor"
    }

}