package presentation.ui.main.uploadChunk

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class UploadController {
    private val pauseMutex = Mutex(locked = false)
    private var _cancelled = false

    fun cancel() {
        _cancelled = true
        if (pauseMutex.isLocked) pauseMutex.unlock() // unlock if paused
    }

    fun isCancelled() = _cancelled

    suspend fun waitIfPaused() {
        pauseMutex.withLock { /* unlocked → continue */ }
    }

    fun pause() {
        if (!pauseMutex.isLocked) pauseMutex.tryLock()  // lock to pause
    }

    fun resume() {
        if (pauseMutex.isLocked) pauseMutex.unlock()
    }
}