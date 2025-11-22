package presentation.ui.main.pemeriksaanteknis

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import business.constants.SECTION_BADAN
import business.constants.SECTION_BAN
import business.constants.SECTION_PENERANGAN
import business.constants.SECTION_PENGEREMAN
import business.constants.SECTION_PENGUKUR_KECEPATAN
import business.constants.SECTION_PERLENGKAPAN
import business.constants.SECTION_TANGGAP_DARURAT
import business.constants.SECTION_WIPER
import business.core.UIComponent
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.painterResource
import presentation.component.ConditionCard
import presentation.component.DEFAULT__BUTTON_SIZE
import presentation.component.DefaultButton
import presentation.component.DefaultScreenUI
import presentation.component.Spacer_16dp
import presentation.component.Spacer_32dp
import presentation.component.Spacer_8dp
import presentation.theme.PrimaryColor
import presentation.ui.main.datapemeriksaan.kir.ButtonNextSection
import presentation.ui.main.home.view_model.HomeEvent

import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_kemenhub
import rampcheck.shared.generated.resources.kartu_uji

@Composable
fun HasilPemeriksaanTeknisUtamaScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToTeknisPenunjang: () -> Unit
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Pemeriksaan Teknis Utama",
        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
        onClickStartIconToolbar = { popup() },
        endIconToolbar = Res.drawable.ic_kemenhub
    ) {
        HasilPemeriksaanKPCadanganContent(
            state = state,
            events = events,
            navigateToTeknisPenunjang = navigateToTeknisPenunjang
        )

    }
}

@Composable
private fun HasilPemeriksaanKPCadanganContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToTeknisPenunjang: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item { HeaderSection() }
                item { SistemPeneranganSection(state, events) }
                item { SistemPengeremanSection(state, events) }
                item { BadanKendaraanSection(state, events) }
                item { KondisiBanSection(state, events) }
                item { PerlengkapanSection(state, events) }
                item { PengukurKecepatanSection(state, events) }
                item { WiperSection(state, events) }
                item { TanggapDaruratSection(state, events) }
            }
        }



        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.White)
                .padding(16.dp)
        ) {
            DefaultButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DEFAULT__BUTTON_SIZE),
                enabled = true,
                enableElevation = false,
                text = "SIMPAN",
                onClick = {
                    navigateToTeknisPenunjang()
                }
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
        ) {
            Text(
                "Hasil Pemeriksaan",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
        }
    }
}


@Composable
private fun SistemPeneranganSection(state: HomeState, events: (HomeEvent) -> Unit) {
    val SECTION_ID = SECTION_PENERANGAN
    val items = state.technicalConditions.filter { it.section == SECTION_ID }

    Column(Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF3E9FF))) {
            Text(
                SECTION_ID,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
        Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
            items.forEach { item ->
                ConditionCard(item = item, events = events)
                Spacer_8dp()
            }
        }
    }
}

@Composable
private fun SistemPengeremanSection(state: HomeState, events: (HomeEvent) -> Unit) {
    val SECTION_ID = SECTION_PENGEREMAN
    val items = state.technicalConditions.filter { it.section == SECTION_ID }

    Column(Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF3E9FF))) {
            Text(
                SECTION_ID,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
        Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
            items.forEach { item ->
                ConditionCard(item = item, events = events)
                Spacer_8dp()
            }
        }
    }
}

@Composable
private fun BadanKendaraanSection(state: HomeState, events: (HomeEvent) -> Unit) {
    val SECTION_ID = SECTION_BADAN
    val items = state.technicalConditions.filter { it.section == SECTION_ID }

    Column(Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF3E9FF))) {
            Text(
                SECTION_ID,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
        Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
            items.forEach { item ->
                ConditionCard(item = item, events = events)
                Spacer_8dp()
            }
        }
    }
}

@Composable
private fun KondisiBanSection(state: HomeState, events: (HomeEvent) -> Unit) {
    val SECTION_ID = SECTION_BAN
    val items = state.technicalConditions.filter { it.section == SECTION_ID }

    Column(Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF3E9FF))) {
            Text(
                SECTION_ID,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
        Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
            items.forEach { item ->
                ConditionCard(item = item, events = events)
                Spacer_8dp()
            }
        }
    }
}

@Composable
private fun PerlengkapanSection(state: HomeState, events: (HomeEvent) -> Unit) {
    val SECTION_ID = SECTION_PERLENGKAPAN
    val items = state.technicalConditions.filter { it.section == SECTION_ID }

    Column(Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF3E9FF))) {
            Text(
                SECTION_ID,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
        Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
            items.forEach { item ->
                ConditionCard(item = item, events = events)
                Spacer_8dp()
            }
        }
    }
}

@Composable
private fun PengukurKecepatanSection(state: HomeState, events: (HomeEvent) -> Unit) {
    val SECTION_ID = SECTION_PENGUKUR_KECEPATAN
    val items = state.technicalConditions.filter { it.section == SECTION_ID }

    Column(Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF3E9FF))) {
            Text(
                SECTION_ID,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
        Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
            items.forEach { item ->
                ConditionCard(item = item, events = events)
                Spacer_8dp()
            }
        }
    }
}

@Composable
private fun WiperSection(state: HomeState, events: (HomeEvent) -> Unit) {
    val SECTION_ID = SECTION_WIPER
    val items = state.technicalConditions.filter { it.section == SECTION_ID }

    Column(Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF3E9FF))) {
            Text(
                SECTION_ID,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
        Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
            items.forEach { item ->
                ConditionCard(item = item, events = events)
                Spacer_8dp()
            }
        }
    }
}

@Composable
private fun TanggapDaruratSection(state: HomeState, events: (HomeEvent) -> Unit) {
    val SECTION_ID = SECTION_TANGGAP_DARURAT
    val items = state.technicalConditions.filter { it.section == SECTION_ID }

    Column(Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF3E9FF))) {
            Text(
                SECTION_ID,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
        Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
            items.forEach { item ->
                ConditionCard(item = item, events = events)
                Spacer_8dp()
            }
        }
    }
}

