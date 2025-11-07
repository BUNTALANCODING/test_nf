import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import presentation.util.BackgroundScheduler
import java.util.concurrent.TimeUnit

class AndroidBackgroundScheduler(private val context: Context) : BackgroundScheduler {

    override fun enqueueVideoUpload(filePath: String, token: String) {
        val uploadData = workDataOf(
            "file_path" to filePath,
            "token" to token
        )
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadRequest = OneTimeWorkRequestBuilder<VideoUploadWorker>()
            .setInputData(uploadData)
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
            .addTag("video_upload_${System.currentTimeMillis()}")
            .build()

        // Logika WorkManager di sini
        WorkManager.getInstance(context).enqueue(uploadRequest)
    }
}