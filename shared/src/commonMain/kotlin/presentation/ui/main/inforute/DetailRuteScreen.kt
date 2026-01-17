package presentation.ui.main.inforute

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import business.datasource.network.main.dto.response.RouteDetailResponse
import business.datasource.network.main.dto.response.TripResponse
import common.map.PlatformRouteMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import navbuss.shared.generated.resources.Res
import navbuss.shared.generated.resources.ic_close
import navbuss.shared.generated.resources.ic_end_halte
import navbuss.shared.generated.resources.ic_expand
import navbuss.shared.generated.resources.ic_info_detail
import navbuss.shared.generated.resources.ic_map
import navbuss.shared.generated.resources.ic_start_halte
import navbuss.shared.generated.resources.ic_swap
import org.jetbrains.compose.resources.painterResource
import presentation.ui.main.inforute.view_model.BusMapUi
import presentation.ui.main.inforute.view_model.FakeRouteMapRepository
import presentation.ui.main.inforute.view_model.GeoPoint
import presentation.ui.main.inforute.view_model.StopInfo
import presentation.ui.main.inforute.view_model.StopMapUi
import presentation.ui.main.inforute.view_model.detail.DetailRuteAction
import presentation.ui.main.inforute.view_model.detail.DetailRuteViewModel
import presentation.ui.main.inforute.view_model.map.RouteMapViewModel
import kotlin.math.roundToInt

private enum class MapMode { Hidden, Inline, Full }
private enum class PointerDirection { Down, Up }

private fun String?.toRouteStatus(): RouteStatus {
    val s = this?.trim()?.lowercase().orEmpty()
    return when {
        s.contains("aktif") || s.contains("active") || s.contains("beroperasi") -> RouteStatus.ACTIVE
        s.contains("hampir") || s.contains("almost") -> RouteStatus.ALMOST_DONE
        s.contains("tutup") || s.contains("ended") || s.contains("non") || s.contains("tidak") -> RouteStatus.ENDED
        else -> RouteStatus.ACTIVE
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailRuteScreen(
    corridorCode: String,
    onBack: () -> Unit,
    onOpenMap: () -> Unit,
    onBusDetail: (busId: String) -> Unit = {},
    onHalteClick: (halteId: String) -> Unit = {}
) {
    val cs = MaterialTheme.colorScheme
    val vm: DetailRuteViewModel = org.koin.compose.koinInject()
    val trip by vm.trip.collectAsState()
    val state by vm.state.collectAsState()

    var mapMode by remember { mutableStateOf(MapMode.Hidden) }
    var buses by remember { mutableStateOf<List<BusMapUi>>(emptyList()) }
    var selectedBus by remember { mutableStateOf<BusMapUi?>(null) }

    val mapVm: RouteMapViewModel = org.koin.compose.koinInject()
    val mapState by mapVm.state.collectAsState()

    val lat = "-6.3932222"
    val long = "106.8240833"

    LaunchedEffect(corridorCode) {
        vm.onAction(DetailRuteAction.Load(corridorCode))
    }

    fun openInlineMap() {
        onOpenMap()
        selectedBus = null
        mapMode = MapMode.Inline
    }

    fun closeMap() {
        selectedBus = null
        mapMode = MapMode.Hidden
    }

    fun toggleFull() {
        mapMode = if (mapMode == MapMode.Full) MapMode.Inline else MapMode.Full
    }

    Scaffold(
        containerColor = cs.background,
        topBar = { DetailRuteTopBar(onBack = onBack) }
    ) { padding ->
        when {
            state.isLoading -> LoadingState(padding)

            state.error != null -> ErrorState(
                padding = padding,
                message = state.error!!,
                onRetry = { vm.onAction(DetailRuteAction.Refresh) }
            )

            else -> {
                val d: RouteDetailResponse = state.data ?: return@Scaffold

                val routeCode = d.routeCode?.trim()

                LaunchedEffect(routeCode, mapMode) {
                    if (mapMode == MapMode.Hidden) return@LaunchedEffect
                    if (routeCode.isNullOrBlank()) return@LaunchedEffect
                    mapVm.loadRoute(routeCode)
                }

                LaunchedEffect(d.routeCode) {
                    vm.loadTrip(d.routeCode ?: corridorCode, lat, long)
                }

                if (mapMode == MapMode.Full) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        RouteMapBox(
                            mapLoading = mapState.isLoading,
                            mapError = mapState.error,
                            polyline = mapState.polyline,
                            stops = mapState.stops,

                            buses = emptyList(),
                            selectedBus = null,
                            onBusClick = {},
                            onDismissBus = {},

                            onCloseMap = { closeMap() },
                            onToggleFull = { toggleFull() },
                            onBusDetail = {},
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    return@Scaffold
                }

                DetailRuteContent(
                    padding = padding,
                    corridorCodeParam = corridorCode,
                    detail = d,
                    trip = trip,
                    onSwap = { vm.swapToNextRoute(lat, long) },
                    mapMode = mapMode,

                    mapLoading = mapState.isLoading,
                    mapError = mapState.error,
                    polyline = mapState.polyline,
                    mapStops = mapState.stops,

                    buses = emptyList(),
                    selectedBus = null,
                    onSelectBus = {},
                    onDismissBus = {},

                    onOpenInlineMap = { openInlineMap() },
                    onCloseMap = { closeMap() },
                    onToggleFull = { toggleFull() },
                    onBusDetail = onBusDetail,
                    onHalteClick = onHalteClick
                )
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailRuteTopBar(onBack: () -> Unit) {
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
                    "Detail Rute",
                    color = cs.onSecondary,
                    fontWeight = FontWeight.SemiBold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Kembali",
                        tint = cs.onSecondary
                    )
                }
            },
            actions = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.StarBorder,
                        contentDescription = "Favorit",
                        tint = cs.onSecondary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = cs.primary),
            windowInsets = WindowInsets(0)
        )
    }
}

