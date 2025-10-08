package common

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSTimeZone
import platform.Foundation.localTimeZone
import platform.Foundation.timeZoneWithAbbreviation
import platform.Foundation.*

actual object DateTime {
    actual fun getFormattedDate(timestamp: String): String {
        val df = NSDateFormatter()
        val timestampFormat = "yyyy-MM-dd HH:mm:ss"
        val outputFormat = "dd MMM yyyy, HH:mm"

        df.dateFormat = timestampFormat

        // Set the time zone to GMT (or UTC)
        NSTimeZone.timeZoneWithAbbreviation("GMT")?.let { df.timeZone = it }

        // Parse the GMT timestamp into an NSDate
        val date = df.dateFromString(timestamp)

        // Create a date formatter for the local time zone
        df.timeZone = NSTimeZone.localTimeZone
        df.dateFormat = outputFormat

        // Format the NSDate into a string in the local time zone
        return df.stringFromDate(date!!)
    }

    actual fun getFormattedDateOnly(timestamp: String): String {
        val df = NSDateFormatter()
        val timestampFormat = "yyyy-MM-dd"
        val outputFormat = "dd MMM yyyy"

        df.dateFormat = timestampFormat

        // Set the time zone to GMT (or UTC)
        NSTimeZone.timeZoneWithAbbreviation("GMT")?.let { df.timeZone = it }

        // Parse the GMT timestamp into an NSDate
        val date = df.dateFromString(timestamp)

        // Create a date formatter for the local time zone
        df.timeZone = NSTimeZone.localTimeZone
        df.dateFormat = outputFormat

        // Format the NSDate into a string in the local time zone
        return df.stringFromDate(date!!)
    }

    actual fun getFormattedTime(timestamp: String): String {
        val df = NSDateFormatter()
        val timestampFormat = "HH:mm:ss"
        val outputFormat = "HH:mm"

        df.dateFormat = timestampFormat

        // Set the time zone to GMT (or UTC)
        NSTimeZone.timeZoneWithAbbreviation("GMT")?.let { df.timeZone = it }

        // Parse the GMT timestamp into an NSDate
        val date = df.dateFromString(timestamp)

        // Create a date formatter for the local time zone
        df.timeZone = NSTimeZone.localTimeZone
        df.dateFormat = outputFormat

        // Format the NSDate into a string in the local time zone
        return df.stringFromDate(date!!)
    }

    actual fun formatTimeStamp(timeStamp: Long, outputFormat: String): String {
        val formatter = NSDateFormatter().apply {
            dateFormat = outputFormat
            timeZone = NSTimeZone.localTimeZone
        }
        val date = NSDate(timeStamp.toDouble() / 1000)
        return formatter.stringFromDate(date)
    }

    actual fun getDateInMilliSeconds(timeStamp: String, inputFormat: String): Long {
        if (timeStamp.trim().isEmpty()) return getCurrentTimeInMilliSeconds()

        val df = NSDateFormatter().apply {
            dateFormat = inputFormat
        }
        val date = df.dateFromString(timeStamp)
        return (date!!.timeIntervalSince1970 * 1000).toLong()
    }

    actual fun getCurrentTimeInMilliSeconds(): Long {
        return (NSDate().timeIntervalSince1970 * 1000).toLong()
    }

    actual fun getForwardedDate(
        forwardedDaya: Int,
        forwardedMonth: Int,
        outputFormat: String
    ): String {
        val calendar = NSCalendar.currentCalendar
        val currentDate = NSDate()
        val components = NSDateComponents().apply {
            day = forwardedDaya.toLong()
            month = forwardedMonth.toLong()
        }

        val forwardDate = calendar.dateByAddingComponents(components, currentDate, NSCalendarUnitDay)
        val dateFormatter = NSDateFormatter().apply {
            dateFormat = outputFormat
        }

        return dateFormatter.stringFromDate(forwardDate ?: currentDate)
    }
}

actual fun Format (value: Int): String{
    val numberFormatter = NSNumberFormatter()
    numberFormatter.numberStyle = NSNumberFormatterDecimalStyle
    return numberFormatter.stringFromNumber(NSNumber(value)) ?: ""
}

