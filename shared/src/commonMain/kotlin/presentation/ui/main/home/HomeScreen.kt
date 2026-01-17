package presentation.ui.main.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import business.constants.DataStoreKeys
import business.core.AppDataStore
import navbuss.shared.generated.resources.Res
import navbuss.shared.generated.resources.bg_home_header
import navbuss.shared.generated.resources.ic_buss_stop
import navbuss.shared.generated.resources.ic_empty_halte
import navbuss.shared.generated.resources.ic_empty_kedatanga
import navbuss.shared.generated.resources.ic_jadwal
import navbuss.shared.generated.resources.ic_layanan
import navbuss.shared.generated.resources.ic_layanan_halte
import navbuss.shared.generated.resources.ic_location
import navbuss.shared.generated.resources.ic_map_ar
import navbuss.shared.generated.resources.ic_map_rute
import navbuss.shared.generated.resources.ic_mark_location
import navbuss.shared.generated.resources.ic_rute
import navbuss.shared.generated.resources.ic_status
import org.jetbrains.compose.resources.painterResource
import presentation.ui.main.home.view_model.HomeAction
import presentation.ui.main.home.view_model.HomeViewModel
import presentation.ui.main.inforute.view_model.GeoPoint

data class HalteTerdekatUi(
    val title: String,
    val distanceText: String,
    val operationalText: String,
    val statusText: String,
)

data class BusArrivalUi(
    val routeNo: String,
    val routeColor: Color,
    val title: String,
    val etaMinutes: Int,
    val clock: String = "14:52",
    val routeCode: String = "TP 000",
)

@Composable
fun HomeScreen(
    name: String,
    navigateToInfoRute: () -> Unit,
    navigateToCariHalte: () -> Unit,
    navigateToJadwalBus: () -> Unit,
    navigateToUbahLokasi: () -> Unit,
    navigateToArCamScreen: () -> Unit,
    navigateToArahkanRute: () -> Unit,
    navigateToMauKemana: () -> Unit,
    halteTerdekat: HalteTerdekatUi? = null,
    busArrivals: List<BusArrivalUi> = emptyList(),
    appDataStore: AppDataStore,
) {
    val cs = MaterialTheme.colorScheme

    val vm: HomeViewModel = org.koin.compose.koinInject()
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) { vm.onAction(HomeAction.Load) }

    val halteTerdekat = state.halteTerdekat
    val busArrivals = state.busArrivals

    androidx.compose.runtime.LaunchedEffect(Unit) {
        vm.onAction(HomeAction.Load)
    }

    var savedTitle by remember { mutableStateOf<String?>(null) }
    var savedAddress by remember { mutableStateOf<String?>(null) }
    var savedPoint by remember { mutableStateOf<GeoPoint?>(null) }

    suspend fun loadSavedLocation() {
        val latStr = appDataStore.readValue(DataStoreKeys.USER_LAT)
        val lngStr = appDataStore.readValue(DataStoreKeys.USER_LNG)

        val lat = latStr?.toDoubleOrNull()
        val lng = lngStr?.toDoubleOrNull()

        savedPoint = if (lat != null && lng != null) GeoPoint(lat, lng) else null

        val t = appDataStore.readValue(DataStoreKeys.USER_PLACE_NAME)
        val a = appDataStore.readValue(DataStoreKeys.USER_PLACE_ADDRESS)

        savedTitle = t?.takeIf { it.isNotBlank() }
        savedAddress = a?.takeIf { it.isNotBlank() }
    }

    LaunchedEffect(Unit) {
        loadSavedLocation()
    }

    Scaffold(
        containerColor = cs.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(cs.background)
                .padding(padding),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                HeaderWithLocationCard(
                    name = name,
                    onClickUbahLokasi = navigateToUbahLokasi,
                    onClickMauKemana = navigateToMauKemana,
                    locationTitle = savedTitle,
                    locationAddress = savedAddress,
                    locationPoint = savedPoint
                )
            }

            item {
                InfoDanLayananSection(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onClickInfoRute = navigateToInfoRute,
                    onClickCariHalte = navigateToCariHalte,
                    onClickJadwalBus = navigateToJadwalBus,
                )
            }

            item {
                HalteTerdekatSection(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    navigateToArCamScreen = navigateToArCamScreen,
                    navigateToArahkanRute = navigateToArahkanRute,
                    halteTerdekat = halteTerdekat,
                    busArrivals = busArrivals
                )
            }
        }
    }
}

