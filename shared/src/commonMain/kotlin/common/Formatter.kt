package common

expect object DateTime {
    fun getFormattedDate(
        timestamp: String,
    ): String

    fun getFormattedDateOnly(
        timestamp: String,
    ): String

    fun getFormattedTime(
        timestamp: String,
    ): String

    fun formatTimeStamp(
        timeStamp: Long,
        outputFormat: String = "yyyy-MM-dd HH:mm:ss"
    ): String

    fun getDateInMilliSeconds(timeStamp: String, inputFormat: String): Long

    fun getCurrentTimeInMilliSeconds(): Long

    fun getForwardedDate(
        forwardedDaya: Int = 0,
        forwardedMonth: Int = 0,
        outputFormat: String = "yyyy-MM-ddTHH:mm:ss"
    ): String
}

expect fun Format(value: Int): String
