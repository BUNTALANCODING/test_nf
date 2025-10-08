package logger

object Logger {
    private var config = LoggerConfig()
    private var isInitialized = false

    fun initialize(loggerConfig: LoggerConfig) {
        config = loggerConfig
        PlatformLogger.init(config) // Direct object call
        isInitialized = true
    }

    // Verbose logging
    fun v(message: String, tag: String = config.globalTag, throwable: Throwable? = null) {
        log(LogLevel.VERBOSE, tag, message, throwable)
    }

    // Debug logging
    fun d(message: String, tag: String = config.globalTag, throwable: Throwable? = null) {
        log(LogLevel.DEBUG, tag, message, throwable)
    }

    // Info logging
    fun i(message: String, tag: String = config.globalTag, throwable: Throwable? = null) {
        log(LogLevel.INFO, tag, message, throwable)
    }

    // Warning logging
    fun w(message: String, tag: String = config.globalTag, throwable: Throwable? = null) {
        log(LogLevel.WARN, tag, message, throwable)
    }

    // Error logging
    fun e(message: String, tag: String = config.globalTag, throwable: Throwable? = null) {
        log(LogLevel.ERROR, tag, message, throwable)
    }

    // Assert/What a Terrible Failure logging
    fun wtf(message: String, tag: String = config.globalTag, throwable: Throwable? = null) {
        log(LogLevel.ASSERT, tag, message, throwable)
    }

    private fun log(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        if (!isInitialized) {
            // Fallback initialization with default config
            initialize(LoggerDefaults.debugConfig())
        }

        if (!shouldLog(level)) return

        val formattedMessage = formatMessage(level, message, throwable)
        PlatformLogger.log(level, tag, formattedMessage, throwable)
    }

    private fun shouldLog(level: LogLevel): Boolean {
        return level.priority >= config.minLogLevel.priority &&
                PlatformLogger.isLoggable(level)
    }

    private fun formatMessage(level: LogLevel, message: String, throwable: Throwable?): String {
        return when (config.logFormat) {
            LogFormat.MINIMAL -> message
            LogFormat.STANDARD -> buildStandardFormat(level, message)
            LogFormat.DETAILED -> buildDetailedFormat(level, message)
        }
    }

    private fun buildStandardFormat(level: LogLevel, message: String): String {
        return buildString {
            if (config.enableTimestamp) {
                append("[${getCurrentTimestamp()}] ")
            }
            if (config.enableThreadInfo) {
                append("[${getCurrentThreadInfo()}] ")
            }
            append("${level.symbol} $message")
        }
    }

    private fun buildDetailedFormat(level: LogLevel, message: String): String {
        return buildString {
            if (config.enableTimestamp) {
                append("[${getCurrentTimestamp()}] ")
            }
            if (config.enableThreadInfo) {
                append("[${getCurrentThreadInfo()}] ")
            }
            append("[${getCallerInfo()}] ")
            append("${level.symbol} $message")
        }
    }

    // Extension functions for easier usage
    fun <T: Any> T.logd(message: String = "", tag: String = config.globalTag): T {
        d("${this::class.simpleName}: $message", tag)
        return this
    }

    fun <T: Any> T.logi(message: String = "", tag: String = config.globalTag): T {
        i("${this::class.simpleName}: $message", tag)
        return this
    }

    fun <T: Any> T.loge(
        message: String = "",
        tag: String = config.globalTag,
        throwable: Throwable? = null
    ): T {
        e("${this::class.simpleName}: $message", tag, throwable)
        return this
    }

    // Performance logging
    internal inline fun <T> measureAndLog(
        operation: String,
        tag: String = config.globalTag,
        block: () -> T
    ): T {
        val startTime = getTimeMillis()
        val result = block()
        val duration = getTimeMillis() - startTime
        d("$operation completed in ${duration}ms", tag)
        return result
    }

    // Check if logger is initialized
    fun isInitialized(): Boolean = isInitialized

    // Get current configuration
    fun getConfig(): LoggerConfig = config
}