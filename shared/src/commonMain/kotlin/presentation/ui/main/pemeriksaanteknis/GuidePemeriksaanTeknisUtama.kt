package presentation.ui.main.pemeriksaanteknis

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import business.core.UIComponent
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.painterResource
import presentation.component.DefaultScreenUI
import presentation.component.Spacer_16dp
import presentation.component.Spacer_8dp
import presentation.ui.main.datapemeriksaan.fotokendaraan.ButtonNextSection
import presentation.ui.main.datapemeriksaan.fotopetugas.GuideRow
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_bus_guide
import rampcheck.shared.generated.resources.ic_card_guide
import rampcheck.shared.generated.resources.ic_guie_teknis_utama
import rampcheck.shared.generated.resources.ic_identity
import rampcheck.shared.generated.resources.ic_kemenhub
import rampcheck.shared.generated.resources.ic_location_guide

@Composable
fun GuidePemeriksaanTeknisUtamaScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToCameraFace: () -> Unit
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Rampcheck Kemenhub",
        endIconToolbar = Res.drawable.ic_kemenhub
    ) {
        GuidePemeriksaanTeknisUtamaContent(
            state = state,
            events = events,
            navigateToCameraFace = navigateToCameraFace
        )

    }
}

@Composable
private fun GuidePemeriksaanTeknisUtamaContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToCameraFace: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HeaderSection()
            InformasiFaceContent()
            Spacer(modifier = Modifier.weight(1f))
            ButtonNextSection("MULAI PEMERIKSAAN")
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

            Image(
                painter = painterResource(Res.drawable.ic_guie_teknis_utama),
                contentDescription = null
            )
            Spacer_8dp()
            Text(
                "PEMERIKSAAN TEKNIS UTAMA",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Composable
private fun InformasiFaceContent() {
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Text(
                "Ketentuan Pemeriksaan",
                style = MaterialTheme.typography.labelMedium
            )
            Spacer_8dp()
            GuideRow(
                iconRes = Res.drawable.ic_bus_guide,
                description = "Petugas melakukan perekaman video terhadap kendaraan yang diperiksa"
            )
            Spacer_8dp()
            GuideRow(
                iconRes = Res.drawable.ic_bus_guide,
                description = "Kendaraan yang direkam harus sesuai dengan data kendaraan yang telah dipilih sebelumnya"
            )
        }
    }
}

