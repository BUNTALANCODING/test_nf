package presentation.ui.main.halte

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import common.map.PlatformRouteMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import navbuss.shared.generated.resources.Res
import navbuss.shared.generated.resources.ic_map_ar
import navbuss.shared.generated.resources.ic_map_rute
import org.jetbrains.compose.resources.painterResource
import presentation.ui.main.inforute.view_model.BusMapUi
import presentation.ui.main.inforute.view_model.FakeRouteMapRepository
import presentation.ui.main.inforute.view_model.GeoPoint
import presentation.ui.main.inforute.view_model.RouteMapRepository
import presentation.ui.main.inforute.view_model.StopMapUi
import kotlin.math.roundToInt

data class BusArrivalUi(
    val routeNo: Int,
    val routeCode: String,
    val destination: String,
    val etaMinutes: Int,
    val clock: String
)

data class HalteDetailUi(
    val halteName: String,
    val distanceMeters: Int,
    val availableRoutes: List<Int>,
    val arrivals: List<BusArrivalUi>
)

interface HalteDetailRepository {
    suspend fun fetchHalteDetail(halteId: String): HalteDetailUi
}

class FakeHalteDetailRepository : HalteDetailRepository {
    override suspend fun fetchHalteDetail(halteId: String): HalteDetailUi {
        return HalteDetailUi(
            halteName = "Tugu Narkoba 2",
            distanceMeters = 150,
            availableRoutes = listOf(5, 1),
            arrivals = listOf(
                BusArrivalUi(5, "TP 030", "Terminal Ciparigi", 1, "14:52"),
                BusArrivalUi(5, "TP 045", "Stasiun KA Bogor", 5, "15:00"),
                BusArrivalUi(1, "TP 038", "Cidangiang", 0, "14:52"),
                BusArrivalUi(1, "TP 024", "Terminal Bubulak", 3, "14:55"),
            )
        )
    }
}

private enum class MapMode { Hidden, Map }
private enum class PointerDirection { Down, Up }

private sealed class MapRenderState {
    data object Loading : MapRenderState()
    data class Error(val message: String) : MapRenderState()
    data object Map : MapRenderState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailHalteScreen(
    halteId: String,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit = {},
    onOpenMap: () -> Unit,
    onOpenAr: () -> Unit,
    onRoute: () -> Unit,
    onBusDetail: (String) -> Unit = {},
    repo: HalteDetailRepository = FakeHalteDetailRepository(),
    mapRepo: RouteMapRepository = FakeRouteMapRepository()
) {
    val cs = MaterialTheme.colorScheme
    val scope = rememberCoroutineScope()

    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var data by remember { mutableStateOf<HalteDetailUi?>(null) }

    fun load() {
        scope.launch {
            loading = true
            error = null
            runCatching { repo.fetchHalteDetail(halteId) }
                .onSuccess { data = it; loading = false }
                .onFailure { error = it.message ?: "Terjadi kesalahan"; loading = false }
        }
    }

    LaunchedEffect(halteId) { load() }

    var mapMode by remember { mutableStateOf(MapMode.Hidden) }
    var mapLoading by remember { mutableStateOf(false) }
    var mapError by remember { mutableStateOf<String?>(null) }

    var polyline by remember { mutableStateOf<List<GeoPoint>>(emptyList()) }
    var mapStops by remember { mutableStateOf<List<StopMapUi>>(emptyList()) }
    var buses by remember { mutableStateOf<List<BusMapUi>>(emptyList()) }
    var selectedBus by remember { mutableStateOf<BusMapUi?>(null) }

    fun openMap() {
        onOpenMap()
        selectedBus = null
        mapMode = MapMode.Map
    }

    fun closeMap() {
        selectedBus = null
        mapMode = MapMode.Hidden
    }

    Scaffold(
        containerColor = cs.background,
        topBar = {
            DetailHalteTopBar(
                title = "Detail Halte",
                onBack = onBack,
                onFavorite = onToggleFavorite
            )
        }
    ) { padding ->

        when {
            loading -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            error != null -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(error!!, color = cs.error)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { load() }) { Text("Coba lagi") }
                }
            }

