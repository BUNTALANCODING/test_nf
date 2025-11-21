package common

actual class VideoUploader actual constructor(context: Context) {


    actual suspend fun uploadVideo(
        filePath: String,
        uploadToken: String
    ): String {
        return ""
    }
}