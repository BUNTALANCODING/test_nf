package presentation.ui.main.kedatangabuss

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreTime
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import navbuss.shared.generated.resources.Res
import navbuss.shared.generated.resources.ic_empty_halte
import navbuss.shared.generated.resources.ic_jam
import navbuss.shared.generated.resources.ic_route_info
import org.jetbrains.compose.resources.painterResource
import kotlin.math.abs

data class RouteOption(
    val id: String,
    val number: Int,
    val name: String
)

data class BusArrivalItem(
    val routeNumber: Int,
    val destination: String,
    val stopName: String,
    val busCode: String,
    val etaLabel: String,
    val etaTime: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KedatanganBussScreen(
    modifier: Modifier = Modifier,
    routes: List<RouteOption> = sampleRoutes(),
    initialSelectedRouteId: String? = null,
    initialFromTime: String = "13:00",
    initialToTime: String = "14:00",
    arrivals: List<BusArrivalItem> = sampleArrivals(),

    onBack: () -> Unit,
    onRouteSelected: (RouteOption) -> Unit = {},
    onTimeChanged: (from: String, to: String) -> Unit = { _, _ -> },
    onArrivalClick: (BusArrivalItem) -> Unit = {}
) {
    val cs = MaterialTheme.colorScheme
    val pageBg = Color(0xFFF3F3F3)

    var selectedRoute by remember {
        mutableStateOf(routes.firstOrNull { it.id == initialSelectedRouteId })
    }

    var fromTime by remember { mutableStateOf(initialFromTime) }
    var toTime by remember { mutableStateOf(initialToTime) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val filteredArrivals by remember(selectedRoute, fromTime, toTime, arrivals) {
        derivedStateOf {
            val route = selectedRoute ?: return@derivedStateOf emptyList()
            val a = timeToMinutes(fromTime)
            val b = timeToMinutes(toTime)
            val lo = minOf(a, b)
            val hi = maxOf(a, b)
            arrivals.filter { item ->
                item.routeNumber == route.number &&
                        timeToMinutes(item.etaTime) in lo..hi
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { KedatanganBusTopBar(onBack = onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(pageBg)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                RouteDropdown(
                    label = "Pilih Rute",
                    routes = routes,
                    selected = selectedRoute,
                    onSelected = { r ->
                        selectedRoute = r
                        onRouteSelected(r)
                    }
                )

                Text(
                    text = "Tampilkan kedatangan bus :",
                    color = cs.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TimeFieldSheetPicker(
                        title = "Dari :",
                        value = fromTime,
                        enabled = selectedRoute != null,
                        onNeedRoute = {
                        },
                        onConfirm = { picked ->
                            fromTime = picked
                            if (timeToMinutes(fromTime) > timeToMinutes(toTime)) {
                                toTime = fromTime
                            }
                            onTimeChanged(fromTime, toTime)
                        },
                        modifier = Modifier.weight(1f)
                    )

                    TimeFieldSheetPicker(
                        title = "Sampai :",
                        value = toTime,
                        enabled = selectedRoute != null,
                        onNeedRoute = {
                            scope.launch { snackbarHostState.showSnackbar("Pilih rute dulu") }
                        },
                        onConfirm = { picked ->
                            toTime = picked
                            if (timeToMinutes(toTime) < timeToMinutes(fromTime)) {
                                fromTime = toTime
                            }
                            onTimeChanged(fromTime, toTime)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(4.dp))

                val showInfoHeader = selectedRoute != null && filteredArrivals.isNotEmpty()

                if (showInfoHeader) {
                    Text(
                        text = "Info Kedatangan Bus",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = cs.onSurface
                    )
                    Text(
                        text = "Menampilkan estimasi bus yang akan tiba di halte",
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onSurfaceVariant
                    )
                }


                if (selectedRoute == null) {

                    EmptySchedule()
                } else {
                    if (filteredArrivals.isEmpty()) {
                        EmptySchedule()
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            filteredArrivals.forEach { item ->
                                ArrivalCard(
                                    item = item,
                                    onClick = { onArrivalClick(item) }
                                )
                            }
                        }
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun RouteDropdown(
    label: String,
    routes: List<RouteOption>,
    selected: RouteOption?,
    onSelected: (RouteOption) -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    var expanded by remember { mutableStateOf(false) }
    var anchorWidthPx by remember { mutableIntStateOf(0) }
    val anchorWidthDp = with(LocalDensity.current) { anchorWidthPx.toDp() }

    Box(modifier = modifier.fillMaxWidth()) {
        Surface(
            color = cs.surface,
            shape = RoundedCornerShape(14.dp),
            shadowElevation = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { anchorWidthPx = it.size.width }
                .clickable { expanded = true }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(modifier = Modifier.size(24.dp), painter = painterResource(Res.drawable.ic_route_info), contentDescription = null, tint = Color(0xFFD4A93B))

                if (selected == null) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        color = cs.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    RouteNumberBadge(number = selected.number, color = routeColor(selected.number))
                    Text(
                        text = selected.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = cs.onSurface,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = cs.onSurfaceVariant
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(anchorWidthDp)
                .padding(top = 6.dp)
        ) {
            routes.forEachIndexed { idx, r ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            RouteNumberBadge(number = r.number, color = routeColor(r.number))
                            Text(
                                text = r.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    onClick = {
                        expanded = false
                        onSelected(r)
                    }
                )
                if (idx != routes.lastIndex) Divider()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeFieldSheetPicker(
    title: String,
    value: String,
    enabled: Boolean,
    onNeedRoute: () -> Unit,
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    var showSheet by remember { mutableStateOf(false) }

    Surface(
        color = cs.surface,
        shape = RoundedCornerShape(14.dp),
        shadowElevation = 1.dp,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (enabled) showSheet = true else onNeedRoute()
            }
    ) {
        Column(Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = cs.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(modifier = Modifier.size(24.dp), painter = painterResource(Res.drawable.ic_jam), contentDescription = null, tint = Color(0xFFD4A93B))
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = if (enabled) cs.onSurface else cs.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = cs.onSurfaceVariant
                )
            }
        }
    }

    if (showSheet) {
        TimePickerBottomSheet(
            initial = value,
            onDismiss = { showSheet = false },
            onPick = { picked ->
                showSheet = false
                onConfirm(picked)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerBottomSheet(
    initial: String,
    onDismiss: () -> Unit,
    onPick: (String) -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val (initH, initM) = remember(initial) { parseTime(initial) }
    var hour by remember { mutableStateOf(initH) }
    var minute by remember { mutableStateOf(initM) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = cs.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WheelIntPicker(
                range = 0..23,
                initialValue = hour,
                itemHeight = 44.dp,
                visibleCount = 5,
                highlightColor = Color(0xFFF3E2A7),
                format = { it.toString().padStart(2, '0') },
                onValueChange = { hour = it },
                modifier = Modifier.weight(1f)
            )

            WheelIntPicker(
                range = 0..59,
                initialValue = minute,
                itemHeight = 44.dp,
                visibleCount = 5,
                highlightColor = Color(0xFFF3E2A7),
                format = { it.toString().padStart(2, '0') },
                onValueChange = { minute = it },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(18.dp))

        Button(
            onClick = {
                val picked = "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
                onPick(picked)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Pilih Jam", fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun WheelIntPicker(
    range: IntRange,
    initialValue: Int,
    itemHeight: Dp,
    visibleCount: Int,
    highlightColor: Color,
    format: (Int) -> String,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = remember(range) { range.toList() }
    val initialIndex = remember(items, initialValue) { items.indexOf(initialValue).coerceAtLeast(0) }
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val pad = itemHeight * (visibleCount / 2)

    fun nearestIndex(): Int {
        val layout = listState.layoutInfo
        val visible = layout.visibleItemsInfo
        if (visible.isEmpty()) return listState.firstVisibleItemIndex

        val center = (layout.viewportStartOffset + layout.viewportEndOffset) / 2
        val nearest = visible.minByOrNull { info ->
            abs((info.offset + info.size / 2) - center)
        }
        return nearest?.index ?: listState.firstVisibleItemIndex
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect {
                val idx = nearestIndex().coerceIn(0, items.lastIndex)
                onValueChange(items[idx])
            }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .collect { scrolling ->
                if (!scrolling) {
                    val idx = nearestIndex().coerceIn(0, items.lastIndex)
                    scope.launch { listState.animateScrollToItem(idx) }
                }
            }
    }

    Box(
        modifier = modifier
            .height(itemHeight * visibleCount)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .clip(RoundedCornerShape(12.dp))
                .background(highlightColor)
        )

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = pad),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(items.size) { idx ->
                val v = items[idx]
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .clickable {
                            scope.launch { listState.animateScrollToItem(idx) }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = format(v),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun ArrivalCard(
    item: BusArrivalItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val etaBg = Color(0xFFF3E2A7)

    Surface(
        color = cs.surface,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 1.dp,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RouteNumberBadge(number = item.routeNumber, color = routeColor(item.routeNumber))
                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = "Tujuan Akhir : ",
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onSurfaceVariant
                    )
                    Text(
                        text = item.destination,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = cs.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Tiba di halte",
                    style = MaterialTheme.typography.bodySmall,
                    color = cs.onSurfaceVariant
                )

                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.DirectionsBus,
                        contentDescription = null,
                        tint = Color(0xFFD4A93B),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = item.stopName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = cs.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                BusCodeChip(code = item.busCode)
                Spacer(Modifier.height(8.dp))
                Surface(color = etaBg, shape = RoundedCornerShape(12.dp)) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "EstimasiTiba",
                            style = MaterialTheme.typography.labelSmall,
                            color = cs.onSurface
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "${item.etaLabel} (${item.etaTime})",
                            style = MaterialTheme.typography.bodySmall,
                            color = cs.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BusCodeChip(code: String, modifier: Modifier = Modifier) {
    val chipBg = Color(0xFF4C90B8)
    Surface(
        color = chipBg,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.DirectionsBus,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = code,
                color = Color.White,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun RouteNumberBadge(
    number: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = color,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.size(28.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = number.toString(),
                color = Color.White,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun EmptySchedule(modifier: Modifier = Modifier) {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Icon(
                painter = painterResource(Res.drawable.ic_empty_halte),
                contentDescription = null,
                tint = cs.outlineVariant,
                modifier = Modifier.fillMaxSize()
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KedatanganBusTopBar(
    title: String = "Info Kedatangan Bus",
    onBack: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Column(Modifier.fillMaxWidth()) {
        Box(
            Modifier
                .fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .background(cs.background)
        )

        TopAppBar(
            title = {
                Text(
                    text = title,
                    color = cs.onSecondary,
                    fontWeight = FontWeight.SemiBold
                )
            },
            actions = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Kembali",
                        tint = cs.onSecondary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = cs.primary),
            windowInsets = WindowInsets(0)
        )
    }
}

private fun parseTime(value: String): Pair<Int, Int> {
    val parts = value.split(":")
    val h = parts.getOrNull(0)?.toIntOrNull() ?: 0
    val m = parts.getOrNull(1)?.toIntOrNull() ?: 0
    return h.coerceIn(0, 23) to m.coerceIn(0, 59)
}

private fun timeToMinutes(value: String): Int {
    val (h, m) = parseTime(value)
    return h * 60 + m
}

private fun routeColor(routeNumber: Int): Color = when (routeNumber) {
    1 -> Color(0xFF4C90B8)
    2 -> Color(0xFF1B2A7A)
    5 -> Color(0xFFF39B2B)
    6 -> Color(0xFFB64A3A)
    else -> Color(0xFF4C90B8)
}

private fun sampleRoutes(): List<RouteOption> = listOf(
    RouteOption(id = "1", number = 1, name = "Terminal Bubulak - Cidangiang"),
    RouteOption(id = "2", number = 2, name = "Terminal Bubulak - Ciawi"),
    RouteOption(id = "5", number = 5, name = "Terminal Ciparigi - Stasiun KA Bogor"),
    RouteOption(id = "6", number = 6, name = "Parung Banteng - Stasiun KA Bogor")
)

private fun sampleArrivals(): List<BusArrivalItem> = listOf(
    BusArrivalItem(1, "Cidangiang", "Tugu Narkoba 2", "TP 038", "<1 menit", "13:16"),
    BusArrivalItem(1, "Cidangiang", "Tugu Narkoba 2", "TP 024", "3 menit", "13:19"),
    BusArrivalItem(1, "Cidangiang", "Dinas Pendidikan", "TP 038", "5 menit", "13:24"),
    BusArrivalItem(1, "Terminal Bubulak", "Taman Ekspresi", "TP 068", "6 menit", "13:30"),
    BusArrivalItem(1, "Terminal Bubulak", "Bantar Jati", "TP 038", "20 menit", "13:50"),

    BusArrivalItem(2, "Ciawi", "Baranangsiang", "TP 010", "2 menit", "13:10"),
    BusArrivalItem(5, "Stasiun KA Bogor", "Ciparigi", "TP 055", "8 menit", "13:40")
)
