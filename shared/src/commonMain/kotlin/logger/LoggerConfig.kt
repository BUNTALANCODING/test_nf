package logger

data class LoggerConfig(
    val isDebugBuild: Boolean = false,
    val minLogLevel: LogLevel = if (isDebugBuild) LogLevel.VERBOSE else LogLevel.WARN,
    val enableFileLogging: Boolean = false,
    val enableCrashlytics: Boolean = !isDebugBuild,
    val enableTimestamp: Boolean = true,
    val enableThreadInfo: Boolean = true,
    val maxLogLength: Int = 4000,
    val globalTag: String = "KMPLogger",
    val logFormat: LogFormat = LogFormat.STANDARD
)

enum class LogFormat {
    MINIMAL,    // Just message
    STANDARD,   // [Timestamp][Thread] Message
    DETAILED    // [Timestamp][Thread][Class][Method] Message
}

object LoggerDefaults {
    fun debugConfig() = LoggerConfig(
        isDebugBuild = true,
        minLogLevel = LogLevel.VERBOSE,
        enableFileLogging = true,
        enableCrashlytics = true,
        logFormat = LogFormat.DETAILED
    )

    fun releaseConfig() = LoggerConfig(
        isDebugBuild = false,
        minLogLevel = LogLevel.WARN,
        enableFileLogging = false,
        enableCrashlytics = true,
        logFormat = LogFormat.MINIMAL
    )
}
