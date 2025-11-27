//package detection
//
//interface ObjectDetector {
//    suspend fun detect(image: ByteArray): List<Detection>
//
//    companion object {
//        fun create(type: DetectorType): ObjectDetector =
//            createPlatformDetector(type)
//    }
//}
//
//// implemented in androidMain & iosMain
//expect fun createPlatformDetector(type: DetectorType): ObjectDetector
