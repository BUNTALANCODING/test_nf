package presentation.ui.main.uploadChunk

import kotlinx.coroutines.delay
import kotlin.concurrent.Volatile

class UploadController {

    @Volatile
    private var paused: Boolean = false

    @Volatile
    private var cancelled: Boolean = false

    fun pause() {
        paused = true
    }

    fun resume() {
        paused = false
    }

    fun cancel() {
        cancelled = true
    }

    fun isCancelled(): Boolean = cancelled

    suspend fun waitIfPaused() {
        while (paused && !cancelled) {
            delay(200)
        }
    }
}
