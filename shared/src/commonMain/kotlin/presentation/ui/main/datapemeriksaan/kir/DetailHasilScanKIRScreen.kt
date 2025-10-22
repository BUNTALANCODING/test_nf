package presentation.ui.main.datapemeriksaan.kir

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import business.core.UIComponent
import kotlinx.coroutines.flow.Flow
import presentation.component.DefaultScreenUI
import presentation.component.InformasiRow
import presentation.component.Spacer_8dp
import presentation.theme.SuccessColor
import presentation.theme.TextGray
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_kemenhub

@Composable
fun DetailHasilScanScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToDetail: () -> Unit
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Data Pemeriksaan",
        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
        onClickStartIconToolbar = { popup() },
        endIconToolbar = Res.drawable.ic_kemenhub
    ) {
        DataKendaraanContent(
            state = state,
            events = events,
            navigateToDetail = navigateToDetail
        )

    }
}

@Composable
private fun DataKendaraanContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToDetail: () -> Unit
) {

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HeadlineSectionDetail()
            DetailKendaraanSection(state,events)
            Spacer(modifier = Modifier.weight(1f))
            ButtonNextSection("MULAI PEMERIKSAAN", state = state, events = events)
        }
    }

}




@Composable
fun HeadlineSectionDetail() {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "INSPEKSI KESELAMATAN LALU LINTAS DAN ANGKUTAN\n JALAN UNTUK ANGKUTAN UMUM",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )

    }
}

@Composable
fun DetailKendaraanSection(state: HomeState, events: (HomeEvent) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {

        Text(
            "Detail Pemeriksaan",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer_8dp()
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(16.dp),
            ) {

                InformasiRow("Nomor Kendaraan", state.platNumber)
                Spacer_8dp()
                InformasiRow("Nama Perusahaan Angkutan", state.operatorName)
                Spacer_8dp()
                InformasiRow("Jenis Angkutan", state.cargoName)
                Spacer_8dp()
                InformasiRow("Trayek", state.route)
                Spacer_8dp()
                InformasiRow("Nomor STUK", state.stukNo)
                Spacer_8dp()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Status Uji KIR",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Normal,
                            color = TextGray
                        )
                    )

                    Text(
                        state.status,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SuccessColor
                        )
                    )
                }
            }
        }
    }
}



