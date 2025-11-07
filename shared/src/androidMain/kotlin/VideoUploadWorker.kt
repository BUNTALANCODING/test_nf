import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import business.datasource.network.main.MainService
import business.datasource.network.main.MainServiceImpl
import business.datasource.network.main.request.UploadPetugasRequestDTO
import business.datasource.network.main.request.UploadVideoRequestDTO
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class VideoUploadWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val mainService: MainService by inject()

    override suspend fun doWork(): Result {
        val filePath = inputData.getString("file_path") ?: return Result.failure()
        val token = inputData.getString("token") ?: return Result.failure()

        val onProgressUpdate: (Float) -> Unit = { progress ->
            runBlocking { setProgress(workDataOf("Progress" to (progress * 100).toInt())) }
        }

        val params = UploadVideoRequestDTO(filePath, onProgressUpdate)

        val success = mainService.uploadVideo(token, params)

        return if (success) Result.success() else Result.retry()
    }
}