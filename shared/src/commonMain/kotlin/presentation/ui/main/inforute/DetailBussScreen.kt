package presentation.ui.main.busdetail

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.map.PlatformRouteMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import navbuss.shared.generated.resources.Res
import navbuss.shared.generated.resources.ic_close
import navbuss.shared.generated.resources.ic_detail_buss
import navbuss.shared.generated.resources.ic_expand
import navbuss.shared.generated.resources.ic_map
import org.jetbrains.compose.resources.painterResource
import presentation.ui.main.inforute.view_model.BusMapUi
import presentation.ui.main.inforute.view_model.FakeRouteMapRepository
import presentation.ui.main.inforute.view_model.GeoPoint
import presentation.ui.main.inforute.view_model.RouteMapRepository
import presentation.ui.main.inforute.view_model.StopMapUi

private enum class MapMode { Hidden, Inline, Full }

private sealed class MapRenderState {
    data object Loading : MapRenderState()
    data class Error(val message: String) : MapRenderState()
    data object Map : MapRenderState()
}

data class BusArrivalStopUi(
    val stopName: String,
    val etaMinutes: Int,
    val clock: String
)

data class BusDetailUi(
    val routeNo: String,
    val routeCode: String,
    val routeColor: Color,
    val routeTitle: String,
    val finalDestination: String,
    val stops: List<BusArrivalStopUi>
)

interface BusDetailRepository {
    suspend fun fetchBusDetail(busId: String): BusDetailUi
}

