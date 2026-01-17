package presentation.ui.main.inforute

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import presentation.ui.main.inforute.view_model.info.InfoRuteViewModel

data class RouteInfo(
    val id: String,
    val number: Int,
    val title: String,
    val operationalHours: String,
    val status: RouteStatus
)

enum class RouteStatus(val label: String) {
    ACTIVE("Masih Beroperasi"),
    ALMOST_DONE("Hampir Selesai"),
    ENDED("Operasional Selesai")
}

@Stable
data class InfoRuteUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val routes: List<RouteInfo> = emptyList()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoRuteScreen(
    onClose: () -> Unit = {},
    onRouteClick: (RouteInfo) -> Unit = {},
) {
    val cs = MaterialTheme.colorScheme
    val scope = rememberCoroutineScope()

    val vm: InfoRuteViewModel = org.koin.compose.koinInject()
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.load("")
    }

    LaunchedEffect(state.query) {
        kotlinx.coroutines.delay(350)
        vm.load(state.query)
    }

    Scaffold(
        containerColor = cs.background,
        topBar = {
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
                            "Info Rute",
                            fontWeight = FontWeight.SemiBold,
                            color = cs.onSecondary
                        )
                    },
                    actions = {
                        IconButton(onClick = onClose) {
                            Icon(
                                imageVector = Icons.Default.Close,
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
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(cs.background)
        ) {
            Spacer(Modifier.height(12.dp))

            SearchBox(
                value = state.query,
                onValueChange = vm::onQueryChange,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(14.dp))

            when {
                state.isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                state.errorMessage != null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(state.errorMessage ?: "Error", color = cs.error)
                            Spacer(Modifier.height(10.dp))
                            Button(onClick = { scope.launch { vm.load("") } }) {
                                Text("Coba lagi")
                            }
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.routes.size) { idx ->
                            val item = state.routes[idx]

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
                                )
                            ) {
                                RouteCard(
                                    route = item,
                                    onClick = { onRouteClick(item) }
                                )
                            }
                        }

                        item { Spacer(Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBox(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        placeholder = { Text("Cari nama rute atau kode..") },
        singleLine = true,
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        shape = RoundedCornerShape(14.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = cs.surface,
            unfocusedContainerColor = cs.surface,
            disabledContainerColor = cs.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
private fun RouteCard(
    route: RouteInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val statusColor = when (route.status) {
        RouteStatus.ACTIVE -> Color(0xFF2E7D32)
        RouteStatus.ALMOST_DONE -> Color(0xFFF9A825)
        RouteStatus.ENDED -> Color(0xFFC62828)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = cs.surface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
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
                    .background(colorForBadge(route.number)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = route.number.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = route.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                        fontSize = 14.sp
                    )

                    Spacer(Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = cs.onSurfaceVariant
                    )
                }

                Spacer(Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Jam Operasional:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = cs.onSurfaceVariant,
                        fontSize = 12.sp,
                        )

                    Spacer(Modifier.width(5.dp))

                    Text(
                        text = route.operationalHours,
                        style = MaterialTheme.typography.bodyMedium,
                        color = cs.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }


                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = route.status.label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = cs.onSurface,
                        fontSize = 12.sp
                    )
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(statusColor)
                    )
                }
            }
        }
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
