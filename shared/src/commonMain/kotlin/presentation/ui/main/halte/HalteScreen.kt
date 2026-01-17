package presentation.ui.main.halte

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import navbuss.shared.generated.resources.Res
import navbuss.shared.generated.resources.ic_arrow
import org.jetbrains.compose.resources.painterResource

data class HalteUi(
    val id: String,
    val name: String,
    val distanceText: String,
    val isFavorite: Boolean = false
)

enum class HalteTab { Nearest, Favorite, History }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalteScreen(
    modifier: Modifier = Modifier,
    initialTab: HalteTab = HalteTab.Nearest,
    items: List<HalteUi> = remember {
        listOf(
            HalteUi("1", "Tugu Narkoba 2", "150 m"),
            HalteUi("2", "Ruko Yasmin 1", "500 m"),
            HalteUi("3", "Radar Bogor", "750 m"),
            HalteUi("4", "Transmart", "1 km"),
            HalteUi("5", "Ramayana Jalan Baru", "1.2 km")
        )
    },
    onBack: () -> Unit = {},
    onHalteClick: (halteId: String) -> Unit = {}

) {
    val cs = MaterialTheme.colorScheme

    var tab by remember { mutableStateOf(initialTab) }
    var query by remember { mutableStateOf("") }

    val filtered = remember(items, tab, query) {
        val q = query.trim().lowercase()
        items
            .asSequence()
            .filter {
                when (tab) {
                    HalteTab.Nearest -> true
                    HalteTab.Favorite -> it.isFavorite
                    HalteTab.History -> true
                }
            }
            .filter { q.isEmpty() || it.name.lowercase().contains(q) }
            .toList()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = cs.background,
        topBar = {
            HalteTopBar(
                onBack = onBack,
            )
        }    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            HalteSearchField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth()
            )

            HalteTabsRow(
                tab = tab,
                onTabChange = { tab = it },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Rekomendasi halte terdekat dari lokasi kamu",
                style = MaterialTheme.typography.bodyMedium,
                color = cs.onSurfaceVariant
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filtered, key = { it.id }) { halte ->
                    HalteRowCard(
                        halte = halte,
                        onClick = { onHalteClick(halte.id) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HalteSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(14.dp)

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = { Text("Cari nama halte..") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = cs.onSurfaceVariant
            )
        },
        singleLine = true,
        shape = shape,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = cs.surface,
            unfocusedContainerColor = cs.surface,
            disabledContainerColor = cs.surface,
            focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
            unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
            cursorColor = cs.primary
        )
    )
}

@Composable
private fun HalteTabsRow(
    tab: HalteTab,
    onTabChange: (HalteTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TabChip(
            text = "Halte terdekat",
            selected = tab == HalteTab.Nearest,
            onClick = { onTabChange(HalteTab.Nearest) }
        )

        TabChip(
            text = "Favorit",
            selected = tab == HalteTab.Favorite,
            onClick = { onTabChange(HalteTab.Favorite) }
        )

        IconCircleButton(
            selected = tab == HalteTab.History,
            onClick = { onTabChange(HalteTab.History) }
        )
    }
}

@Composable
private fun TabChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    val bg = if (selected) cs.onSurface else cs.surfaceVariant
    val fg = if (selected) cs.surface else cs.onSurface

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(999.dp),
        color = bg,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Text(
            text = text,
            color = fg,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        )
    }
}

@Composable
private fun IconCircleButton(
    selected: Boolean,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val bg = if (selected) cs.onSurface else cs.surfaceVariant
    val fg = if (selected) cs.surface else cs.onSurface

    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = bg,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier.size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.History,
                contentDescription = "Riwayat",
                tint = fg,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun HalteRowCard(
    halte: HalteUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(16.dp)

    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        color = cs.surface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, cs.outlineVariant.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(cs.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.DirectionsBus,
                    contentDescription = null,
                    tint = cs.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = halte.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = cs.onSurface,
                    fontSize = 12.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = halte.distanceText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = cs.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }


            Image(
                painter = painterResource(Res.drawable.ic_arrow),
                contentDescription = "Lihat detail halte",
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalteTopBar(
    onBack: () -> Unit,
    title: String = "Halte"
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
            actions = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Tutup",
                        tint = cs.onSecondary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = cs.primary),
            windowInsets = WindowInsets(0)
        )
    }
}
