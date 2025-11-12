package presentation.ui.main.pemeriksaanadministrasi.kpreguler

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import business.constants.CARD_NOT_AVAILABLE
import business.constants.KP_REGULER_TYPE
import business.core.UIComponent
import business.core.UIComponentState
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.painterResource
import presentation.component.ButtonVerticalSection
import presentation.component.DefaultScreenUI
import presentation.component.KartuTidakAdaDialog
import presentation.component.Spacer_48dp
import presentation.component.Spacer_8dp
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_identity
import rampcheck.shared.generated.resources.ic_kemenhub

@Composable
fun PemeriksaanKPRegularScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToCameraKPReguler: () -> Unit,
    navigateToHasilKPReguler: () -> Unit
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Pemeriksaan Administrasi",
        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
        onClickStartIconToolbar = { popup() },
        endIconToolbar = Res.drawable.ic_kemenhub
    ) {
        PemeriksaanKPRegularContent(
            state = state,
            events = events,
            navigateToCameraKPReguler = navigateToCameraKPReguler,
            navigateToHasilKPReguler = navigateToHasilKPReguler,
        )

    }
}

@Composable
private fun PemeriksaanKPRegularContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToCameraKPReguler: () -> Unit,
    navigateToHasilKPReguler: () -> Unit
) {
    if (state.showDialogKartuTidakAda == UIComponentState.Show) {
        KartuTidakAdaDialog(
            keterangan = state.keteranganKartuTidakAda ?: "",
            onKeteranganChange = { events(HomeEvent.OnUpdateKeteranganKartuTidakAda(it)) },
            onDismiss = {
                events(HomeEvent.OnShowDialogKartuTidakAda(UIComponentState.Hide))
            },
            onSimpan = {
                events(HomeEvent.OnShowDialogKartuTidakAda(UIComponentState.Hide))
                events(HomeEvent.OnUpdateTypeCard(KP_REGULER_TYPE))
                events(HomeEvent.OnUpdateCardAvailable(CARD_NOT_AVAILABLE))
                navigateToHasilKPReguler()
            },
            enableSimpan = state.keteranganKartuTidakAda != null
        )
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HeaderSection()
            ButtonVerticalSection(
                positiveButtonLabel = "AMBIL FOTO",
                negativeButtonLabel = "KARTU TIDAK ADA",
                positiveButtonClick = navigateToCameraKPReguler,
                negativeButtonClick = {events(HomeEvent.OnShowDialogKartuTidakAda(UIComponentState.Show))}
            )
        }
    }
}



@Composable
private fun HeaderSection() {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer_48dp()
            Image(
                painter = painterResource(Res.drawable.ic_identity),
                contentDescription = "wajah"
            )
            Spacer_8dp()
            Text(
                "KP Reguler",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}