@Composable
private fun HeaderWithLocationCard(
    name: String,
    onClickUbahLokasi: () -> Unit,
    onClickMauKemana: () -> Unit,
    locationTitle: String?,
    locationAddress: String?,
    locationPoint: GeoPoint?
) {
    val headerHeight = 160.dp
    val overlap = 40.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        HomeHeader(
            name = name,
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .align(Alignment.TopCenter)
        )

        LocationCardSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = headerHeight - overlap),
            onClickUbah = onClickUbahLokasi,
            onClickMauKemana = onClickMauKemana,
            locationTitle = locationTitle,
            locationAddress = locationAddress,
            locationPoint = locationPoint
        )
    }
}

@Composable
private fun HomeHeader(
    name: String,
    modifier: Modifier = Modifier,
) {
    val headerContentHeight = 130.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(headerContentHeight)
    ) {
        Image(
            painter = painterResource(Res.drawable.bg_home_header),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Text(
                text = "Halo,",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = name,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        }
    }
}

@Composable
private fun LocationCardSection(
    modifier: Modifier = Modifier,
    onClickUbah: () -> Unit,
    onClickMauKemana: () -> Unit,
    locationTitle: String?,
    locationAddress: String?,
    locationPoint: GeoPoint?
) {
    val cs = MaterialTheme.colorScheme

    val titleText = if (!locationTitle.isNullOrBlank()) {
        locationTitle
    } else {
        if (locationPoint != null) "Lokasi Saya" else "Lokasi Saya"
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(cs.surface)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(Res.drawable.ic_location),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = titleText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = cs.onSurface
                )
            }

            Text(
                text = "Ubah",
                color = cs.primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(cs.primary.copy(alpha = 0.18f))
                    .clickable { onClickUbah() }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            )
        }

        Spacer(Modifier.height(12.dp))
        Divider(color = cs.outline.copy(alpha = 0.3f))
        Spacer(Modifier.height(12.dp))

        Spacer(Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClickMauKemana() }
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_mark_location),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "Mau kemana hari ini?",
                style = MaterialTheme.typography.bodyMedium,
                color = cs.onSurface.copy(alpha = 0.55f)
            )
        }
    }
}

@Composable
private fun InfoDanLayananSection(
    modifier: Modifier = Modifier,
    onClickInfoRute: () -> Unit,
    onClickCariHalte: () -> Unit,
    onClickJadwalBus: () -> Unit,
) {
    val cs = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(cs.surface)
            .padding(16.dp)
    ) {
        Text(
            text = "Informasi & Layanan",
            style = MaterialTheme.typography.titleMedium,
            color = cs.onSurface
        )

        Spacer(Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val shape = RoundedCornerShape(15.dp)

            ServiceMenuItem(
                title = "Info Rute",
                icon = painterResource(Res.drawable.ic_rute),
                modifier = Modifier.weight(1f).clip(shape),
                onClick = onClickInfoRute
            )
            ServiceMenuItem(
                title = "Cari Halte",
                icon = painterResource(Res.drawable.ic_buss_stop),
                modifier = Modifier.weight(1f).clip(shape),
                onClick = onClickCariHalte
            )
            ServiceMenuItem(
                title = "Kedatangan Bus",
                icon = painterResource(Res.drawable.ic_jadwal),
                modifier = Modifier.weight(1f).clip(shape),
                onClick = onClickJadwalBus
            )
        }
    }
}

