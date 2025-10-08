package logger

import platform.Foundation.*
import platform.darwin.*
import kotlinx.cinterop.*

actual object PlatformLogger {
    private var config: LoggerConfig? = null

    actual fun init(config: LoggerConfig) {
        this.config = config
        if (config.enableFileLogging) {
            initFileLogging()
        }
        NSLog("PlatformLogger initialized - Debug: ${config.isDebugBuild}")
    }

    actual fun log(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        val logMessage = "[$tag] $message"

        when (level) {
            LogLevel.VERBOSE, LogLevel.DEBUG -> {
                if (isDebugBuild()) {
                    NSLog("🔍 DEBUG: $logMessage")
                    println("🔍 $logMessage")
                }
            }
            LogLevel.INFO -> {
                NSLog("ℹ️ INFO: $logMessage")
                println("ℹ️ $logMessage")
            }
            LogLevel.WARN -> {
                NSLog("⚠️ WARN: $logMessage")
                println("⚠️ $logMessage")
            }
            LogLevel.ERROR, LogLevel.ASSERT -> {
                NSLog("❌ ERROR: $logMessage")
                println("❌ $logMessage")

                // In release, send to crash reporting
                if (!isDebugBuild() && config?.enableCrashlytics == true) {
                    sendToCrashReporting(level, tag, message, throwable)
                }
            }
        }

        throwable?.let {
            val exceptionInfo = "Exception: ${it.message}\nStack: ${it.stackTraceToString()}"
            NSLog("Exception Details: $exceptionInfo")
            println("Exception Details: $exceptionInfo")
        }

        // Write to file if enabled
        if (config?.enableFileLogging == true) {
            writeToFile(level, tag, message, throwable)
        }
    }

    actual fun isLoggable(level: LogLevel): Boolean {
        val currentConfig = config ?: return false
        return when {
            isDebugBuild() -> true
            else -> level.priority >= LogLevel.WARN.priority
        }
    }

    private fun isDebugBuild(): Boolean {
        return Platform.isDebugBinary
    }

    private fun initFileLogging() {
        try {
            val documentsPath = NSSearchPathForDirectoriesInDomains(
                NSDocumentDirectory,
                NSUserDomainMask,
                true
            ).firstOrNull() as? String ?: return

            val logDirPath = "$documentsPath/logs"
            val fileManager = NSFileManager.defaultManager

            if (!fileManager.fileExistsAtPath(logDirPath)) {
                fileManager.createDirectoryAtPath(
                    logDirPath,
                    withIntermediateDirectories = true,
                    attributes = null,
                    error = null
                )
            }
            NSLog("iOS file logging initialized at: $logDirPath")
        } catch (e: Exception) {
            NSLog("Failed to initialize file logging: ${e.message}")
        }
    }

    private fun writeToFile(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        try {
            val documentsPath = NSSearchPathForDirectoriesInDomains(
                NSDocumentDirectory,
                NSUserDomainMask,
                true
            ).firstOrNull() as? String ?: return

            val logFilePath = "$documentsPath/logs/app_log_${getCurrentDateString()}.txt"
            val logEntry = "${getCurrentTimestamp()} ${level.name} [$tag] $message\n"

            val fileManager = NSFileManager.defaultManager
            if (fileManager.fileExistsAtPath(logFilePath)) {
                val existingData = NSData.dataWithContentsOfFile(logFilePath)
                val existingString = existingData?.let {
                    NSString.create(it, NSUTF8StringEncoding)
                } ?: ""
                val newContent = "$existingString$logEntry"
                newContent.writeToFile(logFilePath, atomically = true, encoding = NSUTF8StringEncoding, error = null)
            } else {
                logEntry.writeToFile(logFilePath, atomically = true, encoding = NSUTF8StringEncoding, error = null)
            }
        } catch (e: Exception) {
            NSLog("Failed to write to log file: ${e.message}")
        }
    }

    private fun sendToCrashReporting(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        // Implement Crashlytics or other crash reporting service
        // Example: FirebaseCrashlytics.crashlytics().log("\(tag): \(message)")
        NSLog("Crash Report: [$tag] $message")
    }

    private fun getCurrentDateString(): String {
        val formatter = NSDateFormatter().apply {
            dateFormat = "yyyy-MM-dd"
        }
        return formatter.stringFromDate(NSDate())
    }
}

actual fun getCurrentTimestamp(): String {
    val formatter = NSDateFormatter().apply {
        dateFormat = "yyyy-MM-dd HH:mm:ss.SSS"
    }
    return formatter.stringFromDate(NSDate())
}

actual fun getCurrentThreadInfo(): String {
    return if (NSThread.isMainThread) "Main" else "Background-${NSThread.currentThread.hash}"
}

actual fun getCallerInfo(): String {
    // iOS doesn't have easy stack trace access like JVM
    // You could implement custom stack tracking if needed
    return "iOS-Caller"
}

actual fun getTimeMillis(): Long {
    return (NSDate().timeIntervalSince1970 * 1000).toLong()
}