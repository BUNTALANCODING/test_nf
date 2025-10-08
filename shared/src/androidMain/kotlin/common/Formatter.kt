package common

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

actual object DateTime {
    @JvmStatic
    actual fun getFormattedDate(
        timestamp: String,
    ): String {
        val timestampFormat = "yyyy-MM-dd HH:mm:ss"
        val outputFormat = "dd MMM yyyy, HH:mm"

        val dateFormatter = SimpleDateFormat(outputFormat, Locale.getDefault())
        //dateFormatter.timeZone = TimeZone.getTimeZone("GMT")
        dateFormatter.timeZone = TimeZone.getDefault()

        val parser = SimpleDateFormat(timestampFormat, Locale.getDefault())
        //parser.timeZone = TimeZone.getTimeZone("GMT")
        parser.timeZone = TimeZone.getDefault()

        try {
            val date = parser.parse(timestamp)
            if (date != null) {
                dateFormatter.timeZone = TimeZone.getDefault()
                return dateFormatter.format(date)
            }
        } catch (e: Exception) {
            // Handle parsing error
            e.printStackTrace()
        }

        // If parsing fails, return the original timestamp
        return timestamp
    }

    @JvmStatic
    actual fun getFormattedDateOnly(
        timestamp: String,
    ): String {
        val timestampFormat = "yyyy-MM-dd"
        val outputFormat = "dd MMM yyyy"

        val dateFormatter = SimpleDateFormat(outputFormat, Locale.getDefault())
        //dateFormatter.timeZone = TimeZone.getTimeZone("GMT")
        dateFormatter.timeZone = TimeZone.getDefault()

        val parser = SimpleDateFormat(timestampFormat, Locale.getDefault())
        //parser.timeZone = TimeZone.getTimeZone("GMT")
        parser.timeZone = TimeZone.getDefault()

        try {
            val date = parser.parse(timestamp)
            if (date != null) {
                dateFormatter.timeZone = TimeZone.getDefault()
                return dateFormatter.format(date)
            }
        } catch (e: Exception) {
            // Handle parsing error
            e.printStackTrace()
        }

        // If parsing fails, return the original timestamp
        return timestamp
    }

    @JvmStatic
    actual fun getFormattedTime(
        timestamp: String,
    ): String {
        val timestampFormat = "HH:mm:ss"
        val outputFormat = "HH:mm"

        val dateFormatter = SimpleDateFormat(outputFormat, Locale.getDefault())
        //dateFormatter.timeZone = TimeZone.getTimeZone("GMT")
        dateFormatter.timeZone = TimeZone.getDefault()

        val parser = SimpleDateFormat(timestampFormat, Locale.getDefault())
        //parser.timeZone = TimeZone.getTimeZone("GMT")
        parser.timeZone = TimeZone.getDefault()

        try {
            val date = parser.parse(timestamp)
            if (date != null) {
                dateFormatter.timeZone = TimeZone.getDefault()
                return dateFormatter.format(date)
            }
        } catch (e: Exception) {
            // Handle parsing error
            e.printStackTrace()
        }

        // If parsing fails, return the original timestamp
        return timestamp
    }

    actual fun formatTimeStamp(timeStamp: Long, outputFormat: String): String {
        val sdf = SimpleDateFormat(outputFormat, Locale.getDefault())
        return sdf.format(Date(timeStamp * 1000))
    }

    actual fun getDateInMilliSeconds(timeStamp: String, inputFormat: String): Long {
        if (timeStamp.trim().isEmpty()) return getCurrentTimeInMilliSeconds()

        val parser = SimpleDateFormat(inputFormat, Locale.getDefault())
        parser.timeZone = TimeZone.getDefault()
        return parser.parse(timeStamp)?.time ?: 0
    }

    actual fun getCurrentTimeInMilliSeconds(): Long {
        return System.currentTimeMillis()
    }

    actual fun getForwardedDate(
        forwardedDaya: Int,
        forwardedMonth: Int,
        outputFormat: String
    ): String {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, forwardedDaya)
            add(Calendar.MONTH, forwardedMonth)
        }

        val dateFormat = SimpleDateFormat(outputFormat, Locale.ENGLISH)
        return dateFormat.format(Date(calendar.timeInMillis))
    }
}

actual fun Format (value: Int): String{
    val pattern = ",00"
    val formatter = DecimalFormat("#,###")
    val res = formatter.format(value).replace(",", ".")
    return res+pattern
}

