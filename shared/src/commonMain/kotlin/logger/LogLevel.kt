package logger

enum class LogLevel(val priority: Int, val symbol: String) {
    VERBOSE(0, "🔍"),
    DEBUG(1, "🐛"),
    INFO(2, "ℹ️"),
    WARN(3, "⚠️"),
    ERROR(4, "❌"),
    ASSERT(5, "💥");

    companion object {
        fun fromString(level: String): LogLevel {
            return values().find { it.name.equals(level, ignoreCase = true) } ?: INFO
        }

        fun getMinimumReleaseLevel(): LogLevel = WARN
        fun getMinimumDebugLevel(): LogLevel = VERBOSE
    }
}
