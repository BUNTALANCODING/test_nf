package presentation.ui.main.pemeriksaanadministrasi.simpengemudi

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import business.constants.CARD_NOT_AVAILABLE
import business.constants.SECTION_KARTU_UJI
import business.constants.SECTION_SIM_PENGEMUDI
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
import rampcheck.shared.generated.resources.ic_kartu_not_available
import rampcheck.shared.generated.resources.ic_kemenhub
import rampcheck.shared.generated.resources.kartu_uji

@Composable
fun HasilPemeriksaanSIMPengemudiScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToPemeriksaanTeknis: () -> Unit
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Pemeriksaan Administrasi",
        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
        onClickStartIconToolbar = { popup() },
        endIconToolbar = Res.drawable.ic_kemenhub
    ) {
        HasilPemeriksaanSIMPengemudiContent(
            state = state,
            events = events,
            navigateToPemeriksaanTeknis = navigateToPemeriksaanTeknis
        )
    }
}

@Composable
private fun HasilPemeriksaanSIMPengemudiContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToPemeriksaanTeknis: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HeaderSection(state)
            CardSection(state, events)
            Spacer(Modifier.weight(1f))
            ButtonNextSection("LANJUT", state, onClick = {
                events(HomeEvent.NegativeAnswerSIM)
            })
        }
    }
}

@Composable
private fun CardSection(state: HomeState, events: (HomeEvent) -> Unit) {
    if(state.availableCard == CARD_NOT_AVAILABLE){
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)){
            Text(
                "Hasil Pemeriksaan",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
            Spacer_16dp()
            Text(
                "SIM Pengemudi",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal,
                )
            )
            Spacer_8dp()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .padding(16.dp),
            ) {
                Text(
                    state.keteranganKartuTidakAda ?: "",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
    } else {
        val items = state.getStepKartuUJi.questions?.filter { it?.questionId == 183 }
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                "Hasil Pemeriksaan",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
            Spacer_16dp()
            items?.forEach { item ->
                ConditionCard(
                    item = item!!, events = events, state = state,
                    value = state.tidakSesuai,
                    onValueChange = {
                        events(HomeEvent.OnUpdateTidakSesuai(it))
                    },
                    onClickCamera = {

                    }
                )
                Spacer_8dp()
            }

        }
    }
}


@Composable
private fun HeaderSection(state: HomeState) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer_16dp()
            Column(
                modifier = Modifier.fillMaxWidth().height(120.dp).background(Color(0xFFE5E5E5)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if(state.availableCard == CARD_NOT_AVAILABLE){
                    Image(
                        painter = painterResource(Res.drawable.ic_kartu_not_available),
                        modifier = Modifier.height(110.dp),
                        contentDescription = null
                    )
                } else {
                    Image(
                        painter = painterResource(Res.drawable.kartu_uji),
                        modifier = Modifier.height(110.dp),
                        contentDescription = null
                    )
                }
            }
            Spacer_8dp()
            Text(
                "SIM Pengemudi",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}