@Composable
private fun LoadingState(padding: PaddingValues) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(
    padding: PaddingValues,
    message: String,
    onRetry: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    Box(
        Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(message, color = cs.error)
            Spacer(Modifier.height(12.dp))
            Button(onClick = onRetry) { Text("Coba lagi") }
        }
    }
}


@Composable
private fun DetailRuteContent(
    padding: PaddingValues,
    detail: RouteDetailResponse,
    corridorCodeParam: String,

    trip: TripResponse?,
    onSwap: () -> Unit,

    mapMode: MapMode,
    mapLoading: Boolean,
    mapError: String?,
    polyline: List<GeoPoint>,
    mapStops: List<StopMapUi>,
    buses: List<BusMapUi>,
    selectedBus: BusMapUi?,
    onOpenInlineMap: () -> Unit,
    onCloseMap: () -> Unit,
    onToggleFull: () -> Unit,
    onSelectBus: (String) -> Unit,
    onDismissBus: () -> Unit,
    onBusDetail: (String) -> Unit,
    onHalteClick: (halteId: String) -> Unit
) {

    val swapEnabled = !trip?.nextRoute.isNullOrBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .imePadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        RouteHeader(
            corridorCodeParam = corridorCodeParam,
            d = detail
        )

        TwoInfoCards(
            leftTitle = "Jam Operasional",
            leftValue = detail.operationalHour,
            rightTitle = "Tarif",
            rightValue = detail.fare
        )

        FromToCard(
            from = trip?.halteStart ?: "-",
            to = trip?.halteEnd ?: "-",
            swapEnabled = swapEnabled,
            onSwap = onSwap
        )


        SummaryRow(
            stopCount = trip?.totalHalte ?: 0,
            activeBusCount = trip?.busActive ?: 0
        )

        if (mapMode == MapMode.Inline) {
            RouteMapBox(
                mapLoading = mapLoading,
                mapError = mapError,
                polyline = polyline,
                stops = mapStops,
                buses = buses,
                selectedBus = selectedBus,
                onBusClick = onSelectBus,
                onDismissBus = onDismissBus,
                onCloseMap = onCloseMap,
                onToggleFull = onToggleFull,
                onBusDetail = onBusDetail,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        } else {
            StopsSectionHeader(onOpenInlineMap = onOpenInlineMap)

            val halteList = remember(trip?.listHalte) {
                trip?.listHalte?.mapIndexed { index, halte ->

                    val badgeNumbers: List<Int> = halte.otherCorridors
                        .mapNotNull { it.trim().toIntOrNull() }
                        .distinct()
                        .sorted()

                    val nearestInfo: String? =
                        if (halte.isNearest == true) {
                            val d = halte.distance?.takeIf { it.isNotBlank() }
                            if (d != null) "Halte terdekat ($d)" else "Halte terdekat"
                        } else null

                    StopInfo(
                        id = halte.cHalte ?: (halte.iSeq?.toString() ?: index.toString()),
                        name = halte.nHalte ?: "Halte ${halte.iSeq ?: (index + 1)}",
                        badges = badgeNumbers,
                        nearestInfo = nearestInfo
                    )
                } ?: emptyList()
            }



            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item {
                    if (halteList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Tidak ada halte pada rute ini")
                        }
                    } else {
                        StopsList(
                            stops = halteList,
                            onStopClick = { stop -> onHalteClick(stop.id) }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun RouteHeader(
    corridorCodeParam: String,
    d: RouteDetailResponse
) {
    val cs = MaterialTheme.colorScheme

    val status = d.statusOperational.toRouteStatus()
    val statusColor = when (status) {
        RouteStatus.ACTIVE -> Color(0xFF2E7D32)
        RouteStatus.ALMOST_DONE -> Color(0xFFF9A825)
        RouteStatus.ENDED -> Color(0xFFC62828)
    }

    val corridorText = corridorCodeParam.trim().ifEmpty { "-" }
    val corridorBadge = corridorText.toIntOrNull()?.coerceAtLeast(0) ?: 0

    Surface(shape = RoundedCornerShape(16.dp), color = cs.surface) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorForBadge(corridorBadge)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = corridorText,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = d.routeName ?: "-",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(statusColor)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = status.label,
                        color = cs.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}


@Composable
private fun StopsSectionHeader(onOpenInlineMap: () -> Unit) {
    val cs = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Halte yang dilewati",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.weight(1f))

        Button(
            onClick = onOpenInlineMap,
            shape = RoundedCornerShape(999.dp),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = cs.primary,
                contentColor = cs.onPrimary
            )
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_map),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Lihat Peta",
                fontWeight = FontWeight.SemiBold,
                color = cs.onSecondary
            )
        }
    }
}

@Composable
private fun RouteMapBox(
    mapLoading: Boolean,
    mapError: String?,
    polyline: List<GeoPoint>,
    stops: List<StopMapUi>,
    buses: List<BusMapUi>,
    selectedBus: BusMapUi?,
    onBusClick: (String) -> Unit,
    onDismissBus: () -> Unit,
    onCloseMap: () -> Unit,
    onToggleFull: () -> Unit,
    onBusDetail: (busId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(20.dp)
    val density = LocalDensity.current

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var busAnchorPx by remember { mutableStateOf<Offset?>(null) }
    var bubbleSize by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(selectedBus?.id) {
        bubbleSize = IntSize.Zero
        busAnchorPx = null
    }

    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = cs.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, cs.outlineVariant.copy(alpha = 0.55f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { containerSize = it }
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(shape)
            ) {
                Crossfade(
                    targetState = when {
                        mapLoading -> MapRenderState.Loading
                        mapError != null -> MapRenderState.Error(mapError)
                        else -> MapRenderState.Map
                    },
                    label = "map_state"
                ) { state ->
                    when (state) {
                        MapRenderState.Loading -> Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) { CircularProgressIndicator(strokeWidth = 2.5.dp) }

                        is MapRenderState.Error -> Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) { Text(state.message, color = cs.error) }

                        MapRenderState.Map -> {
                            PlatformRouteMap(
                                polyline = polyline,
                                stops = stops,
                                buses = buses,
                                selectedBusId = selectedBus?.id,
                                onBusClick = { busId -> onBusClick(busId) },
                                onMapClick = {
                                    busAnchorPx = null
                                    onDismissBus()
                                },
                                onBusAnchorChanged = { anchor -> busAnchorPx = anchor },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }

            MapTopControls(
                onToggleFull = onToggleFull,
                onCloseMap = {
                    busAnchorPx = null
                    onDismissBus()
                    onCloseMap()
                },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(12.dp)
                    .fillMaxWidth()
            )

            if (selectedBus != null && busAnchorPx != null && containerSize.width > 0 && containerSize.height > 0) {
                val anchor = busAnchorPx!!
                val needMeasure = bubbleSize.width == 0 || bubbleSize.height == 0

                if (needMeasure) {
                    BusInfoBubbleCompact(
                        bus = selectedBus,
                        onDetail = { onBusDetail(selectedBus.id) },
                        pointerCenterXPx = 120f,
                        pointerDirection = PointerDirection.Down,
                        modifier = Modifier
                            .alpha(0f)
                            .offset { IntOffset(-10000, -10000) }
                            .onSizeChanged { bubbleSize = it }
                    )
                } else {
                    val bw = bubbleSize.width
                    val bh = bubbleSize.height

                    val marginPx = with(density) { 8.dp.roundToPx() }
                    val pointerSafePx = with(density) { 18.dp.toPx() }

                    val desiredX = (anchor.x - bw / 2f).roundToInt()
                    val minX = marginPx
                    val maxX = (containerSize.width - bw - marginPx).coerceAtLeast(marginPx)
                    val clampedX = desiredX.coerceIn(minX, maxX)

                    val pointerCenterX = (anchor.x - clampedX.toFloat()).coerceIn(
                        pointerSafePx,
                        (bw.toFloat() - pointerSafePx).coerceAtLeast(pointerSafePx)
                    )

                    val yAboveTop = (anchor.y - bh).roundToInt()
                    val yBelowTop = anchor.y.roundToInt()

                    val canShowAbove = yAboveTop >= marginPx
                    val canShowBelow = (yBelowTop + bh) <= (containerSize.height - marginPx)

                    val direction = when {
                        canShowAbove -> PointerDirection.Down
                        canShowBelow -> PointerDirection.Up
                        else -> PointerDirection.Down
                    }

                    val finalY = if (direction == PointerDirection.Down) yAboveTop else yBelowTop

                    BusInfoBubbleCompact(
                        bus = selectedBus,
                        onDetail = { onBusDetail(selectedBus.id) },
                        pointerCenterXPx = pointerCenterX,
                        pointerDirection = direction,
                        modifier = Modifier
                            .zIndex(10f)
                            .offset { IntOffset(clampedX, finalY) }
                    )
                }
            }
        }
    }
}

private sealed class MapRenderState {
    data object Loading : MapRenderState()
    data class Error(val message: String) : MapRenderState()
    data object Map : MapRenderState()
}

@Composable
private fun MapTopControls(
    onToggleFull: () -> Unit,
    onCloseMap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MapFloatingSurface(
            shape = CircleShape,
            modifier = Modifier.size(40.dp),
            onClick = onToggleFull
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_expand),
                contentDescription = "Toggle Fullscreen Map",
                modifier = Modifier.size(18.dp),
                colorFilter = ColorFilter.tint(cs.onSurface)
            )
        }

        MapFloatingSurface(
            shape = RoundedCornerShape(999.dp),
            onClick = onCloseMap
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_close),
                    contentDescription = "Tutup",
                    modifier = Modifier.size(18.dp),
                    colorFilter = ColorFilter.tint(cs.onSurface)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Tutup Peta",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = cs.onSurface
                )
            }
        }
    }
}