            else -> {
                val d = data ?: return@Scaffold

                var selectedRoute by remember { mutableStateOf<Int?>(null) }

                val filtered = remember(d.arrivals, selectedRoute) {
                    if (selectedRoute == null) d.arrivals
                    else d.arrivals.filter { it.routeNo == selectedRoute }
                }

                val routeKeyForMap = remember(selectedRoute, d.availableRoutes, halteId) {
                    (selectedRoute ?: d.availableRoutes.firstOrNull())?.toString() ?: halteId
                }

                LaunchedEffect(routeKeyForMap, mapMode) {
                    if (mapMode == MapMode.Hidden) return@LaunchedEffect
                    mapLoading = true
                    mapError = null
                    runCatching { mapRepo.fetchRoutePolyline(routeKeyForMap) }
                        .onSuccess { polyline = it }
                        .onFailure { mapError = it.message ?: "Gagal ambil rute" }

                    runCatching { mapRepo.fetchStops(routeKeyForMap) }
                        .onSuccess { mapStops = it }
                        .onFailure { mapError = it.message ?: "Gagal ambil halte" }

                    mapLoading = false
                }

                LaunchedEffect(routeKeyForMap, mapMode) {
                    if (mapMode == MapMode.Hidden) return@LaunchedEffect
                    while (isActive) {
                        runCatching { mapRepo.fetchBuses(routeKeyForMap) }
                            .onSuccess { buses = it }
                        delay(2000)
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(cs.background)
                        .navigationBarsPadding()
                        .imePadding()
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        HalteHeader(
                            halteName = d.halteName,
                            distanceMeters = d.distanceMeters,
                            showMapButton = (mapMode == MapMode.Hidden),
                            onOpenMap = { openMap() }
                        )

                        if (mapMode == MapMode.Hidden) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                HalteActionButton(
                                    text = "Lihat dengan AR",
                                    icon = painterResource(Res.drawable.ic_map_ar),
                                    onClick = onOpenAr,
                                    modifier = Modifier.weight(1f)
                                )
                                HalteActionButton(
                                    text = "Arahkan Rute",
                                    icon = painterResource(Res.drawable.ic_map_rute),
                                    onClick = onRoute,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    if (mapMode == MapMode.Map) {
                        RouteMapBox(
                            mapLoading = mapLoading,
                            mapError = mapError,
                            polyline = polyline,
                            stops = mapStops,
                            buses = buses,
                            selectedBus = selectedBus,
                            onBusClick = { busId -> selectedBus = buses.firstOrNull { it.id == busId } },
                            onDismissBus = { selectedBus = null },
                            onCloseMap = { closeMap() },
                            onToggleFull = { },
                            onBusDetail = onBusDetail,
                            showExpand = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    } else {
                        val sectionBg = cs.surfaceVariant.copy(alpha = 0.35f)

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(sectionBg)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                InfoKedatanganHeader()

                                RouteFilterRowMock(
                                    routes = d.availableRoutes,
                                    selectedRoute = selectedRoute,
                                    onSelectAll = { selectedRoute = null },
                                    onSelectRoute = { selectedRoute = it }
                                )

                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(bottom = 18.dp)
                                ) {
                                    items(filtered) { bus ->
                                        BusArrivalCard(bus = bus)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailHalteTopBar(
    title: String,
    onBack: () -> Unit,
    onFavorite: () -> Unit
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
                    title,
                    color = cs.onSecondary,
                    fontWeight = FontWeight.SemiBold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = cs.onSecondary
                    )
                }
            },
            actions = {
                IconButton(onClick = onFavorite) {
                    Icon(
                        imageVector = Icons.Filled.StarBorder,
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
private fun HalteHeader(
    halteName: String,
    distanceMeters: Int,
    showMapButton: Boolean,
    onOpenMap: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = halteName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Directions,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = cs.onSurfaceVariant
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "${distanceMeters} m",
                    color = cs.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        if (showMapButton) {
            Button(
                onClick = onOpenMap,
                shape = RoundedCornerShape(999.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = cs.primary,
                    contentColor = cs.onSecondary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Map,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = cs.onSecondary
                )
                Spacer(Modifier.width(8.dp))
                Text("Lihat Peta", fontWeight = FontWeight.SemiBold, color = cs.onSecondary)
            }
        }
    }
}

@Composable
private fun HalteActionButton(
    text: String,
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    containerColor: Color = borderColor.copy(alpha = 0.10f),
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(72.dp),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = borderColor
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = borderColor
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun InfoKedatanganHeader() {
    val cs = MaterialTheme.colorScheme
    Text(
        text = "Info Kedatangan Bus",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = cs.onSurface
    )
}

@Composable
private fun RouteFilterRowMock(
    routes: List<Int>,
    selectedRoute: Int?,
    onSelectAll: () -> Unit,
    onSelectRoute: (Int) -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val allBg = cs.primary
    val chipH = 38.dp
    val shape = RoundedCornerShape(10.dp)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val allSelected = selectedRoute == null
        Surface(
            onClick = onSelectAll,
            shape = shape,
            color = if (allSelected) allBg else allBg.copy(alpha = 0.55f),
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {
            Box(
                modifier = Modifier.height(chipH).padding(horizontal = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Semua",
                    fontWeight = FontWeight.SemiBold,
                    color = if (allSelected) Color.White else Color.White.copy(alpha = 0.75f)
                )
            }
        }

        routes.take(3).forEach { r ->
            val selected = selectedRoute == r
            val activeColor = routeColorFor(r)
            val inactiveColor = activeColor.copy(alpha = 0.35f)

            Surface(
                onClick = { onSelectRoute(r) },
                shape = shape,
                color = if (selected) activeColor else inactiveColor,
                tonalElevation = 0.dp,
                shadowElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier.size(chipH),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = r.toString(),
                        fontWeight = FontWeight.Bold,
                        color = if (selected) Color.White else Color.White.copy(alpha = 0.75f)
                    )
                }
            }
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
    showExpand: Boolean,
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
                    label = "halte_map_state"
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
                showExpand = showExpand,
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

@Composable
private fun MapTopControls(
    showExpand: Boolean,
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
        if (showExpand) {
            MapFloatingSurface(
                shape = CircleShape,
                modifier = Modifier.size(40.dp),
                onClick = onToggleFull
            ) {
                Text("⤢", fontWeight = FontWeight.Bold, color = cs.onSurface)
            }
        } else {
            Spacer(Modifier.size(40.dp))
        }

        MapFloatingSurface(
            shape = RoundedCornerShape(999.dp),
            onClick = onCloseMap
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Tutup",
                    modifier = Modifier.size(18.dp),
                    tint = cs.onSurface
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
private fun BusArrivalCard(bus: BusArrivalUi) {
    val cs = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(16.dp)
    val routeColor = routeColorFor(bus.routeNo)

    val etaText = when {
        bus.etaMinutes <= 0 -> "Sekarang"
        bus.etaMinutes == 1 -> "<1 menit"
        else -> "${bus.etaMinutes} menit"
    }

    val etaBg = if (bus.etaMinutes <= 0) Color(0xFF2E7D32) else Color(0xFFF3E2AE)
    val etaFg = if (bus.etaMinutes <= 0) Color.White else Color.Black

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = shape,
        color = cs.surface,
        border = BorderStroke(1.dp, cs.outlineVariant.copy(alpha = 0.65f)),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(routeColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = bus.routeNo.toString(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(routeColor)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
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
                            text = bus.routeCode,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Tujuan akhir",
                    style = MaterialTheme.typography.labelSmall,
                    color = cs.onSurfaceVariant
                )

                Text(
                    text = bus.destination,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .widthIn(min = 140.dp)
                    .background(etaBg, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Estimasi Tiba",
                    style = MaterialTheme.typography.labelMedium,
                    color = etaFg.copy(alpha = 0.9f)
                )

                Spacer(Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = etaText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = etaFg,
                        fontSize = 12.sp
                    )
                    if (bus.etaMinutes > 0) {
                        Text(
                            text = "(${bus.clock})",
                            style = MaterialTheme.typography.labelMedium,
                            color = etaFg.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

private fun routeColorFor(n: Int): Color {
    return when (n) {
        1 -> Color(0xFF4F8AA5)
        2 -> Color(0xFF0F2F6B)
        5 -> Color(0xFFE0912D)
        6 -> Color(0xFFB94A31)
        else -> Color(0xFF6D6D6D)
    }
}