class FakeBusDetailRepository : BusDetailRepository {
    override suspend fun fetchBusDetail(busId: String): BusDetailUi {
        return BusDetailUi(
            routeNo = "1",
            routeCode = "TP 024",
            routeColor = Color(0xFF4F8AA5),
            routeTitle = "Terminal Bubulak - Cidangiang",
            finalDestination = "Terminal Bubulak",
            stops = listOf(
                BusArrivalStopUi("Tugu Narkoba 2", 0, "14:52"),
                BusArrivalStopUi("Resna Subakti", 5, "14:57"),
                BusArrivalStopUi("Taman Ekspresi", 14, "15:11"),
                BusArrivalStopUi("Bantar Jati", 20, "15:31"),
                BusArrivalStopUi("Bantar Jati", 20, "15:31"),
                BusArrivalStopUi("Bantar Jati", 20, "15:31"),
                BusArrivalStopUi("Bantar Jati", 20, "15:31"),
                BusArrivalStopUi("Bantar Jati", 20, "15:31"),
                BusArrivalStopUi("Bantar Jati", 20, "15:31"),
                BusArrivalStopUi("Bantar Jati", 20, "15:31"),
                BusArrivalStopUi("Bantar Jati", 20, "15:31"),
                BusArrivalStopUi("Bantar Jati", 20, "15:31"),
                BusArrivalStopUi("Bantar Jati", 20, "15:31"),
                BusArrivalStopUi("Bantar Jati", 20, "15:31"),
                BusArrivalStopUi("Bantar Jati", 20, "15:31"),
                BusArrivalStopUi("Bantar Jati", 20, "15:31"),
                BusArrivalStopUi("Bantar Jati", 20, "15:31"),
                BusArrivalStopUi("Bantar Jati", 20, "15:31"),
                BusArrivalStopUi("Bantar Jati", 20, "15:31"),
                BusArrivalStopUi("Bantar Jati", 20, "15:31"),
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBussScreen(
    busId: String,
    onBack: () -> Unit,
    onOpenMap: () -> Unit,
    repo: BusDetailRepository = FakeBusDetailRepository(),
    mapRepo: RouteMapRepository = FakeRouteMapRepository(),
) {
    val scope = rememberCoroutineScope()

    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var data by remember { mutableStateOf<BusDetailUi?>(null) }

    fun load() {
        scope.launch {
            loading = true
            error = null
            runCatching { repo.fetchBusDetail(busId) }
                .onSuccess {
                    data = it
                    loading = false
                }
                .onFailure {
                    error = it.message ?: "Terjadi kesalahan"
                    loading = false
                }
        }
    }

    LaunchedEffect(busId) { load() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { DetailBusTopBar(onBack = onBack) }
    ) { padding ->
        when {
            loading -> LoadingState(padding)
            error != null -> ErrorState(padding, error!!, onRetry = { load() })
            else -> {
                val d = data ?: return@Scaffold
                DetailBussContent(
                    busId = busId,
                    routeNo = d.routeNo,
                    routeCode = d.routeCode,
                    routeColor = d.routeColor,
                    routeTitle = d.routeTitle,
                    finalDestination = d.finalDestination,
                    stops = d.stops,
                    onOpenMap = onOpenMap,
                    mapRepo = mapRepo,
                    onBack = onBack
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailBusTopBar(
    onBack: () -> Unit,
    onFavorite: () -> Unit = {}
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
                    "Detail Bus",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailBussContent(
    busId: String,
    routeNo: String,
    routeCode: String,
    routeColor: Color,
    routeTitle: String,
    finalDestination: String,
    stops: List<BusArrivalStopUi>,
    onBack: () -> Unit,
    onOpenMap: () -> Unit,
    mapRepo: RouteMapRepository
) {
    val cs = MaterialTheme.colorScheme

    var mapMode by remember { mutableStateOf(MapMode.Hidden) }
    var mapLoading by remember { mutableStateOf(false) }
    var mapError by remember { mutableStateOf<String?>(null) }

    var polyline by remember { mutableStateOf<List<GeoPoint>>(emptyList()) }
    var mapStops by remember { mutableStateOf<List<StopMapUi>>(emptyList()) }
    var buses by remember { mutableStateOf<List<BusMapUi>>(emptyList()) }

    val routeKey = routeNo

    fun openInlineMap() {
        onOpenMap()
        mapMode = MapMode.Inline
    }

    fun closeMap() {
        mapMode = MapMode.Hidden
    }

    fun toggleFull() {
        mapMode = if (mapMode == MapMode.Full) MapMode.Inline else MapMode.Full
    }

    LaunchedEffect(routeKey, mapMode) {
        if (mapMode == MapMode.Hidden) return@LaunchedEffect

        mapLoading = true
        mapError = null

        runCatching { mapRepo.fetchRoutePolyline(routeKey) }
            .onSuccess { polyline = it }
            .onFailure { mapError = it.message ?: "Gagal ambil rute" }

        runCatching { mapRepo.fetchStops(routeKey) }
            .onSuccess { mapStops = it }
            .onFailure { mapError = it.message ?: "Gagal ambil halte" }

        mapLoading = false
    }

    LaunchedEffect(routeKey, mapMode) {
        if (mapMode == MapMode.Hidden) return@LaunchedEffect
        while (isActive) {
            runCatching { mapRepo.fetchBuses(routeKey) }
                .onSuccess { buses = it }
            delay(2000)
        }
    }

    Scaffold(
        containerColor = cs.background,
        topBar = {
            DetailBusTopBar(onBack = onBack)
        }
    ) { padding ->

        if (mapMode == MapMode.Full) {
            BusRouteMapBox(
                mapLoading = mapLoading,
                mapError = mapError,
                polyline = polyline,
                stops = mapStops,
                buses = buses,
                selectedBusId = busId,
                onCloseMap = { closeMap() },
                onToggleFull = { toggleFull() },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .navigationBarsPadding()
            )
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Spacer(Modifier.height(2.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(routeColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = routeNo,
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
                        text = routeCode,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            InfoBeigeCard(label = "Rute", value = routeTitle)
            InfoBeigeCard(label = "Tujuan Akhir", value = finalDestination)

            if (mapMode == MapMode.Inline) {
                BusRouteMapBox(
                    mapLoading = mapLoading,
                    mapError = mapError,
                    polyline = polyline,
                    stops = mapStops,
                    buses = buses,
                    selectedBusId = busId,
                    onCloseMap = { closeMap() },
                    onToggleFull = { toggleFull() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else {
                ArrivalSectionHeader(onOpenInlineMap = { openInlineMap() })

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 12.dp)
                ) {
                    items(stops) { s ->
                        ArrivalStopCard(
                            stopName = s.stopName,
                            etaMinutes = s.etaMinutes,
                            clock = s.clock
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ArrivalSectionHeader(onOpenInlineMap: () -> Unit) {
    val cs = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Jadwal Kedatangan Bus",
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
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_map),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = cs.onSecondary
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
private fun BusRouteMapBox(
    mapLoading: Boolean,
    mapError: String?,
    polyline: List<GeoPoint>,
    stops: List<StopMapUi>,
    buses: List<BusMapUi>,
    selectedBusId: String?,
    onCloseMap: () -> Unit,
    onToggleFull: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(20.dp)

    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = cs.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, cs.outlineVariant.copy(alpha = 0.55f))
    ) {
        Box(Modifier.fillMaxSize()) {

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
                    label = "bus_map_state"
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
                                selectedBusId = selectedBusId,
                                onBusClick = { },
                                onMapClick = { },
                                onBusAnchorChanged = { },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }

            MapTopControls(
                onToggleFull = onToggleFull,
                onCloseMap = onCloseMap,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(12.dp)
                    .fillMaxWidth()
            )
        }
    }
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
private fun InfoBeigeCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val bg = Color(0xFFF7F0D8)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = bg,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = cs.onSurfaceVariant,
                fontSize = 12.sp
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun ArrivalStopCard(
    stopName: String,
    etaMinutes: Int,
    clock: String,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(14.dp)
    val etaBg = Color(0xFFF3E2AE)

    val etaText = if (etaMinutes <= 0) "<1 menit" else "$etaMinutes menit"

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = cs.surface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, cs.outlineVariant.copy(alpha = 0.55f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Tiba di halte",
                    style = MaterialTheme.typography.labelSmall,
                    color = cs.onSurfaceVariant,
                    fontSize = 12.sp
                )

                Spacer(Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.ic_detail_buss),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(Modifier.width(10.dp))

                    Text(
                        text = stopName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .widthIn(min = 128.dp, max = 150.dp)
                    .background(etaBg, RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Estimasi Tiba",
                    fontSize = 12.sp,
                    color = Color.Black
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = etaText,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "($clock)",
                        fontSize = 12.sp,
                        color = Color.Black.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
