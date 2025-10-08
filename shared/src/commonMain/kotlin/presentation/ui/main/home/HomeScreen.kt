package presentation.ui.main.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import business.core.ProgressBarState
import business.core.UIComponent
import business.core.UIComponentState
import business.datasource.network.main.responses.ProfileDTO
import business.domain.main.NewsItem
import common.AndroidQOrBelow
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.camera.CAMERA
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import dev.icerock.moko.permissions.gallery.GALLERY
import dev.icerock.moko.permissions.notifications.REMOTE_NOTIFICATION
import dev.icerock.moko.permissions.storage.STORAGE
import dev.icerock.moko.permissions.storage.WRITE_STORAGE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import logger.Logger
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import presentation.component.CekPajakBottomDialog
import presentation.component.DefaultScreenUI
import presentation.component.Spacer_16dp
import presentation.component.Spacer_48dp
import presentation.component.Spacer_4dp
import presentation.component.Spacer_8dp
import presentation.component.noRippleClickable
import presentation.theme.PrimaryColor
import presentation.theme.gradientSamsatCeriaVertical
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.bell
import rampcheck.shared.generated.resources.ic_article_img
import rampcheck.shared.generated.resources.ic_cekpajak_samsat
import rampcheck.shared.generated.resources.ic_daftarkendaraan_samsat
import rampcheck.shared.generated.resources.ic_decor_pengaduan
import rampcheck.shared.generated.resources.ic_ekd
import rampcheck.shared.generated.resources.ic_epengesahan
import rampcheck.shared.generated.resources.ic_etbpkp
import rampcheck.shared.generated.resources.ic_isi_saldo
import rampcheck.shared.generated.resources.ic_riwayatpembayaran_samsat
import rampcheck.shared.generated.resources.ic_thorn
import rampcheck.shared.generated.resources.ic_transfer
import rampcheck.shared.generated.resources.ic_visibillity_off_home
import rampcheck.shared.generated.resources.profile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit = {},
    errors: Flow<UIComponent>,
    navigateToNotifications: () -> Unit = {},
    navigateToSaldo: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToNews: () -> Unit,
    navigateToMilikOrangLain: () -> Unit,
    navigateToDaftarKendaraan: () -> Unit,
    navigateToDaftarKendaraanSaya: () -> Unit,
    navigateToRiwayatPembayaran: () -> Unit,
    navigateToEtbpkp: () -> Unit,
    navigateToEPengesahan: () -> Unit,
    navigateToEKD: () -> Unit
) {
//    val homeState = rememberHomeScreenState(state)

    // Initialize permissions
    InitializePermissions(events)

    // Handle dialogs
//    HomeDialogs(state = state, events = events)

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        networkState = state.networkState,
        parent = true,
        onTryAgain = { },
    ) {
        HomeContent(
            state = state,
//            homeState = homeState,
            events = events,
            navigateToNotifications = navigateToNotifications,
            navigateToLogin = navigateToLogin,
            navigateToSaldo = navigateToSaldo,
            navigateToNews = navigateToNews,
            navigateToMilikOrangLain = navigateToMilikOrangLain,
            navigateToDaftarKendaraan = navigateToDaftarKendaraan,
            navigateToDaftarKendaraanSaya = navigateToDaftarKendaraanSaya,
            navigateToRiwayatPembayaran = navigateToRiwayatPembayaran,
            navigateToEtbpkp = navigateToEtbpkp,
            navigateToEPengesahan = navigateToEPengesahan,
            navigateToEKD = navigateToEKD
        )
    }
}

@Composable
private fun HomeContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToNotifications: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToSaldo: () -> Unit,
    navigateToNews: () -> Unit,
    navigateToMilikOrangLain: () -> Unit,
    navigateToDaftarKendaraan: () -> Unit,
    navigateToDaftarKendaraanSaya: () -> Unit,
    navigateToRiwayatPembayaran: () -> Unit,
    navigateToEtbpkp: () -> Unit,
    navigateToEPengesahan: () -> Unit,
    navigateToEKD: () -> Unit
) {

    if (state.showDialogPajak == UIComponentState.Show) {
        CekPajakBottomDialog(
            onDismiss = { events(HomeEvent.OnShowDialogPajak(UIComponentState.Hide)) },
            navigateToDaftarKendaraan = navigateToDaftarKendaraan,
            navigateToMilikOrangLain = navigateToMilikOrangLain
        )
    }
    val newsItems = listOf(
        NewsItem(
            date = "23 September 2025",
            title = "Program Pemutihan Pajak Kendaraan Pemprov Banten",
            imageUrl = Res.drawable.ic_article_img
        ),
        NewsItem(
            date = "24 September 2025",
            title = "7 Spot Wisata Pantai Eksotis di Pandeglang",
            imageUrl = Res.drawable.ic_article_img
        ),
        NewsItem(
            date = "25 September 2025",
            title = "Kuliner Legendaris Serang Wajib Coba",
            imageUrl = Res.drawable.ic_article_img
        )
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            HeaderSection(
                state = state,
                profile = state.profile,
                navigateToLogin = navigateToLogin,
                navigateToNotifications = navigateToNotifications,
                navigateToSaldo = navigateToSaldo,
                events = events,
                navigateToDaftarKendaraanSaya = navigateToDaftarKendaraanSaya,
                navigateToRiwayatPembayaran = navigateToRiwayatPembayaran
            )
        }
        item {
            DokumenSection(
                navigateToEtbpkp = navigateToEtbpkp,
                navigateToEKD = navigateToEKD,
                navigateToEPengesahan = navigateToEPengesahan
            )
        }
        item {
            PengaduanSection()
        }
        item { BeritaSection(newsItems = newsItems, onClick = navigateToNews) }
        item { }
    }
}