@Composable
private fun ServiceMenuItem(
    title: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val cs = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(16.dp)

    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp),
            shape = shape,
            color = cs.surface,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            border = BorderStroke(1.dp, cs.outlineVariant)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier.size(44.dp)
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = cs.onSurface,
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun HalteTerdekatSection(
    modifier: Modifier = Modifier,
    navigateToArCamScreen: () -> Unit,
    navigateToArahkanRute: () -> Unit,
    halteTerdekat: HalteTerdekatUi?,
    busArrivals: List<BusArrivalUi>
) {
    val cs = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(cs.surface)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(Res.drawable.ic_layanan),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Halte terdekat dari lokasi kamu",
                style = MaterialTheme.typography.titleSmall,
                color = cs.onSurface
            )
        }

        Spacer(Modifier.height(12.dp))

        if (halteTerdekat == null) {
            EmptyHaltePlaceholder()
        } else {
            HalteInfoCard(
                title = halteTerdekat.title,
                distanceText = halteTerdekat.distanceText,
                operationalText = halteTerdekat.operationalText,
                statusText = halteTerdekat.statusText,
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HalteActionButton(
                    text = "Lihat dengan AR",
                    icon = painterResource(Res.drawable.ic_map_ar),
                    onClick = navigateToArCamScreen,
                    modifier = Modifier.weight(1f)
                )

                HalteActionButton(
                    text = "Arahkan Rute",
                    icon = painterResource(Res.drawable.ic_map_rute),
                    onClick = navigateToArahkanRute,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        SectionDivider()
        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(Res.drawable.ic_layanan_halte),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "Kedatangan bus terdekat halte ini",
                style = MaterialTheme.typography.bodyMedium,
                color = cs.onSurface,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(Modifier.height(10.dp))

        if (busArrivals.isEmpty()) {
            EmptyBusPlaceholder()
        } else {
            BusArrivalSection(arrivals = busArrivals)
        }
    }
}

@Composable
private fun EmptyHaltePlaceholder(modifier: Modifier = Modifier) {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_empty_halte),
            contentDescription = null,
            tint = cs.onSurface.copy(alpha = 0.18f),
            modifier = Modifier.size(150.dp)
        )
    }
}

@Composable
private fun EmptyBusPlaceholder(modifier: Modifier = Modifier) {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_empty_kedatanga),
            contentDescription = null,
            tint = cs.onSurface.copy(alpha = 0.18f),
            modifier = Modifier.size(150.dp)
        )
    }
}

@Composable
fun BusArrivalSection(arrivals: List<BusArrivalUi>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        arrivals.forEach { a ->
            BusArrivalItem(
                routeNo = a.routeNo,
                routeColor = a.routeColor,
                title = a.title,
                etaMinutes = a.etaMinutes,
                clock = a.clock,
                routeCode = a.routeCode
            )
        }
    }
}

@Composable
fun BusArrivalItem(
    routeNo: String,
    routeColor: Color,
    title: String,
    etaMinutes: Int,
    clock: String = "14:52",
    routeCode: String = "TP 000",
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val shape = RoundedCornerShape(14.dp)
    val etaBg = Color(0xFFF3E2AE)
    val cs = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(onClick = onClick)
            .background(cs.background.copy(alpha = 0.90f))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(routeColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = routeNo,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(routeColor)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsBus,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = routeCode,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Column {
                Text(
                    text = "Tujuan Akhir",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(Modifier.width(10.dp))

        val etaText = if (etaMinutes <= 0) "<1 menit" else "$etaMinutes menit"

        Column(
            modifier = Modifier
                .background(etaBg, RoundedCornerShape(10.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Estimasi Tiba",
                fontSize = 12.sp,
                color = Color.Black
            )

            Row {
                Text(
                    text = etaText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = "($clock)",
                    fontSize = 12.sp,
                    color = Color.Black.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun HalteInfoCard(
    title: String,
    distanceText: String,
    operationalText: String,
    statusText: String,
    modifier: Modifier = Modifier,
    onStatusClick: () -> Unit = {},
) {
    val cs = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(cs.background.copy(alpha = 0.90f))
            .padding(14.dp)
    ) {
        Column {
            Row {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = cs.onSurface,
                    fontSize = 14.sp
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = distanceText,
                    color = cs.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
            }

            Spacer(Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Jam Operasional: ", fontSize = 10.sp)
                    Text(
                        text = operationalText,
                        color = cs.onSurface.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 10.sp
                    )
                }

                Spacer(Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onStatusClick() }
                ) {
                    Text(text = statusText, fontSize = 10.sp)
                    Image(
                        painter = painterResource(Res.drawable.ic_status),
                        contentDescription = null,
                        modifier = Modifier
                            .size(14.dp)
                            .padding(start = 4.dp)
                    )
                }
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
private fun SectionDivider(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp),
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
    )
}

