package common.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.mlkit.vision.face.Face

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.IOException

enum class FaceState { HOLD, TAKE_PICTURE, INBOX, BLINK, SMILE }

data class LivenessUiState(
    val currentStep: FaceState = FaceState.INBOX,
    val faceInBox: Boolean = false,
    val smileProbability: Float = 0f,
    val leftEyeProb: Float = 1f,
    val rightEyeProb: Float = 1f,
    val showCheck: Boolean = false,
    val errorMessage: String? = null
)

class LivenessViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LivenessUiState())
    val uiState: StateFlow<LivenessUiState> = _uiState
    private val _photoSavedEvent = MutableSharedFlow<ImageBitmap>()
    val photoSavedEvent: SharedFlow<ImageBitmap> = _photoSavedEvent

    // This is your ordered random state
    private val steps: List<FaceState> = buildList {
        add(FaceState.INBOX)
        add(FaceState.HOLD)
        add(FaceState.BLINK)
        add(FaceState.SMILE)
        add(FaceState.TAKE_PICTURE)
    }

    private var index = 0

    // Event: when you must take picture
    private val _takePictureEvent = MutableSharedFlow<Unit>()
    val takePictureEvent: SharedFlow<Unit> = _takePictureEvent

    // Resets everything when needed
    fun reset() {
        index = 0
        _uiState.value = LivenessUiState(currentStep = steps[index])
    }

    init {
        reset()
    }

    fun faceNotFound() {
        _uiState.update {
            it.copy(
                errorMessage = "Wajah tidak ditemukan",
                showCheck = false
            )
        }
    }

    fun processImage(face: Face?, inBox: Boolean) {
        _uiState.update { it.copy(errorMessage = null) }

        if (face == null) return

        if (index >= steps.size) return

        val currentStep = steps[index]
        when (currentStep) {
            FaceState.INBOX -> {
                if (inBox) {
                    successStep()
                }
            }

            FaceState.TAKE_PICTURE -> {
                // Immediately trigger picture event
                viewModelScope.launch {
                    _takePictureEvent.emit(Unit)
                }
                successStep()
            }

            FaceState.HOLD -> {
                successStep()
            }

            FaceState.BLINK -> {
                val leftEye = face.leftEyeOpenProbability ?: 1f
                val rightEye = face.rightEyeOpenProbability ?: 1f
                if (leftEye < 0.2f || rightEye < 0.2f) {
                    successStep()
                } else {
                    _uiState.update {
                        it.copy(
                            leftEyeProb = leftEye,
                            rightEyeProb = rightEye
                        )
                    }
                }
            }

            FaceState.SMILE -> {
                val smile = face.smilingProbability ?: 0f
                if (smile > 0.7f) {
                    successStep()
                } else {
                    _uiState.update {
                        it.copy(smileProbability = smile)
                    }
                }
            }
        }
    }

    fun handleImageSaved(context: Context, file: File) {
        viewModelScope.launch(Dispatchers.IO) {
//            val compressed = Compressor.compress(context, file)

//            val bitmap = getBitmapFromFile(compressed)

            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val imageBitmap = bitmap?.asImageBitmap() ?: ImageBitmap(1, 1) // Fallback empty ImageBitmap

//            val base64 = compressed.toBase64()
            _photoSavedEvent.emit(imageBitmap)
        }
    }


    private fun successStep() {
        index += 1

        val nextStep = if (index < steps.size) steps[index] else FaceState.HOLD
        println("✅ Moving to next step: $nextStep")
        _uiState.update {
            it.copy(
                currentStep = nextStep,
                showCheck = true
            )
        }
        // Optionally reset check after delay
        viewModelScope.launch {
            delay(800)
            _uiState.update { s -> s.copy(showCheck = false) }
        }
    }
}

fun File?.toBase64(): String {
    var base64String = ""

    try {
        val bytes = this?.readBytes()
        base64String = Base64.encodeToString(bytes, Base64.NO_WRAP)
    } catch (error: Exception) {
//        FirebaseCrashlytics.getInstance().sendCustomLog {
//            key("log", "Error encode file to Base64")
//            key("error", "${error.printStackTrace()}")
//        }
        error.printStackTrace()
    }

    return base64String
}

@Throws(IOException::class)
private fun getBitmapFromFile(file: File): Bitmap {
    // Create a FileInputStream to read the file
    val inputStream = FileInputStream(file)

    // Decode the file into a Bitmap
    return BitmapFactory.decodeStream(inputStream).apply {
        inputStream.close() // Always close the input stream after use
    }
}

