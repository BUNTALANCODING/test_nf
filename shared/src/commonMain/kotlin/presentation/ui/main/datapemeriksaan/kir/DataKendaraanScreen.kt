package presentation.ui.main.datapemeriksaan.kir

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import business.core.UIComponent
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import presentation.component.DEFAULT__BUTTON_SIZE
import presentation.component.DefaultButton
import presentation.component.DefaultScreenUI
import presentation.component.DefaultTextField
import presentation.component.DottedBorderBackground
import presentation.component.Spacer_16dp
import presentation.component.Spacer_8dp
import presentation.theme.LightPurpleColor
import presentation.theme.PrimaryColor
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_camera
import rampcheck.shared.generated.resources.ic_kemenhub
import rampcheck.shared.generated.resources.ic_upload

@Composable
fun DataKendaraanScreen(
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
            HeadlineSection()
            TextFieldSection(state,events)
            Spacer(modifier = Modifier.weight(1f))
            ButtonNextSection("LANJUT PINDAI QR CODE KIR")

        }
    }

}




@Composable
fun HeadlineSection() {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "INSPEKSI KESELAMATAN LALU LINTAS DAN ANGKUTAN\n JALAN UNTUK ANGKUTAN UMUM",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )

        Spacer_16dp()

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Data Pemeriksaan",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }

    }
}

@Composable
fun TextFieldSection(state: HomeState, events: (HomeEvent) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {

        Text(
            "Nomor Kendaraan",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer_8dp()
        DefaultTextField(
            modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE),
            value = state.tanggalPemeriksaan,
            onValueChange = {events(HomeEvent.OnUpdateTanggalPemeriksaan(it))},
            enabled = false,
            placeholder = "Pilih Nomor Kendaraan",
            textStyle = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal
            ),
            iconEnd = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Arrow Down",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            },
            color = Color.White
        )
        Spacer_16dp()

        Text(
            "Foto KIR Kendaraan",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            "Pastikan foto posisi stiker KIR pada kendaraan terlihat jelas",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal
            )
        )
        Spacer_16dp()
        DottedBorderBackground(modifier = Modifier.fillMaxWidth().height(160.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painterResource(Res.drawable.ic_camera),
                    null,
                    modifier = Modifier.size(24.dp)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Fit
                )
                Spacer_8dp()
                Text(
                    "Ambil Foto",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = PrimaryColor
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun ButtonNextSection(label: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)){
        DefaultButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE),
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor, contentColor = Color.White)
        )
    }
}