@Composable
private fun HeaderSection(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    profile: ProfileDTO,
    navigateToLogin: () -> Unit,
    navigateToNotifications: () -> Unit,
    navigateToSaldo: () -> Unit,
    navigateToDaftarKendaraanSaya: () -> Unit,
    navigateToRiwayatPembayaran: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileButton(
                state = state,
                profileName = profile.memberName.orEmpty(),
                onClick = navigateToLogin
            )

            Spacer(modifier = Modifier.weight(1f))

            NotificationButton(onClick = navigateToNotifications)
        }
        Spacer_16dp()

        Row(modifier = Modifier.fillMaxWidth().padding(16.dp).clickable {
            navigateToSaldo()
        }) {
            Column {
                Text(
                    "Saldo Samsat Ceria",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer_4dp()
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(

                        "Rp",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer_4dp()
                    Text(
                        "• • • • •",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                    )
                    Spacer_8dp()
                    Image(
                        painter = painterResource(Res.drawable.ic_visibillity_off_home),
                        contentDescription = "Hide Saldo"
                    )
                }

            }
            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_isi_saldo),
                    contentDescription = null
                )
                Spacer_8dp()
                Text(
                    "Isi Saldo",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                )
            }
            Spacer_8dp()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_transfer),
                    contentDescription = null
                )
                Spacer_8dp()
                Text(
                    "Transfer",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                )
            }

        }

        HomeMenu(
            events = events,
            navigateToDaftarKendaraanSaya = navigateToDaftarKendaraanSaya,
            navigateToRiwayatPembayaran = navigateToRiwayatPembayaran
        )
    }
}

@Composable
private fun ProfileButton(
    state: HomeState,
    profileName: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.noRippleClickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.background.copy(.2f),
                    CircleShape
                )
                .size(45.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.profile),
                contentDescription = "Profile",
                tint = MaterialTheme.colorScheme.background,
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Logger.d("isTokenValid: ${state.isTokenValid}")
        if (!state.isTokenValid) {
            Text(
                text = "Masuk/Daftar",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.background,
                fontFamily = FontFamily.SansSerif
            )
        } else {
            AnimatedVisibility(visible = state.progressBarState == ProgressBarState.LoadingWithLogo) {
                CircularProgressIndicator(
                    modifier = Modifier.size(25.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.background,
                )
            }
            Text(
                text = profileName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.background,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}

@Composable
private fun HomeMenu(
    events: (HomeEvent) -> Unit,
    navigateToDaftarKendaraanSaya: () -> Unit,
    navigateToRiwayatPembayaran: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HomeItem(
                iconRes = Res.drawable.ic_cekpajak_samsat,
                label = "Cek Pajak\nKendaraan",
                onClick = {
                    events(HomeEvent.OnShowDialogPajak(UIComponentState.Show))
                }
            )
            Spacer_4dp()
            HomeItem(
                iconRes = Res.drawable.ic_daftarkendaraan_samsat,
                label = "Daftar\nKendaraan",
                onClick = {
                    navigateToDaftarKendaraanSaya()
                },
            )
            Spacer_4dp()
            HomeItem(
                iconRes = Res.drawable.ic_riwayatpembayaran_samsat,
                label = "Riwayat\nPembayaran",
                onClick = {
                    navigateToRiwayatPembayaran()
                },
            )

        }

    }


}

@Composable
private fun NotificationButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.background.copy(.2f),
                CircleShape
            )
            .size(45.dp)
            .noRippleClickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(Res.drawable.bell),
            contentDescription = "Notifications",
            tint = MaterialTheme.colorScheme.background
        )
    }
}

@Composable
fun HomeItem(iconRes: DrawableResource, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clickable { onClick() }
                .size(64.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
            )
        }
        Spacer_8dp()
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