@Composable
private fun MapFloatingSurface(
    shape: Shape,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Surface(
        modifier = modifier,
        shape = shape,
        color = cs.surface.copy(alpha = 0.92f),
        contentColor = cs.onSurface,
        tonalElevation = 0.dp,
        shadowElevation = 10.dp,
        border = BorderStroke(1.dp, cs.outlineVariant.copy(alpha = 0.55f)),
        onClick = onClick
    ) {
        Box(contentAlignment = Alignment.Center, content = content)
    }
}

@Composable
private fun BusInfoBubbleCompact(
    bus: BusMapUi,
    onDetail: () -> Unit,
    pointerCenterXPx: Float,
    pointerDirection: PointerDirection,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val accent = Color(0xFFE0912D)

    val cardShape = RoundedCornerShape(18.dp)
    val pointerW = 22.dp
    val pointerH = 12.dp
    val borderColor = cs.outlineVariant.copy(alpha = 0.55f)

    Column(
        modifier = modifier.widthIn(max = 280.dp),
        horizontalAlignment = Alignment.Start
    ) {
        if (pointerDirection == PointerDirection.Up) {
            BubblePointer(
                pointerCenterXPx = pointerCenterXPx,
                pointerW = pointerW,
                pointerH = pointerH,
                direction = PointerDirection.Up,
                fillColor = cs.surface,
                borderColor = borderColor,
                borderWidth = 1.dp,
                overlap = 2.dp
            )
        }

        Surface(
            shape = cardShape,
            color = cs.surface,
            shadowElevation = 10.dp,
            border = BorderStroke(1.dp, borderColor)
        ) {
            Column(Modifier.padding(14.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(accent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = bus.id.take(1),
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(accent)
                            .padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DirectionsBus,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = bus.code,
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Menuju Halte",
                    color = cs.onSurfaceVariant,
                    style = MaterialTheme.typography.labelMedium
                )

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = bus.destination,
                        modifier = Modifier.weight(1f),
                        color = cs.onSurface,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = bus.eta,
                        color = cs.onSurfaceVariant,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = onDetail,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accent,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "Detail",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }

        if (pointerDirection == PointerDirection.Down) {
            BubblePointer(
                pointerCenterXPx = pointerCenterXPx,
                pointerW = pointerW,
                pointerH = pointerH,
                direction = PointerDirection.Down,
                fillColor = cs.surface,
                borderColor = borderColor,
                borderWidth = 1.dp,
                overlap = 2.dp
            )
        }
    }
}

@Composable
private fun BubblePointer(
    pointerCenterXPx: Float,
    pointerW: Dp,
    pointerH: Dp,
    direction: PointerDirection,
    fillColor: Color,
    borderColor: Color,
    borderWidth: Dp,
    overlap: Dp = 0.dp
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(pointerH)
            .offset(y = if (direction == PointerDirection.Down) (-overlap) else overlap)
    ) {
        val density = LocalDensity.current
        val boxWpx = with(density) { maxWidth.toPx() }
        val pointerWpx = with(density) { pointerW.toPx() }
        val strokePx = with(density) { borderWidth.toPx() }

        val leftPx = (pointerCenterXPx - pointerWpx / 2f)
            .coerceIn(0f, (boxWpx - pointerWpx).coerceAtLeast(0f))

        Canvas(
            modifier = Modifier
                .size(pointerW, pointerH)
                .offset { IntOffset(leftPx.toInt(), 0) }
        ) {
            val w = size.width
            val h = size.height

            val path = Path().apply {
                if (direction == PointerDirection.Down) {
                    moveTo(0f, 0f)
                    lineTo(w, 0f)
                    quadraticTo(w * 0.72f, h * 0.15f, w * 0.5f, h)
                    quadraticTo(w * 0.28f, h * 0.15f, 0f, 0f)
                } else {
                    moveTo(0f, h)
                    lineTo(w, h)
                    quadraticTo(w * 0.72f, h * 0.85f, w * 0.5f, 0f)
                    quadraticTo(w * 0.28f, h * 0.85f, 0f, h)
                }
                close()
            }

            withTransform({ translate(left = 0f, top = 1.5f) }) {
                drawPath(path, Color.Black.copy(alpha = 0.10f))
            }

            drawPath(path, fillColor)
            drawPath(path, borderColor, style = Stroke(width = strokePx))
        }
    }
}

@Composable
private fun TwoInfoCards(
    leftTitle: String,
    leftValue: String?,
    rightTitle: String,
    rightValue: String?
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        InfoMiniCard(title = leftTitle, value = leftValue, modifier = Modifier.weight(1f))
        InfoMiniCard(title = rightTitle, value = rightValue, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun FromToCard(
    from: String,
    to: String,
    swapEnabled: Boolean,
    onSwap: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Surface(shape = RoundedCornerShape(16.dp), color = cs.surface) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.ic_start_halte),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(from, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                }

                Divider(color = cs.outlineVariant)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.ic_end_halte),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(to, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                }
            }

            Spacer(Modifier.width(12.dp))

            IconButton(
                onClick = onSwap,
                enabled = swapEnabled,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(cs.primary.copy(alpha = if (swapEnabled) 1f else 0.45f))
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_swap),
                    contentDescription = "Tukar",
                    tint = cs.onSecondary.copy(alpha = if (swapEnabled) 1f else 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}



@Composable
private fun SummaryRow(stopCount: Int, activeBusCount: Int) {
    val cs = MaterialTheme.colorScheme

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SummaryItem(
            iconRes = Res.drawable.ic_info_detail,
            text = "Melewati $stopCount Halte"
        )
        SummaryItem(
            iconRes = Res.drawable.ic_info_detail,
            text = "$activeBusCount Bus sedang beroperasi"
        )
    }
}

