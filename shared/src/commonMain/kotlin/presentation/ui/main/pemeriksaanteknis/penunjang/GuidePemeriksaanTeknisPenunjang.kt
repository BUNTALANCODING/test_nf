package presentation.ui.main.pemeriksaanteknis.penunjang

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
import presentation.component.Spacer_8dp
import presentation.ui.main.datapemeriksaan.fotopetugas.GuideRow
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_bus_guide
import rampcheck.shared.generated.resources.ic_guie_teknis_utama
import rampcheck.shared.generated.resources.ic_kemenhub

@Composable
fun GuidePemeriksaanTeknisUtamaScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToTeknisUtama: () -> Unit
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
            navigateToTeknisUtama = navigateToTeknisUtama
        )
    }
}

@Composable
private fun GuidePemeriksaanTeknisUtamaContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToTeknisUtama: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HeaderSection()
            InformasiGuideSection()
            Spacer(modifier = Modifier.weight(1f))
            presentation.ui.main.datapemeriksaan.kir.ButtonNextSection("MULAI PEMERIKSAAN", state, onClick = navigateToTeknisUtama)
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
                "PEMERIKSAAN TEKNIS PENUNJANG",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            Spacer_8dp()
            Text(
                "(Pemeriksaan Kendaraan Bagian Dalam)",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Composable
private fun InformasiGuideSection() {
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Text(
                "Ketentuan Pemeriksaan",
                style = MaterialTheme.typography.labelMedium
            )
            Spacer_8dp()
            GuideRow(
                iconRes = Res.drawable.ic_bus_guide,
                description = "Kendaraan yang periksa harus sesuai dengan data kendaraan yang telah dipilih sebelumnya"
            )
            Spacer_8dp()
            GuideRow(
                iconRes = Res.drawable.ic_bus_guide,
                description = "Petugas melakukan perekaman video pada bagian luar kendaraan"
            )
            Spacer_8dp()
            GuideRow(
                iconRes = Res.drawable.ic_bus_guide,
                description = "Rekam video kendaraan di area yang terang"
            )
            Spacer_8dp()
            GuideRow(
                iconRes = Res.drawable.ic_bus_guide,
                description = "Gunakan flash ketika merekam video apabila kondisi terlalu gelap"
            )
        }
    }
}

