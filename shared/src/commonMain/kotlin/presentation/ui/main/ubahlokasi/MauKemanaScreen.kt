package presentation.ui.main.ubahlokasi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Map
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
import navbuss.shared.generated.resources.ic_end_halte
import navbuss.shared.generated.resources.ic_maukemana
import navbuss.shared.generated.resources.ic_start_halte
import navbuss.shared.generated.resources.ic_swap
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MauKemanaScreen(
    onBack: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val bg = Color(0xFFF2F2F2)
    val pinRed = Color(0xFFD84A2F)

    var originText by remember { mutableStateOf("Lokasi Saya") }
    var destinationText by remember { mutableStateOf<String?>(null) }
    var isRouteAvailable by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = bg,
        topBar = { MauKemanaTopBar(onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(bg)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            FromToSwapCard(
                fromText = originText,
                toText = destinationText ?: "Cari lokasi tujuan kamu disini..",
                fromPlaceholder = false,
                toPlaceholder = destinationText == null,
                onFromClick = { },
                onToClick = { },
                onSwap = {
                    val oldFrom = originText
                    val oldTo = destinationText
                    originText = oldTo ?: "Lokasi Saya"
                    destinationText = if (oldFrom == "Lokasi Saya") null else oldFrom
                }
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { isRouteAvailable = !isRouteAvailable },
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp)
            ) {
                Text("Rekomendasi Rute", fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                if (!isRouteAvailable) {
                    EmptyRouteState(
                        brand = cs.primary,
                        pin = pinRed,
                        textColor = cs.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = "Rute tersedia 🎉",
                        color = cs.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MauKemanaTopBar(
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
                    "Mau kemana hari ini?",
                    color = cs.onSecondary,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
private fun FromToSwapCard(
    fromText: String,
    toText: String,
    fromPlaceholder: Boolean,
    toPlaceholder: Boolean,
    onFromClick: () -> Unit,
    onToClick: () -> Unit,
    onSwap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = cs.surface,
        border = BorderStroke(1.dp, cs.outlineVariant.copy(alpha = 0.55f))
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = onFromClick)
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_start_halte),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = fromText,
                        fontWeight = if (fromPlaceholder) FontWeight.Normal else FontWeight.SemiBold,
                        color = if (fromPlaceholder) cs.onSurfaceVariant else cs.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Divider(color = cs.outlineVariant)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = onToClick)
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_end_halte),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = toText,
                        fontWeight = if (toPlaceholder) FontWeight.Normal else FontWeight.SemiBold,
                        color = if (toPlaceholder) cs.onSurfaceVariant else cs.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            IconButton(
                onClick = onSwap,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(cs.primary)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_swap),
                    contentDescription = "Tukar",
                    tint = cs.onSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyRouteState(
    brand: Color,
    pin: Color,
    textColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_maukemana),
            contentDescription = null,
            modifier = Modifier.size(196.dp),
        )
    }
}


