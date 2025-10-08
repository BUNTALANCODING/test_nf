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
import presentation.theme.yellowBackground
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
import rampcheck.shared.generated.resources.ic_kemenhub
import rampcheck.shared.generated.resources.ic_pemeriksaan
import rampcheck.shared.generated.resources.ic_riwayat_pemeriksaan
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
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp),
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
            PemeriksaanSection(navigateToSaldo)
        }
        item { RiwayatPemeriksaanSection() }
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

        }
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
            Image(
                painter = painterResource(Res.drawable.ic_kemenhub),
                contentDescription = "ic_rampcheck",
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Logger.d("isTokenValid: ${state.isTokenValid}")
        Text(
            text = "RAMPCHECK KEMENHUB",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
            )
        )
    }
}

@Composable
fun PemeriksaanSection(navigateToSaldo: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = yellowBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(96.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .noRippleClickable {
                        navigateToSaldo()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_pemeriksaan),
                    contentDescription = null
                )
                Spacer_8dp()
                Text(
                    "PEMERIKSAAN",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }

        }
    }
}

@Composable
fun RiwayatPemeriksaanSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = yellowBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(96.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_riwayat_pemeriksaan),
                    contentDescription = null
                )
                Spacer_8dp()
                Text(
                    "RIWAYAT PEMERIKSAAN",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
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




