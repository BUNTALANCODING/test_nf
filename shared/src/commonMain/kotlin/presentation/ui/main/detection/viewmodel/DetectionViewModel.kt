//package viewmodel
//
//import androidx.lifecycle.ViewModel
//import detection.*
//import kotlinx.coroutines.flow.MutableStateFlow
//
//class DetectionViewModel : ViewModel() {
//
//    private var detector: ObjectDetector? = null
//
//    private val _detections = MutableStateFlow<List<Detection>>(emptyList())
//    val detections = _detections
//
//    fun setDetector(type: DetectorType) {
//        detector = ObjectDetector.create(type)
//    }
//
//    suspend fun detect(byteArray: ByteArray) {
//        detector?.let {
//            val result = it.detect(byteArray)
//            _detections.value = result
//        }
//    }
//}
