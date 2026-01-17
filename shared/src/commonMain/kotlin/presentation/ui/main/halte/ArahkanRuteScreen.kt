package presentation.ui.main.halte

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import navbuss.shared.generated.resources.Res
import navbuss.shared.generated.resources.ic_location
import navbuss.shared.generated.resources.ic_mark_location
import navbuss.shared.generated.resources.ic_swap
import org.jetbrains.compose.resources.painterResource

sealed interface RouteSegmentUi {
    data object Walk : RouteSegmentUi
    data class Bus(val routeNo: Int) : RouteSegmentUi
}

data class RouteOptionUi(
    val id: String,
    val segments: List<RouteSegmentUi>,
    val durationText: String,
    val distanceText: String,
    val isFastest: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArahkanRuteScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    originLabel: String = "Lokasi Saya",
    destinationLabel: String = "Tugu Narkoba 2",
    routes: List<RouteOptionUi> = remember {
        listOf(
            RouteOptionUi(
                id = "r1",
                segments = listOf(RouteSegmentUi.Walk, RouteSegmentUi.Bus(1), RouteSegmentUi.Walk),
                durationText = "40 Menit",
                distanceText = "2.1 km",
                isFastest = true
            ),
            RouteOptionUi(
                id = "r2",
                segments = listOf(
                    RouteSegmentUi.Walk,
                    RouteSegmentUi.Bus(1),
                    RouteSegmentUi.Bus(5),
                    RouteSegmentUi.Walk
                ),
                durationText = "1 jam 12 Menit",
                distanceText = "3.4 km"
            )
        )
    },
    onSwap: () -> Unit = {},
    onRouteClick: (RouteOptionUi) -> Unit = {}
) {
    val cs = MaterialTheme.colorScheme

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = cs.background,
        topBar = {
            SimpleTopBar(
                title = "Mau kemana hari ini?",
                onBack = onBack
            )
        }
    ) { padding ->
        val pageBg = cs.surfaceVariant.copy(alpha = 0.35f)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(pageBg)
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OriginDestinationCard(
                origin = originLabel,
                destination = destinationLabel,
                onSwap = onSwap
            )

            Surface(
                shape = RoundedCornerShape(999.dp),
                color = cs.onSurface,
                tonalElevation = 0.dp,
                shadowElevation = 0.dp
            ) {
                Text(
                    text = "Rekomendasi Rute",
                    color = cs.surface,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 18.dp)
            ) {
                items(routes, key = { it.id }) { r ->
                    RouteOptionCard(
                        data = r,
                        onClick = { onRouteClick(r) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimpleTopBar(
    title: String,
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
            colors = TopAppBarDefaults.topAppBarColors(containerColor = cs.primary),
            windowInsets = WindowInsets(0)
        )
    }
}

@Composable
private fun OriginDestinationCard(
    origin: String,
    destination: String,
    onSwap: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(18.dp)

    Surface(
        shape = shape,
        color = cs.surface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, cs.outlineVariant.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LocationRow(
                    icon = painterResource(Res.drawable.ic_location),
                    iconTint = Color(0xFFD32F2F),
                    text = origin
                )

                Divider(color = cs.outlineVariant.copy(alpha = 0.45f))

                LocationRow(
                    icon = painterResource(Res.drawable.ic_mark_location),
                    iconTint = cs.primary,
                    text = destination
                )
            }

            Spacer(Modifier.width(12.dp))

            Surface(
                onClick = onSwap,
                shape = RoundedCornerShape(14.dp),
                color = cs.primary,
                tonalElevation = 0.dp,
                shadowElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier.size(46.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_swap),
                        contentDescription = "Tukar",
                        tint = cs.onSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationRow(
    icon: Painter,
    iconTint: Color,
    text: String
) {
    val cs = MaterialTheme.colorScheme

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = text,
            color = cs.onSurface,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Composable
private fun RouteOptionCard(
    data: RouteOptionUi,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(18.dp)
    val beige = Color(0xFFF7F0D8)

    Surface(
        onClick = onClick,
        shape = shape,
        color = cs.surface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, cs.outlineVariant.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RouteSegmentsRow(segments = data.segments)
                Spacer(Modifier.weight(1f))

                if (data.isFastest) {
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = Color(0xFFD7F2C6),
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp
                    ) {
                        Text(
                            text = "Rute Tercepat",
                            color = Color(0xFF2E7D32),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatBox(
                    icon = Icons.Filled.Schedule,
                    label = "Estimasi Perjalanan",
                    value = data.durationText,
                    background = beige,
                    modifier = Modifier.weight(1f),
                )
                StatBox(
                    icon = Icons.Filled.LocationOn,
                    label = "Jarak",
                    value = data.distanceText,
                    background = beige,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun RouteSegmentsRow(segments: List<RouteSegmentUi>) {
    val cs = MaterialTheme.colorScheme

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        segments.forEachIndexed { index, s ->
            when (s) {
                RouteSegmentUi.Walk -> {
                    Icon(
                        imageVector = Icons.Filled.DirectionsWalk,
                        contentDescription = null,
                        tint = cs.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                }

                is RouteSegmentUi.Bus -> {
                    Icon(
                        imageVector = Icons.Filled.DirectionsBus,
                        contentDescription = null,
                        tint = cs.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                    RouteNoBadge(routeNo = s.routeNo)
                }
            }

            if (index != segments.lastIndex) {
                Text(
                    text = "›",
                    color = cs.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun RouteNoBadge(routeNo: Int) {
    val shape = RoundedCornerShape(8.dp)
    val bg = routeColorFor(routeNo)

    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(shape)
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = routeNo.toString(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun StatBox(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    background: Color,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(14.dp)

    Surface(
        modifier = modifier,
        shape = shape,
        color = background,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = cs.primary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = label,
                    color = cs.onSurface.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 12.sp
                )
            }

            Text(
                text = value,
                color = cs.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )
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
