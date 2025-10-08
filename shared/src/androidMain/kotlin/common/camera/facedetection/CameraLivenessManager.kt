package common.camera.facedetection

import android.content.Context
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.face.Face
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraLivenessManager(
    private val context: Context,
    private val finderView: PreviewView,
    private val lifecycleOwner: LifecycleOwner,
    private val graphicOverlay: GraphicOverlay
) {

    var faceValidationListener: FaceValidationListener? = null
    private var preview: Preview? = null

    private var camera: Camera? = null
    private lateinit var cameraExecutor: ExecutorService
    private var cameraSelectorOption = CameraSelector.LENS_FACING_FRONT
    private var cameraProvider: ProcessCameraProvider? = null

    private var withAnalyzer: Boolean = false
    private var imageAnalyzer: ImageAnalysis? = null
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File

    init {
        createNewExecutor()
    }

    private fun createNewExecutor() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        outputDirectory = getOutputDirectory()
    }

    private fun getOutputDirectory(): File {
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, "Signal").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }

    fun startCameraForEktp() {
        withAnalyzer = false
        cameraSelectorOption = CameraSelector.LENS_FACING_BACK
        startCamera()
    }

    fun startCameraForLiveness() {
        withAnalyzer = true
        cameraSelectorOption = CameraSelector.LENS_FACING_FRONT
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(
            {
                cameraProvider = cameraProviderFuture.get()
                preview = Preview.Builder().build()

                if (withAnalyzer) {
                    // Ensure overlay has a usable bounding box
//                    graphicOverlay.initPositionIfNeeded()

                    imageAnalyzer = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(cameraExecutor, selectAnalyzer())
                        }
                }

                imageCapture = ImageCapture.Builder().build()

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(cameraSelectorOption)
                    .build()

                setCameraConfig(cameraProvider, cameraSelector)

            }, ContextCompat.getMainExecutor(context)
        )
    }

    fun stopCamera() {
        cameraExecutor.shutdown()
    }

    private fun selectAnalyzer(): ImageAnalysis.Analyzer {
        return FaceContourDetectionProcessor(graphicOverlay, faceValidationListener)
    }

    fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory, SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    this@CameraLivenessManager.faceValidationListener?.onImageSaved(photoFile)
                }
            })
    }

    private fun setCameraConfig(
        cameraProvider: ProcessCameraProvider?, cameraSelector: CameraSelector,
    ) {
        try {
            cameraProvider?.unbindAll()
            if (withAnalyzer) {
                camera = cameraProvider?.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageCapture, imageAnalyzer
                )
            } else {
                camera = cameraProvider?.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageCapture
                )
            }
            preview?.setSurfaceProvider(
                finderView.surfaceProvider
            )
        } catch (e: Exception) {
            Log.e(TAG, "Use case binding failed", e)
//            FirebaseCrashlytics.getInstance().sendCustomLog {
//                key("log", "Error while setting camera config")
//                key("screen", TAG)
//                key("error", "${e.printStackTrace()}")
//            }
        }
    }

    fun changeCameraSelector() {
        cameraProvider?.unbindAll()
        cameraSelectorOption =
            if (cameraSelectorOption == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
            else CameraSelector.LENS_FACING_BACK
//        graphicOverlay.toggleSelector()
        startCamera()
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

    interface FaceValidationListener {
        fun onFaceInBoundingBox(face: Face, inBox: Boolean)
        fun onImageSaved(file: File)
        fun onFaceNotFound()
    }

}