@Composable
private fun SummaryItem(iconRes: org.jetbrains.compose.resources.DrawableResource, text: String) {
    val cs = MaterialTheme.colorScheme

    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(painterResource(iconRes), contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(10.dp))
        Text(text, color = cs.onSurfaceVariant, fontSize = 12.sp)
    }
}

@Composable
private fun InfoMiniCard(
    title: String,
    value: String?,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = cs.primary.copy(alpha = 0.12f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                title,
                color = cs.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = value ?: "-",
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun StopsList(
    stops: List<StopInfo>,
    onStopClick: (StopInfo) -> Unit
) {
    val cs = MaterialTheme.colorScheme

    val lineColor = Color(0xFFE9D9A7)
    val lineWidth = 8.dp

    val rowPaddingStart = 16.dp
    val timelineBoxWidth = 32.dp
    val lineX = rowPaddingStart + (timelineBoxWidth - lineWidth) / 2

    val capPad = 26.dp

    Surface(shape = RoundedCornerShape(16.dp), color = cs.surface) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    val xPx = lineX.toPx()
                    val wPx = lineWidth.toPx()
                    val top = capPad.toPx()
                    val bottom = capPad.toPx()
                    val h = size.height - top - bottom
                    if (h > 0f) {
                        drawRoundRect(
                            color = lineColor,
                            topLeft = Offset(xPx, top),
                            size = Size(wPx, h),
                            cornerRadius = CornerRadius(wPx / 2, wPx / 2)
                        )
                    }
                }
        ) {
            Column(Modifier.fillMaxWidth()) {
                stops.forEachIndexed { index, stop ->
                    StopRowFlat(stop = stop, onClick = { onStopClick(stop) })
                    if (index != stops.lastIndex) {
                        Divider(
                            color = cs.outlineVariant,
                            thickness = 1.dp,
                            modifier = Modifier.padding(start = 64.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StopRowFlat(stop: StopInfo, onClick: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    val ringColor = Color(0xFFE0A52D)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(32.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(22.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.White)
                    .border(2.dp, ringColor, RoundedCornerShape(999.dp)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(ringColor)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Column(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                stop.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            if (stop.badges.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    stop.badges.take(3).forEach { BadgeChip(it) }
                }
            }

            stop.nearestInfo?.let { nearest ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = Color(0xFF1E88E5),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        nearest,
                        color = Color(0xFF1E88E5),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = cs.onSurfaceVariant,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun BadgeChip(number: Int) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(colorForBadge(number))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            color = Color.White,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun colorForBadge(n: Int): Color {
    return when (n) {
        1 -> Color(0xFF4F8AA5)
        2 -> Color(0xFF0F2F6B)
        5 -> Color(0xFFE0912D)
        6 -> Color(0xFFB94A31)
        else -> Color(0xFF6D6D6D)
    }
}
