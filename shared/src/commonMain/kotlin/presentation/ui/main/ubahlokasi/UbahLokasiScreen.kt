package presentation.ui.main.ubahlokasi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import navbuss.shared.generated.resources.Res
import navbuss.shared.generated.resources.ic_location
import navbuss.shared.generated.resources.ic_map
import org.jetbrains.compose.resources.painterResource

@Immutable
data class LocationRowUi(
    val id: String,
    val title: String,
    val address: String,
    val isFavorite: Boolean
)

private enum class LocationTab { Recent, Favorite }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UbahLokasiScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onUseMyLocation: () -> Unit = {},
    onPickFromMap: () -> Unit = {},
    onLocationClick: (LocationRowUi) -> Unit = {},
    onToggleFavorite: (LocationRowUi) -> Unit = {},
    recent: List<LocationRowUi> = listOf(
        LocationRowUi(
            id = "1",
            title = "Sunter Mall",
            address = "Jl. Danau Sunter Utara Kav. II Blok G7, RT.1/RW.13, Sunter Agung, Kec. Tj Priok, Jakarta Utara, DKI Jakarta 14350",
            isFavorite = false
        ),
        LocationRowUi(
            id = "2",
            title = "Taman Ismail Marzuki",
            address = "Jl. Cikini Raya No. 8, Cikini, Kec. Menteng, Jakarta Pusat, DKI Jakarta 10330",
            isFavorite = true
        )
    ),
    favorites: List<LocationRowUi> = emptyList()
) {
    val cs = MaterialTheme.colorScheme
    val screenBg = Color(0xFFF3F3F3)

    var query by remember { mutableStateOf("") }
    var tab by remember { mutableStateOf(LocationTab.Recent) }

    val shownList = when (tab) {
        LocationTab.Recent -> recent
        LocationTab.Favorite -> favorites.ifEmpty { recent.filter { it.isFavorite } }
    }.let { list ->
        if (query.isBlank()) list
        else list.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.address.contains(query, ignoreCase = true)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = screenBg,
        topBar = {
            UbahLokasiTopBar(
                title = "Ubah Lokasi Saya",
                onBack = onBack,
                onFavoriteClick = null
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(screenBg)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Cari titik lokasi disini..") },
                singleLine = true,
                leadingIcon = {
                    Image(
                        painter = painterResource(Res.drawable.ic_location),
                        contentDescription = "ic search",
                        modifier = Modifier.size(24.dp)
                    )
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = cs.outline,
                    unfocusedBorderColor = cs.outlineVariant,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(Modifier.height(12.dp))

            val blue = Color(0xFF1E88E5)
            val yellow = Color(0xFFE0B24C)
            val yellowBg = yellow.copy(alpha = 0.12f)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onUseMyLocation,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(999.dp),
                    border = BorderStroke(1.6.dp, blue),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = blue
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.GpsFixed,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = blue
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Gunakan Lokasi Saya",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold,
                        color = blue
                    )
                }

                OutlinedButton(
                    onClick = onPickFromMap,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(999.dp),
                    border = BorderStroke(1.6.dp, yellow),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = yellowBg,
                        contentColor = yellow
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_map),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = yellow
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Pilih lewat Peta",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold,
                        color = yellow
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                TabChip(
                    selected = tab == LocationTab.Recent,
                    text = "Pencarian Terakhir",
                    onClick = { tab = LocationTab.Recent }
                )
                TabChip(
                    selected = tab == LocationTab.Favorite,
                    text = "Favorit",
                    onClick = { tab = LocationTab.Favorite }
                )
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = if (tab == LocationTab.Recent) "Daftar Pencarian Terakhir" else "Daftar Favorit",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = cs.onSurface
            )

            Spacer(Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (shownList.isEmpty()) {
                    item {
                        EmptyState(
                            text = if (tab == LocationTab.Recent)
                                "Belum ada pencarian terbaru."
                            else
                                "Belum ada lokasi favorit."
                        )
                    }
                } else {
                    items(shownList, key = { it.id }) { item ->
                        LocationItemCard(
                            item = item,
                            onClick = { onLocationClick(item) },
                            onToggleFavorite = { onToggleFavorite(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TabChip(
    selected: Boolean,
    text: String,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(999.dp)
    val selectedBg = Color.Black
    val unselectedBg = Color(0xFFE6E6E6)
    val selectedText = Color.White
    val unselectedText = Color.Black

    Surface(
        modifier = Modifier
            .clip(shape)
            .clickable(onClick = onClick),
        color = if (selected) selectedBg else unselectedBg,
        shape = shape
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            color = if (selected) selectedText else unselectedText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun LocationItemCard(
    item: LocationRowUi,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val cardShape = RoundedCornerShape(16.dp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = cardShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFF3D6)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color(0xFFD8A73B),
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = cs.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = item.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = cs.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.width(10.dp))

            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (item.isFavorite) Icons.Default.Star else Icons.Outlined.StarBorder,
                    contentDescription = "Favorite",
                    tint = if (item.isFavorite) Color(0xFFD8A73B) else cs.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UbahLokasiTopBar(
    title: String = "Ubah Lokasi Saya",
    onBack: () -> Unit,
    onFavoriteClick: (() -> Unit)? = null,
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
                if (onFavoriteClick != null) {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = Icons.Outlined.StarBorder,
                            contentDescription = "Favorit",
                            tint = cs.onSecondary
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = cs.primary),
            windowInsets = WindowInsets(0)
        )
    }
}

@Composable
private fun EmptyState(text: String) {
    val cs = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = cs.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
