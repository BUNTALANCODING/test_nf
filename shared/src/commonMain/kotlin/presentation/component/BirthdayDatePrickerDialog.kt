package presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import network.chaintech.kmp_date_time_picker.utils.now

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayDatePrickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    // Indonesia timezone (WIB = UTC+7)
    val indonesiaTimeZone = TimeZone.of("Asia/Jakarta")

    // Get today's date in Indonesia timezone
    val todayIndonesia = Clock.System.now().toLocalDateTime(indonesiaTimeZone).date

    // Convert Indonesia date to UTC milliseconds for DatePicker
    val todayMillisUtc = todayIndonesia.atTime(0, 0, 0, 0)
        .toInstant(TimeZone.UTC)
        .toEpochMilliseconds()

    val datePickerState = rememberDatePickerState(
        initialDisplayedMonthMillis = todayMillisUtc,
        initialSelectedDateMillis = todayMillisUtc, // Optional: if you want today to be pre-selected
        selectableDates = TodayOnlySelectableDates
    )

    val selectedDateMillis = datePickerState.selectedDateMillis
    val selectedDate = selectedDateMillis?.let {
        Instant.fromEpochMilliseconds(it)
            .toLocalDateTime(TimeZone.UTC)
            .date
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        dragHandle = null,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Spacer_16dp()

        androidx.compose.material3.IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(containerColor = Color.White)
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            DefaultButton(
                text = "Simpan",
                onClick = { onConfirm(selectedDate.toString()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//object PastOrPresentSelectableDates2: SelectableDates {
//    // Use Indonesia timezone for calculating "today"
//    private val indonesiaTimeZone = TimeZone.of("Asia/Jakarta")
//    private val nowIndonesia = Clock.System.now().toLocalDateTime(indonesiaTimeZone).date
//    private val dayStartUtc = nowIndonesia.atTime(0, 0, 0, 0).toInstant(TimeZone.UTC).toEpochMilliseconds()
//
//    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
//        return utcTimeMillis <= dayStartUtc
//    }
//
//    override fun isSelectableYear(year: Int): Boolean {
//        return year <= LocalDate.now().year
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
object TodayOnlySelectableDates : SelectableDates {

    private val indonesiaTimeZone = TimeZone.of("Asia/Jakarta")

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        // Tanggal yang lagi dicek DatePicker (dari millis → LocalDate Indonesia)
        val dateFromPicker = Instant.fromEpochMilliseconds(utcTimeMillis)
            .toLocalDateTime(indonesiaTimeZone)
            .date

        // Tanggal hari ini di Indonesia
        val todayIndonesia = Clock.System.now()
            .toLocalDateTime(indonesiaTimeZone)
            .date

        // HANYA boleh kalau tepat HARI INI
        return dateFromPicker == todayIndonesia
    }

    override fun isSelectableYear(year: Int): Boolean {
        val todayYear = Clock.System.now()
            .toLocalDateTime(indonesiaTimeZone)
            .date
            .year

        // cuma tahun sekarang yang boleh
        return year == todayYear
    }
}