data class GridItemData(val icon: DrawableResource, val text: String, val onClick: () -> Unit)


@Composable
private fun DokumenSection(
    navigateToEtbpkp: () -> Unit,
    navigateToEKD: () -> Unit,
    navigateToEPengesahan: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text(
            "Dokumen Digital",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            "Akses dan Unduh Dokumen Digital Kendaraan Anda",
            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
        )

        Spacer_16dp()


        Row {
            DocumentCard(
                iconRes = Res.drawable.ic_etbpkp,
                title = "E-TBPKP",
                subtitle = "STNK Digital\nKendaraan",
            ) {
                navigateToEtbpkp()
            }
            Spacer_4dp()
            DocumentCard(
                iconRes = Res.drawable.ic_epengesahan,
                title = "E-Pengesahan",
                subtitle = "Bukti\nPengesahan STNK",
            ) {
                navigateToEPengesahan()
            }
            Spacer_4dp()
            DocumentCard(
                iconRes = Res.drawable.ic_ekd,
                title = "E-KD",
                subtitle = "Asuransi\nJasa Raharja",
            ) {
                navigateToEKD()
            }
        }

    }
}

@Composable
fun DocumentCard(
    iconRes: DrawableResource,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(114.dp)
            .height(116.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(gradientSamsatCeriaVertical)
            .padding(8.dp)
            .clickable { onClick() },
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
        Spacer_16dp()
        Text(
            title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer_4dp()
        Text(
            subtitle,
            style = MaterialTheme.typography.labelSmall.copy(color = Color.DarkGray)
        )
    }
}

@Composable
fun PengaduanSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(74.dp)) {
            Image(
                painter = painterResource(Res.drawable.ic_decor_pengaduan),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopStart)
            )
            // Teks dengan padding
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Ada Kendala ?",
                    style = MaterialTheme.typography.labelLarge,
                    color = PrimaryColor
                )
                Text(
                    "Sampaikan keluhan Anda dengan mudah disini",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
            }


            Image(
                painter = painterResource(Res.drawable.ic_thorn),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun BeritaSection(newsItems: List<NewsItem>, onClick: () -> Unit) {


    Box(modifier = Modifier.fillMaxWidth()) {
        // Teks dengan padding
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer_8dp()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Berita & Informasi",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        "Temukan Informasi menarik dan berita terkini!",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                Text(
                    "Lihat Semua",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryColor
                    ),
                    modifier = Modifier.clickable {
                        onClick()
                    }
                )
            }

            Spacer_16dp()

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(newsItems) { item ->
                    NewsItemCard(item = item)
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun NewsItemCard(item: NewsItem) {
    Card(
        modifier = Modifier
            .width(280.dp) // Adjust width as needed for your layout (e.g., in a LazyRow)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Set the background color of the card
        ),

        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Image(
                    painter = painterResource(item.imageUrl), // Replace with your image ID
                    contentDescription = "Image for ${item.title}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Date
            Text(
                text = item.date,
                style = MaterialTheme.typography.bodySmall, // Smaller text for date
                color = Color.Gray, // Greyish color for the date
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Title
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), // Bolder, medium size for the title
                color = Color.Black,
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
                    bottom = 12.dp
                ) // Padding for the title
            )
        }
    }
}


@Composable
private fun InitializePermissions(events: (HomeEvent) -> Unit) {
    val factory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
    val viewModel: PermissionsViewModel = remember(factory) {
        PermissionsViewModel(factory.createPermissionsController())
    }

    BindEffect(viewModel.permissionsController)

    LaunchedEffect(Unit) {
        viewModel.onButtonClick()
    }
}


// Permissions ViewModel
class PermissionsViewModel(
    val permissionsController: PermissionsController
) : ViewModel() {
    fun onButtonClick() {
        viewModelScope.launch {
            try {
                if (AndroidQOrBelow()) {
                    permissionsController.providePermission(Permission.CAMERA)
                    permissionsController.providePermission(Permission.GALLERY)
                    permissionsController.providePermission(Permission.WRITE_STORAGE)
                    permissionsController.providePermission(Permission.STORAGE)
                } else {
                    permissionsController.providePermission(Permission.REMOTE_NOTIFICATION)
                    permissionsController.providePermission(Permission.CAMERA)
                    permissionsController.providePermission(Permission.GALLERY)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

// Extension functions for better null safety
private fun String?.orEmpty(): String = this ?: ""

// Constants
private object HomeConstants {
    const val CARD_RADIUS = 10
    const val FIELD_RADIUS = 6
    const val BANNER_HEIGHT = 160
    const val ARTICLE_WIDTH = 180
    const val ARTICLE_IMAGE_HEIGHT = 120
    const val PROFILE_BUTTON_SIZE = 45
    const val ICON_SIZE = 14
    const val BANNER_ELEVATION = 8
}




