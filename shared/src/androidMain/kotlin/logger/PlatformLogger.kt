package logger

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

// Use 'actual object' instead of 'actual class' - no constructor issues
actual object PlatformLogger {
    private var config: LoggerConfig? = null
    private var logFile: File? = null

    actual fun init(config: LoggerConfig) {
        this.config = config
        if (config.enableFileLogging) {
            initFileLogging()
        }
        Log.i("PlatformLogger", "Android logger initialized - Debug: ${config.isDebugBuild}")
    }

    actual fun log(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        val maxLength = config?.maxLogLength ?: 4000
        val chunks = message.chunked(maxLength)

        chunks.forEach { chunk ->
            when (level) {
                LogLevel.VERBOSE -> Log.v(tag, chunk, throwable)
                LogLevel.DEBUG -> Log.d(tag, chunk, throwable)
                LogLevel.INFO -> Log.i(tag, chunk, throwable)
                LogLevel.WARN -> Log.w(tag, chunk, throwable)
                LogLevel.ERROR -> Log.e(tag, chunk, throwable)
                LogLevel.ASSERT -> Log.wtf(tag, chunk, throwable)
            }

            // Write to file if enabled
            if (config?.enableFileLogging == true) {
                writeToFile(level, tag, chunk, throwable)
            }
        }

        // Send to crashlytics in release builds for errors
        if (config?.enableCrashlytics == true && level.priority >= LogLevel.ERROR.priority) {
            sendToCrashlytics(level, tag, message, throwable)
        }
    }

    actual fun isLoggable(level: LogLevel): Boolean {
        val currentConfig = config ?: return false
        return when {
            currentConfig.isDebugBuild -> true
            else -> level.priority >= LogLevel.WARN.priority
        }
    }

    private fun initFileLogging() {
        try {
            // You'll need to provide application context
            // This is a simplified version - implement getApplicationContext()
            val context = getApplicationContext()
            val logDir = File(context.filesDir, "logs")
            if (!logDir.exists()) {
                logDir.mkdirs()
            }
            logFile = File(logDir, "app_log_${getCurrentDateString()}.txt")
            Log.d("PlatformLogger", "File logging initialized: ${logFile?.absolutePath}")
        } catch (e: Exception) {
            Log.e("PlatformLogger", "Failed to initialize file logging", e)
        }
    }

    private fun writeToFile(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        try {
            logFile?.let { file ->
                val writer = FileWriter(file, true)
                writer.append("${getCurrentTimestamp()} ${level.name} [$tag] $message\n")
                throwable?.let {
                    writer.append("Exception: ${it.message}\n")
                    writer.append("${it.stackTraceToString()}\n")
                }
                writer.close()
            }
        } catch (e: Exception) {
            Log.e("PlatformLogger", "Failed to write to log file", e)
        }
    }

    private fun sendToCrashlytics(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        // Implement Firebase Crashlytics integration here
        // Example:
        FirebaseCrashlytics.getInstance().log("$tag: $message")
        throwable?.let { FirebaseCrashlytics.getInstance().recordException(it) }
        Log.i("Crashlytics", "Would send to crashlytics: [$tag] $message")
    }

    private fun getCurrentDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
}

// Application context holder - implement this in your Android module
private lateinit var applicationContext: android.content.Context

fun initializeApplicationContext(context: android.content.Context) {
    applicationContext = context.applicationContext
}

private fun getApplicationContext(): android.content.Context {
    return if (::applicationContext.isInitialized) {
        applicationContext
    } else {
        throw IllegalStateException("Application context not initialized. Call initializeApplicationContext() first.")
    }
}

actual fun getCurrentTimestamp(): String {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
        .format(Date())
}

actual fun getCurrentThreadInfo(): String {
    return Thread.currentThread().name
}

actual fun getCallerInfo(): String {
    return try {
        val stackTrace = Thread.currentThread().stackTrace
        // Find the first non-Logger class in the stack trace
        val caller = stackTrace.firstOrNull { element ->
            !element.className.contains("Logger") &&
                    !element.className.contains("PlatformLogger") &&
                    !element.methodName.contains("log")
        }
        "${caller?.className?.substringAfterLast('.')}::${caller?.methodName}"
    } catch (e: Exception) {
        "Unknown"
    }
}

actual fun getTimeMillis(): Long {
    return System.currentTimeMillis()
}