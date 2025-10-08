package logger

// Using 'expect object' instead of 'expect class' to avoid constructor issues
expect object PlatformLogger {
    fun init(config: LoggerConfig)
    fun log(level: LogLevel, tag: String, message: String, throwable: Throwable?)
    fun isLoggable(level: LogLevel): Boolean
}

// Helper functions as expect functions
expect fun getCurrentTimestamp(): String
expect fun getCurrentThreadInfo(): String
expect fun getCallerInfo(): String
expect fun getTimeMillis(): Long