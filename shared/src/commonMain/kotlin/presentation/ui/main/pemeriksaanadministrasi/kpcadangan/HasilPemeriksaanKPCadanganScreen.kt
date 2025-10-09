package presentation.ui.main.pemeriksaanadministrasi.kpcadangan

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import business.constants.SECTION_KARTU_UJI
import business.constants.SECTION_KP_CADANGAN
import business.core.UIComponent
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.painterResource
import presentation.component.ConditionCard
import presentation.component.DefaultScreenUI
import presentation.component.Spacer_16dp
import presentation.component.Spacer_8dp
import presentation.ui.main.datapemeriksaan.kir.ButtonNextSection
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_kemenhub
import rampcheck.shared.generated.resources.kartu_uji

@Composable
fun HasilPemeriksaanKPCadanganScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToCameraFace: () -> Unit
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Pemeriksaan Administrasi",
        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
        onClickStartIconToolbar = { popup() },
        endIconToolbar = Res.drawable.ic_kemenhub
    ) {
        HasilPemeriksaanKPCadanganContent(
            state = state,
            events = events,
            navigateToCameraFace = navigateToCameraFace
        )

    }
}

@Composable
private fun HasilPemeriksaanKPCadanganContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToCameraFace: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HeaderSection()
            CardSection(state, events)
            Spacer(Modifier.weight(1f))
            ButtonNextSection("LANJUT")
        }
    }
}

@Composable
private fun CardSection(state: HomeState, events: (HomeEvent) -> Unit) {
    val items = state.technicalConditions.filter { it.section == SECTION_KP_CADANGAN }
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            "Hasil Pemeriksaan",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
            )
        )
        Spacer_16dp()
        items.forEach { item ->
            ConditionCard(item = item, events = events)
            Spacer_8dp()
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

            Spacer_16dp()
            Column(modifier = Modifier.fillMaxWidth().height(120.dp).background(Color(0xFFE5E5E5)), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(Res.drawable.kartu_uji),
                    modifier = Modifier.height(110.dp),
                    contentDescription = null
                )
            }
            Spacer_8dp()
            Text(
                "KP Cadangan",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}


