package presentation.util

interface BackgroundScheduler {
    fun enqueueVideoUpload(filePath: String, token: String)
}