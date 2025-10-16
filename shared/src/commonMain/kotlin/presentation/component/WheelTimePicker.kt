package presentation.component

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import logger.Logger

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WheelTimePicker(
    times: List<Time>,
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTime by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            it != SheetValue.Hidden
        }
    )

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = null,
        sheetState = sheetState,
    ) {
        Spacer_16dp()

        androidx.compose.material3.IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer_8dp()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {

            Text(
                "Pilih Waktu Check in",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer_4dp()
            Text(
                "Anda harus masuk ke pelabuhan sebelum waktu di bawah ini.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            *//*Text(
                text = "Choose Time",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )*//*

            TimePicker(
                onTimeSelected = { time ->
                    selectedTime = time
                },
                items = times.toList(),
                visibleItemsCount = 5,
                modifier = Modifier.fillMaxWidth(0.5f),
                textModifier = Modifier.padding(8.dp),
                textStyle = TextStyle(fontSize = 32.sp),
                dividerColor = Color(0xFFE8E8E8)
            )

            *//*Box(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(vertical = 50.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(times) { index, time ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(index)
                                        selectedTime = time
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            val isSelected = selectedTime == time

                            Text(
                                text = time.timeAt ?: "-",
                                style = if (isSelected) {
                                    MaterialTheme.typography.titleLarge.copy(
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                } else {
                                    MaterialTheme.typography.bodyLarge
                                }
                            )
                        }
                    }
                }

                // Overlay selection border
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .height(50.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RectangleShape
                        )
                )
            }*//*

            Spacer_16dp()

            DefaultButton(
                text = "Simpan",
                onClick = { onTimeSelected(selectedTime) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

@Composable
fun TimePicker(
    items: List<Time>,
    onTimeSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    startIndex: Int = 0,
    visibleItemsCount: Int = 3,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    dividerColor: Color = LocalContentColor.current,
) {

    if (items.isEmpty()) {

        Box(modifier = modifier) {
            Text("No time options available", style = textStyle, modifier = textModifier)
        }
        return
    }

    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = Int.MAX_VALUE
    val listScrollMiddle = listScrollCount / 2
    //TEMPORARY FIX
    val listStartIndex = if (items.isNotEmpty()) {
        listScrollMiddle - (listScrollMiddle % items.size) - visibleItemsMiddle + startIndex
    } else {
        0 // or some fallback value
    }

    fun getItem(index: Int) = items[index % items.size]

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val itemHeightPixels = remember { mutableIntStateOf(0) }
    val itemHeightDp = pixelsToDp(itemHeightPixels.value)

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> getItem(index + visibleItemsMiddle) }
            .distinctUntilChanged()
            .collect { item -> onTimeSelected(item.timeAt ?: "-") }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient)
        ) {
            items(listScrollCount) { data ->
                Text(
                    text = getItem(data).timeAt ?: "-",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    style = textStyle,
                    modifier = Modifier
                        .onSizeChanged { size -> itemHeightPixels.value = size.height }
                        .then(textModifier)
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(top = itemHeightDp * visibleItemsMiddle)
                .height(1.dp),
            color = dividerColor
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(top = (itemHeightDp * visibleItemsMiddle) + itemHeightDp)
                .height(1.dp),
            color = dividerColor
        )
    }
}

fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Composable
private fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }*/

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WheelTimePicker(
    isToday: Boolean,
    times: List<Time>,
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {

    // Get current time
    val currentTimeString = getCurrentTimeString()

    val availableTimes = remember(times, currentTimeString, isToday) {
        if (!isToday) {
            // If it's not today, show all valid times (future date)
            times.filter { time ->
                isValidTimeFormat(time.timeAt)
            }.sortedBy { time ->
                parseToLocalTime(time.timeAt)
            }
        } else {
            // If it's today, filter only future times
            val currentTime = parseToLocalTime(currentTimeString)

            times.filter { time ->
                val timeString = time.timeAt
                if (timeString.isNullOrBlank()) return@filter false

                val timeSlot = parseToLocalTime(timeString)

                // Only include times that are after current time
                timeSlot != null && currentTime != null && timeSlot > currentTime
            }.sortedBy { time ->
                parseToLocalTime(time.timeAt)
            }
        }
    }

    // Set initial selected time to the first available time
    var selectedTime by remember(availableTimes) {
        mutableStateOf(availableTimes.firstOrNull()?.timeAt ?: "")
    }

    // Debug information
    LaunchedEffect(availableTimes) {
        Logger.i("Available times count: ${availableTimes.size}")
        availableTimes.take(5).forEach { time ->
            Logger.i("Available time: ${time.timeAt}")
        }
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            it != SheetValue.Hidden
        }
    )

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = null,
        sheetState = sheetState,
    ) {
        Spacer_16dp()

        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer_8dp()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Text(
                "Pilih Waktu Check in",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer_4dp()
            Text(
                "Anda harus masuk ke pelabuhan sebelum waktu di bawah ini.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            */
/*Spacer_8dp()
            Text(
                "Waktu sekarang: $currentTimeString",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )*//*

        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (availableTimes.isEmpty()) {
                Text(
                    text = "Tidak ada waktu tersedia untuk hari ini",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(32.dp)
                )
            } else {
                TimePicker(
                    onTimeSelected = { time ->
                        selectedTime = time
                    },
                    items = availableTimes.toList(),
                    visibleItemsCount = 5,
                    modifier = Modifier.fillMaxWidth(0.5f),
                    textModifier = Modifier.padding(8.dp),
                    textStyle = TextStyle(fontSize = 32.sp),
                    dividerColor = Color(0xFFE8E8E8)
                )
            }

            Spacer_16dp()

            DefaultButton(
                text = "Simpan",
                onClick = { onTimeSelected(selectedTime) },
                enabled = selectedTime.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

@Composable
fun TimePicker(
    items: List<Time>,
    onTimeSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    startIndex: Int = 0,
    visibleItemsCount: Int = 3,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    dividerColor: Color = LocalContentColor.current,
) {
    if (items.isEmpty()) {
        Box(modifier = modifier) {
            Text("No time options available", style = textStyle, modifier = textModifier)
        }
        return
    }

    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = Int.MAX_VALUE
    val listScrollMiddle = listScrollCount / 2

    // Calculate start index to show first available time as selected
    val adjustedStartIndex = 0 // Start with first item
    val listStartIndex = if (items.isNotEmpty()) {
        listScrollMiddle - (listScrollMiddle % items.size) - visibleItemsMiddle + adjustedStartIndex
    } else {
        0
    }

    fun getItem(index: Int) = items[index % items.size]

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val itemHeightPixels = remember { mutableIntStateOf(0) }
    val itemHeightDp = pixelsToDp(itemHeightPixels.value)

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }

    // Track selection and notify parent
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> getItem(index + visibleItemsMiddle) }
            .distinctUntilChanged()
            .collect { item -> onTimeSelected(item.timeAt ?: "-") }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient)
        ) {
            items(listScrollCount) { data ->
                val item = getItem(data)
                val currentIndex = listState.firstVisibleItemIndex + visibleItemsMiddle
                val isSelected = data == currentIndex

                Text(
                    text = item.timeAt ?: "-",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    style = textStyle.copy(
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            textStyle.color.copy(alpha = 0.6f)
                        },
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    ),
                    modifier = Modifier
                        .onSizeChanged { size -> itemHeightPixels.value = size.height }
                        .then(textModifier)
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(top = itemHeightDp * visibleItemsMiddle)
                .height(1.dp),
            color = dividerColor
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(top = (itemHeightDp * visibleItemsMiddle) + itemHeightDp)
                .height(1.dp),
            color = dividerColor
        )
    }
}
*/

fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Composable
private fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }

fun isValidTimeFormat(timeString: String?): Boolean {
    return parseToLocalTime(timeString) != null
}

// Helper function to parse time string using kotlinx.datetime
fun parseToLocalTime(timeString: String?): LocalTime? {
    if (timeString.isNullOrBlank()) return null

    return try {
        // Define time format for HH:mm
        val timeFormat = LocalTime.Format {
            hour()
            char(':')
            minute()
        }

        LocalTime.parse(timeString, timeFormat)
    } catch (e: IllegalArgumentException) {
        // Fallback: try to parse manually if format parsing fails
        try {
            val parts = timeString.split(":")
            if (parts.size == 2) {
                val hour = parts[0].toIntOrNull()
                val minute = parts[1].toIntOrNull()

                if (hour != null && minute != null && hour in 0..23 && minute in 0..59) {
                    LocalTime(hour, minute)
                } else null
            } else null
        } catch (e: Exception) {
            null
        }
    }
}

// Enhanced helper function with midnight handling
fun isTimePastWithMidnightHandling(timeString: String, currentTimeString: String): Boolean {
    val timeParts = timeString.split(":")
    val currentParts = currentTimeString.split(":")

    if (timeParts.size != 2 || currentParts.size != 2) return false

    val timeInMinutes = (timeParts[0].toIntOrNull() ?: 0) * 60 + (timeParts[1].toIntOrNull() ?: 0)
    val currentInMinutes =
        (currentParts[0].toIntOrNull() ?: 0) * 60 + (currentParts[1].toIntOrNull() ?: 0)

    return when {
        // If current time is after midnight (00:00 - 06:00)
        currentInMinutes < 360 -> {
            // Time is past if it's before current time AND not in evening (after 18:00)
            timeInMinutes < currentInMinutes && timeInMinutes < 1080
        }
        // Normal case
        else -> timeInMinutes < currentInMinutes
    }
}

/*
// Helper function to get available times with midnight logic
fun getAvailableTimesWithMidnightHandling(
    times: List<Time>,
    currentTimeString: String
): List<Time> {
    val currentParts = currentTimeString.split(":")
    val currentTimeInMinutes = if (currentParts.size == 2) {
        (currentParts[0].toIntOrNull() ?: 0) * 60 + (currentParts[1].toIntOrNull() ?: 0)
    } else 0

    return times.filter { time ->
        val timeString = time.timeAt ?: return@filter false
        val timeParts = timeString.split(":")
        if (timeParts.size == 2) {
            val timeInMinutes =
                (timeParts[0].toIntOrNull() ?: 0) * 60 + (timeParts[1].toIntOrNull() ?: 0)

            when {
                // If current time is after midnight (00:00 - 06:00)
                currentTimeInMinutes < 360 -> {
                    // Show times from current time until 06:00, and times from 18:00 to 23:59
                    timeInMinutes >= currentTimeInMinutes || timeInMinutes >= 1080 // After 18:00
                }
                // Normal case: show times from current time onwards
                else -> timeInMinutes >= currentTimeInMinutes
            }
        } else false
    }.sortedBy { time ->
        val timeString = time.timeAt ?: return@sortedBy Int.MAX_VALUE
        val timeParts = timeString.split(":")
        if (timeParts.size == 2) {
            val timeInMinutes =
                (timeParts[0].toIntOrNull() ?: 0) * 60 + (timeParts[1].toIntOrNull() ?: 0)

            when {
                currentTimeInMinutes < 360 -> {
                    if (timeInMinutes >= currentTimeInMinutes && timeInMinutes < 360) {
                        timeInMinutes // Early morning times first
                    } else {
                        timeInMinutes + 1440 // Evening times later
                    }
                }

                else -> timeInMinutes
            }
        } else Int.MAX_VALUE
    }
}
*/

@Composable
fun getCurrentTimeString(): String {
    return remember {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        "${now.hour.toString().padStart(2, '0')}:${now.minute.toString().padStart(2, '0')}"
    }
}

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WheelTimePicker(
    times: List<Time>,
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    // Get current time
    val currentTimeString = getCurrentTimeString()

    // Enhanced filter for times with midnight handling
    val availableTimes = remember(times, currentTimeString) {
        val currentParts = currentTimeString.split(":")
        val currentTimeInMinutes = if (currentParts.size == 2) {
            (currentParts[0].toIntOrNull() ?: 0) * 60 + (currentParts[1].toIntOrNull() ?: 0)
        } else 0

        times.filter { time ->
            val timeString = time.timeAt ?: return@filter false
            val timeParts = timeString.split(":")
            if (timeParts.size == 2) {
                val timeInMinutes = (timeParts[0].toIntOrNull() ?: 0) * 60 + (timeParts[1].toIntOrNull() ?: 0)

                // Handle midnight crossing scenario
                when {
                    // If current time is after midnight (00:00 - 06:00), show times until midnight (23:59)
                    currentTimeInMinutes < 360 -> { // Before 6 AM
                        // Show times from current time until midnight (23:59)
                        timeInMinutes >= currentTimeInMinutes || timeInMinutes >= 1440 - 60 // Last hour of day
                    }
                    // Normal case: show times from current time onwards
                    else -> {
                        timeInMinutes >= currentTimeInMinutes
                    }
                }
            } else false
        }.sortedBy { time ->
            // Sort times properly considering midnight crossing
            val timeString = time.timeAt ?: return@sortedBy Int.MAX_VALUE
            val timeParts = timeString.split(":")
            if (timeParts.size == 2) {
                val timeInMinutes = (timeParts[0].toIntOrNull() ?: 0) * 60 + (timeParts[1].toIntOrNull() ?: 0)

                when {
                    // If current time is after midnight, prioritize times from current time first
                    currentTimeInMinutes < 360 -> {
                        if (timeInMinutes >= currentTimeInMinutes && timeInMinutes < 360) {
                            timeInMinutes // Early morning times first
                        } else {
                            timeInMinutes + 1440 // Evening times later
                        }
                    }
                    else -> timeInMinutes
                }
            } else Int.MAX_VALUE
        }
    }

    // Set initial selected time to the first available time
    var selectedTime by remember(availableTimes) {
        mutableStateOf(availableTimes.firstOrNull()?.timeAt ?: "")
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            it != SheetValue.Hidden
        }
    )

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = null,
        sheetState = sheetState,
    ) {
        Spacer_16dp()

        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer_8dp()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Text(
                "Pilih Waktu Check in",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer_4dp()
            Text(
                "Anda harus masuk ke pelabuhan sebelum waktu di bawah ini.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            */
/*Spacer_8dp()
            Text(
                "Waktu sekarang: $currentTimeString",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )*//*

        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (availableTimes.isEmpty()) {
                Text(
                    text = "Tidak ada waktu tersedia untuk hari ini",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(32.dp)
                )
            } else {
                TimePicker(
                    onTimeSelected = { time ->
                        selectedTime = time
                    },
                    items = availableTimes.toList(),
                    visibleItemsCount = 5,
                    modifier = Modifier.fillMaxWidth(0.5f),
                    textModifier = Modifier.padding(8.dp),
                    textStyle = TextStyle(fontSize = 32.sp),
                    dividerColor = Color(0xFFE8E8E8)
                )
            }

            Spacer_16dp()

            DefaultButton(
                text = "Simpan",
                onClick = { onTimeSelected(selectedTime) },
                enabled = selectedTime.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

@Composable
fun TimePicker(
    items: List<Time>,
    onTimeSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    startIndex: Int = 0,
    visibleItemsCount: Int = 3,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    dividerColor: Color = LocalContentColor.current,
) {
    val hapticFeedback = LocalHapticFeedback.current

    if (items.isEmpty()) {
        Box(modifier = modifier) {
            Text("Tidak ada waktu yang tersedia", style = textStyle, modifier = textModifier)
        }
        return
    }

    val visibleItemsMiddle = visibleItemsCount / 2

    // Use finite scroll - only the actual items, no infinite loop
    val paddedItems = remember(items) {
        // Add padding items at start and end for proper centering
        val padding = List(visibleItemsMiddle) { Time(timeAt = "") }
        padding + items + padding
    }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = visibleItemsMiddle + startIndex
    )

    // Custom snap behavior with haptic feedback
    val flingBehavior = remember {
        object : FlingBehavior {
            override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                val targetIndex = if (initialVelocity > 0) {
                    (listState.firstVisibleItemIndex + 1).coerceAtMost(paddedItems.size - visibleItemsCount)
                } else {
                    (listState.firstVisibleItemIndex - 1).coerceAtLeast(0)
                }

                listState.animateScrollToItem(targetIndex)
                return 0f
            }
        }
    }

    val itemHeightPixels = remember { mutableIntStateOf(0) }
    val itemHeightDp = pixelsToDp(itemHeightPixels.value)

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }

    // Track selection with haptic feedback
    LaunchedEffect(listState) {
        var lastSelectedIndex = -1

        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index ->
                val selectedIndex = index + visibleItemsMiddle
                val item = paddedItems.getOrNull(selectedIndex)
                selectedIndex to item
            }
            .distinctUntilChanged()
            .collect { (index, item) ->
                if (lastSelectedIndex != -1 && lastSelectedIndex != index) {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }
                lastSelectedIndex = index

                if (item?.timeAt?.isNotEmpty() == true) {
                    onTimeSelected(item.timeAt.orEmpty())
                }
            }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onDragEnd = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onDrag = { _, dragAmount ->
                            // Provide haptic feedback during significant drag
                            if (abs(dragAmount.y) > 20) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        // Calculate which item was tapped
                        val itemHeight = itemHeightDp.toPx()
                        val tappedIndex = (offset.y / itemHeight).toInt()
                        val targetIndex = listState.firstVisibleItemIndex + tappedIndex - visibleItemsMiddle

                        // Ensure target is within bounds
                        val clampedTarget = targetIndex.coerceIn(0, paddedItems.size - visibleItemsCount)

                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                            listState.animateScrollToItem(clampedTarget)
                        }
                    }
                }
        ) {
            itemsIndexed(paddedItems) { index, item ->
                val currentCenterIndex = listState.firstVisibleItemIndex + visibleItemsMiddle
                val isSelected = index == currentCenterIndex
                val distance = kotlin.math.abs(index - currentCenterIndex)

                // Calculate alpha and scale based on distance from center
                val alpha = when (distance) {
                    0 -> 1f
                    1 -> 0.7f
                    2 -> 0.4f
                    else -> 0.2f
                }

                val scale = when (distance) {
                    0 -> 1f
                    1 -> 0.8f
                    else -> 0.6f
                }

                Text(
                    text = item.timeAt ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    style = textStyle.copy(
                        color = if (isSelected && item.timeAt?.isNotEmpty() == true) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            textStyle.color.copy(alpha = alpha)
                        },
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = textStyle.fontSize * scale
                    ),
                    modifier = Modifier
                        .onSizeChanged { size ->
                            if (itemHeightPixels.value == 0) {
                                itemHeightPixels.value = size.height
                            }
                        }
                        .then(textModifier)
                        .alpha(if (item.timeAt?.isEmpty() == true) 0f else alpha)
                        .clickable(
                            enabled = item.timeAt?.isNotEmpty() == true,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                                listState.animateScrollToItem(index - visibleItemsMiddle)
                            }
                        }
                )
            }
        }

        */
/*HorizontalDivider(
            modifier = Modifier
                .padding(top = itemHeightDp * visibleItemsMiddle)
                .height(1.dp),
            color = dividerColor
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(top = (itemHeightDp * visibleItemsMiddle) + itemHeightDp)
                .height(1.dp),
            color = dividerColor
        )*//*

    }
}

fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Composable
private fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }

// Enhanced helper function with midnight handling
fun isTimePastWithMidnightHandling(timeString: String, currentTimeString: String): Boolean {
    val timeParts = timeString.split(":")
    val currentParts = currentTimeString.split(":")

    if (timeParts.size != 2 || currentParts.size != 2) return false

    val timeInMinutes = (timeParts[0].toIntOrNull() ?: 0) * 60 + (timeParts[1].toIntOrNull() ?: 0)
    val currentInMinutes = (currentParts[0].toIntOrNull() ?: 0) * 60 + (currentParts[1].toIntOrNull() ?: 0)

    return when {
        // If current time is after midnight (00:00 - 06:00)
        currentInMinutes < 360 -> {
            // Time is past if it's before current time AND not in evening (after 18:00)
            timeInMinutes < currentInMinutes && timeInMinutes < 1080
        }
        // Normal case
        else -> timeInMinutes < currentInMinutes
    }
}

// Helper function to get available times with midnight logic
fun getAvailableTimesWithMidnightHandling(times: List<Time>, currentTimeString: String): List<Time> {
    val currentParts = currentTimeString.split(":")
    val currentTimeInMinutes = if (currentParts.size == 2) {
        //(currentParts[0].toIntOrNull() ?: 0)
        (currentParts[0].toIntOrNull() ?: 0) * 60 + (currentParts[1].toIntOrNull() ?: 0)
    } else 0

    return times.filter { time ->
        val timeString = time.timeAt ?: return@filter false
        val timeParts = timeString.split(":")
        if (timeParts.size == 2) {
            val timeInMinutes = (timeParts[0].toIntOrNull() ?: 0) * 60 + (timeParts[1].toIntOrNull() ?: 0)

            when {
                // If current time is after midnight (00:00 - 06:00)
                currentTimeInMinutes < 360 -> {
                    // Show times from current time until 06:00, and times from 18:00 to 23:59
                    timeInMinutes >= currentTimeInMinutes || timeInMinutes >= 1080 // After 18:00
                }
                // Normal case: show times from current time onwards
                else -> timeInMinutes >= currentTimeInMinutes
            }
        } else false
    }.sortedBy { time ->
        val timeString = time.timeAt ?: return@sortedBy Int.MAX_VALUE
        val timeParts = timeString.split(":")
        if (timeParts.size == 2) {
            val timeInMinutes = (timeParts[0].toIntOrNull() ?: 0) * 60 + (timeParts[1].toIntOrNull() ?: 0)

            when {
                currentTimeInMinutes < 360 -> {
                    if (timeInMinutes >= currentTimeInMinutes && timeInMinutes < 360) {
                        timeInMinutes // Early morning times first
                    } else {
                        timeInMinutes + 1440 // Evening times later
                    }
                }
                else -> timeInMinutes
            }
        } else Int.MAX_VALUE
    }
}

@Composable
fun getCurrentTimeString(): String {
    return remember {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        "${now.hour.toString().padStart(2, '0')}:${now.minute.toString().padStart(2, '0')}"
    }
}

// Enhanced TimePicker with better scroll control and haptic feedback
@Composable
fun TimePickerOld(
    items: List<Time>,
    onTimeSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    visibleItemsCount: Int = 5
) {
    val hapticFeedback = LocalHapticFeedback.current
    var isDragging by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }

    if (items.isEmpty()) {
        Box(modifier = modifier) {
            Text(
                text = "No time options available",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    val visibleItemsMiddle = visibleItemsCount / 2
    val itemHeightDp = 60.dp

    // Create padded list for proper centering without infinite scroll
    val paddedItems = remember(items) {
        val emptyPadding = List(visibleItemsMiddle) { Time(timeAt = "") }
        emptyPadding + items + emptyPadding
    }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = visibleItemsMiddle
    )

    // Custom fling behavior with haptic feedback and snapping
    val customFlingBehavior = remember {
        object : FlingBehavior {
            override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                // Calculate target index based on velocity
                val currentIndex = listState.firstVisibleItemIndex
                val targetIndex = if (initialVelocity > 500) {
                    (currentIndex + 1).coerceAtMost(paddedItems.size - visibleItemsCount)
                } else if (initialVelocity < -500) {
                    (currentIndex - 1).coerceAtLeast(0)
                } else {
                    currentIndex
                }

                listState.animateScrollToItem(targetIndex)
                return 0f
            }
        }
    }

    // Track scroll state and provide haptic feedback
    LaunchedEffect(listState) {
        var lastIndex = -1

        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { isScrolling ->
                if (isScrolling && !isDragging) {
                    isDragging = true
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                } else if (!isScrolling && isDragging) {
                    isDragging = false
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            }
    }

    // Track selection changes
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> index + visibleItemsMiddle }
            .distinctUntilChanged()
            .collect { centerIndex ->
                if (selectedIndex != centerIndex && selectedIndex != 0) {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }
                selectedIndex = centerIndex

                val item = paddedItems.getOrNull(centerIndex)
                if (item?.timeAt?.isNotEmpty() == true) {
                    onTimeSelected(item.timeAt.orEmpty())
                }
            }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            flingBehavior = customFlingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * visibleItemsCount)
                .fadingEdge(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.2f to Color.Black,
                        0.8f to Color.Black,
                        1f to Color.Transparent
                    )
                )
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            isDragging = true
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onDragEnd = {
                            isDragging = false
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onDrag = { _, dragAmount ->
                            if (kotlin.math.abs(dragAmount.y) > 15) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        val itemHeight = itemHeightDp.toPx()
                        val tappedItemIndex = (offset.y / itemHeight).toInt()
                        val targetScrollIndex = listState.firstVisibleItemIndex + tappedItemIndex - visibleItemsMiddle

                        val clampedTarget = targetScrollIndex.coerceIn(
                            0,
                            (paddedItems.size - visibleItemsCount).coerceAtLeast(0)
                        )

                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                            listState.animateScrollToItem(clampedTarget)
                        }
                    }
                }
        ) {
            itemsIndexed(paddedItems) { index, item ->
                val centerIndex = listState.firstVisibleItemIndex + visibleItemsMiddle
                val isSelected = index == centerIndex
                val distance = kotlin.math.abs(index - centerIndex)

                val alpha = when (distance) {
                    0 -> 1f
                    1 -> 0.7f
                    2 -> 0.4f
                    else -> 0.2f
                }

                val scale = when (distance) {
                    0 -> 1.1f
                    1 -> 0.9f
                    else -> 0.7f
                }

                Box(
                    modifier = Modifier
                        .height(itemHeightDp)
                        .fillMaxWidth()
                        .clickable(
                            enabled = item.timeAt?.isNotEmpty() == true,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                                val targetIndex = (index - visibleItemsMiddle).coerceIn(
                                    0,
                                    (paddedItems.size - visibleItemsCount).coerceAtLeast(0)
                                )
                                listState.animateScrollToItem(targetIndex)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.timeAt ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = when {
                                item.timeAt?.isEmpty() == true -> Color.Transparent
                                isSelected -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.onSurface.copy(alpha = alpha)
                            },
                            fontSize = (28 * scale).sp
                        ),
                        modifier = Modifier
                            .alpha(if (item.timeAt?.isEmpty() == true) 0f else alpha)
                            .scale(scale)
                    )
                }
            }
        }

        // Selection indicator
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.8f)
                .height(itemHeightDp)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    RoundedCornerShape(12.dp)
                )
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    RoundedCornerShape(12.dp)
                )
        )

        // Top divider
        HorizontalDivider(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = itemHeightDp * visibleItemsMiddle)
                .fillMaxWidth(0.6f),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )

        // Bottom divider
        HorizontalDivider(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = itemHeightDp * (visibleItemsMiddle + 1))
                .fillMaxWidth(0.6f),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )

        // Drag indicator
        if (isDragging) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                        RoundedCornerShape(12.dp)
                    )
            )
        }
    }
}

// Utility function to create bounded scroll behavior
fun createBoundedScrollBehavior(
    itemCount: Int,
    hapticFeedback: HapticFeedback
): FlingBehavior {
    return object : FlingBehavior {
        override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            return 0f
        }
    }
}*/
