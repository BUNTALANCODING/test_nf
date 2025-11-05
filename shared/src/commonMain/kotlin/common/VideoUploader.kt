package common

expect class VideoUploader(context: Context) {
    suspend fun uploadVideo(filePath: String, uploadToken: String): String
}