package presentation.ui.main.datapemeriksaan.fotopetugas

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import business.core.UIComponent
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.vectorResource
import presentation.component.DEFAULT__BUTTON_SIZE
import presentation.component.DefaultButton
import presentation.component.DefaultScreenUI
import presentation.component.DefaultTextField
import presentation.component.Spacer_16dp
import presentation.component.Spacer_8dp
import presentation.theme.LightPurpleColor
import presentation.theme.PrimaryColor
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_kemenhub

@Composable
fun FormDataPemeriksaanScreen(
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
        FormDataPemeriksaanContent(
            state = state,
            events = events,
            navigateToDetail = navigateToDetail
        )

    }
}

@Composable
private fun FormDataPemeriksaanContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToDetail: () -> Unit
) {

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HeadlineSection()
            TextFieldSection(state,events)
            ButtonLocationSection()
            Spacer(modifier = Modifier.weight(1f))
            ButtonNextSection()
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
            "Tanggal Pemeriksaan",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal
            )
        )
        Spacer_8dp()
        DefaultTextField(
            modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE),
            value = state.tanggalPemeriksaan,
            onValueChange = {events(HomeEvent.OnUpdateTanggalPemeriksaan(it))},
            enabled = false,
            placeholder = "Tanggal Pemeriksaan",
            textStyle = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal
            ),
            iconEnd = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Pilih Tanggal",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            },
            color = Color.White
        )
        Spacer_16dp()

        Text(
            "Lokasi",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal
            )
        )
        Spacer_8dp()
        DefaultTextField(
            modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE),
            value = state.tanggalPemeriksaan,
            onValueChange = {events(HomeEvent.OnUpdateTanggalPemeriksaan(it))},
            enabled = false,
            placeholder = "Pilih Lokasi",
            textStyle = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal
            ),
            iconEnd = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Pilih Tanggal",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            },
            color = Color.White
        )
        Spacer_16dp()

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(
                    "Latitude",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer_8dp()
                DefaultTextField(
                    modifier = Modifier.width(175.dp).height(DEFAULT__BUTTON_SIZE),
                    value = state.tanggalPemeriksaan,
                    onValueChange = {events(HomeEvent.OnUpdateTanggalPemeriksaan(it))},
                    enabled = false,
                    placeholder = "Masukkan Latitude",
                    textStyle = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.White
                )
            }

            Column {
                Text(
                    "Longitude",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer_8dp()
                DefaultTextField(
                    modifier = Modifier.width(175.dp).height(DEFAULT__BUTTON_SIZE),
                    value = state.tanggalPemeriksaan,
                    onValueChange = {events(HomeEvent.OnUpdateTanggalPemeriksaan(it))},
                    enabled = false,
                    placeholder = "Masukkan Longitude",
                    textStyle = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ButtonLocationSection() {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Spacer_16dp()
        DefaultButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE),
            text = "GUNAKAN LOKASI SAYA",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            colors = ButtonDefaults.buttonColors(containerColor = LightPurpleColor, contentColor = PrimaryColor)
        )
    }
}

@Composable
fun ButtonNextSection() {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)){
        DefaultButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE),
            text = "LANJUT",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor, contentColor = Color.White)
        )
    }
}