package presentation.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import business.core.AppDataStore
import common.auth.AuthUserProvider
import navbuss.shared.generated.resources.Res
import navbuss.shared.generated.resources.ic_home
import navbuss.shared.generated.resources.ic_profile
import org.jetbrains.compose.resources.painterResource
import presentation.ui.main.home.HomeScreen
import presentation.ui.main.profile.ProfileScreen

enum class BottomTab { Home, Profile }

@Composable
fun MainScreen(
    navigateToInfoRute: () -> Unit,
    navigateToCariHalte: () -> Unit,
    navigateToJadwalBus: () -> Unit,
    navigateToUbahLokasi: () -> Unit,
    navigateToArCamScreen: () -> Unit,
    navigateToArahkanRute: () -> Unit,
    navigateToMauKemana: () -> Unit,
    appDataStore: AppDataStore
) {
    var selected by rememberSaveable { mutableStateOf(BottomTab.Home) }

    val user by AuthUserProvider.currentUser.collectAsState()

    val displayName = user?.displayName
        ?: user?.email?.substringBefore("@")
        ?: "Pengguna"

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            BottomNavBar(
                selected = selected,
                onSelected = { selected = it }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
        ) {
            when (selected) {
                BottomTab.Home -> HomeScreen(
                    name = displayName,
                    navigateToInfoRute = navigateToInfoRute,
                    navigateToCariHalte = navigateToCariHalte,
                    navigateToJadwalBus = navigateToJadwalBus,
                    navigateToUbahLokasi = navigateToUbahLokasi,
                    navigateToArCamScreen = navigateToArCamScreen,
                    navigateToArahkanRute = navigateToArahkanRute,
                    navigateToMauKemana = navigateToMauKemana,
                    appDataStore = appDataStore
                )

                BottomTab.Profile -> ProfileScreen()
            }
        }
    }
}


@Composable
fun BottomNavBar(
    selected: BottomTab,
    onSelected: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val compact = maxWidth < 360.dp

        val barHeight = if (compact) 75.dp else 77.dp
        val iconSize = if (compact) 22.dp else 24.dp
        val labelSize = if (compact) 11.sp else 12.sp
        val indicatorWidth = if (compact) 46.dp else 56.dp
        val indicatorHeight = if (compact) 3.dp else 4.dp

        Surface(
            color = cs.surface,
            tonalElevation = 0.dp,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(barHeight)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    modifier = Modifier.weight(1f),
                    selected = selected == BottomTab.Home,
                    icon = painterResource(Res.drawable.ic_home),
                    label = "Beranda",
                    iconSize = iconSize,
                    labelSize = labelSize,
                    indicatorWidth = indicatorWidth,
                    indicatorHeight = indicatorHeight,
                    onClick = { onSelected(BottomTab.Home) }
                )

                BottomNavItem(
                    modifier = Modifier.weight(1f),
                    selected = selected == BottomTab.Profile,
                    icon = painterResource(Res.drawable.ic_profile),
                    label = "Profil",
                    iconSize = iconSize,
                    labelSize = labelSize,
                    indicatorWidth = indicatorWidth,
                    indicatorHeight = indicatorHeight,
                    onClick = { onSelected(BottomTab.Profile) }
                )
            }
        }
    }
}


@Composable
private fun BottomNavItem(
    selected: Boolean,
    icon: Painter,
    label: String,
    onClick: () -> Unit,
    iconSize: Dp,
    labelSize: TextUnit,
    indicatorWidth: Dp,
    indicatorHeight: Dp,
    modifier: Modifier = Modifier
) {
    val active = Color.Black
    val inactive = Color(0xFFB0B0B0)

    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .width(indicatorWidth)
                .height(indicatorHeight)
                .clip(RoundedCornerShape(999.dp))
                .background(if (selected) active else Color.Transparent)
        )

        Spacer(Modifier.height(6.dp))

        Icon(
            painter = icon,
            contentDescription = label,
            tint = if (selected) active else inactive,
            modifier = Modifier.size(iconSize)
        )

        Spacer(Modifier.height(3.dp))

        Text(
            text = label,
            fontSize = labelSize,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (selected) active else inactive,
            maxLines = 1
        )
    }
}
