package presentation.ui.main.datapemeriksaan.fotopetugas

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import business.core.UIComponent
import business.core.UIComponentState
import common.LocationFetcher
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.vectorResource
import presentation.component.BirthdayDatePrickerDialog
import presentation.component.CustomDropdownPicker
import presentation.component.DEFAULT__BUTTON_SIZE
import presentation.component.DefaultButton
import presentation.component.DefaultScreenUI
import presentation.component.DefaultTextField
import presentation.component.Spacer_16dp
import presentation.component.Spacer_8dp
import presentation.component.noRippleClickable
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
    navigateToGuideFoto: () -> Unit
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
            navigateToGuideFoto = navigateToGuideFoto
        )

    }
}

@Composable
private fun FormDataPemeriksaanContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToGuideFoto: () -> Unit
) {

    LaunchedEffect(Unit){
        events(HomeEvent.GetLocation)
    }
    if (state.locationTrigger) {
        LocationFetcher(
            onLocationReceived = { latLng ->
                events(HomeEvent.OnUpdateLatitude(latLng.latitude.toString()))
                events(HomeEvent.OnUpdateLongitude(latLng.longitude.toString()))
                events(HomeEvent.OnLocationTrigger(false))
            },
            onError = { message ->
                events(HomeEvent.OnUpdateStatusMessage(message))
                events(HomeEvent.OnLocationTrigger(false))
            }
        )
    }

    if (state.showDialogDatePicker == UIComponentState.Show) {
        BirthdayDatePrickerDialog(
            onDismiss = { events(HomeEvent.OnShowDialogDatePicker(UIComponentState.Hide)) },
            onConfirm = { selectedDate ->
                events(HomeEvent.OnUpdateTanggalPemeriksaan(selectedDate))
                events(HomeEvent.OnShowDialogDatePicker(UIComponentState.Hide))
            }
        )
    }


    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HeadlineSection()
            TextFieldSection(state, events)
            ButtonLocationSection(state, events)
            Spacer(modifier = Modifier.weight(1f))
            ButtonNextSection(state, events)
        }
    }

}


@Composable
fun HeadlineSection() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE).noRippleClickable {
                events(HomeEvent.OnShowDialogDatePicker(UIComponentState.Show))
            },
            value = state.tanggalPemeriksaan,
            onValueChange = { events(HomeEvent.OnUpdateTanggalPemeriksaan(it)) },
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
        CustomDropdownPicker(
            options = state.selectedLocationList,
            modifier = Modifier.fillMaxWidth(),
            value = state.location,
            expanded = state.showDialogLocation == UIComponentState.Show,
            onShowDropdown = {
                events(HomeEvent.OnShowDialogLocation(UIComponentState.Show))
            },
            onHideDropdown = { events(HomeEvent.OnShowDialogLocation(UIComponentState.Hide)) },
            onValueChange = { events(HomeEvent.OnUpdateLocation(it)) },
            onOptionSelected = {
                events(HomeEvent.OnUpdateLocation(it.rampcheckLocationName ?: ""))
                events(HomeEvent.OnUpdateLocationId(it.rampcheckLocationId ?: ""))
            }
        )
//        DefaultTextField(
//            modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE).noRippleClickable {
//                events(HomeEvent.OnShowDialogLocation(UIComponentState.Show))
//                events(HomeEvent.GetLocation)
//            },
//            value = state.location,
//            onValueChange = {events(HomeEvent.OnUpdateLocation(it))},
//            enabled = false,
//            placeholder = "Pilih Lokasi",
//            textStyle = MaterialTheme.typography.labelMedium.copy(
//                fontWeight = FontWeight.Normal
//            ),
//            iconEnd = {
//                Icon(
//                    imageVector = Icons.Default.ArrowDropDown,
//                    contentDescription = "Pilih Lokasi",
//                    tint = Color.Black,
//                    modifier = Modifier.size(16.dp)
//                )
//            },
//            color = Color.White
//        )
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
                    value = state.latitude,
                    onValueChange = { events(HomeEvent.OnUpdateTanggalPemeriksaan(it)) },
                    enabled = false,
                    placeholder = "Masukkan Latitude",
                    textStyle = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal,
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
                    value = state.longitude,
                    onValueChange = { events(HomeEvent.OnUpdateTanggalPemeriksaan(it)) },
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
fun ButtonLocationSection(state: HomeState, event: (HomeEvent) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Spacer_16dp()
        DefaultButton(
            onClick = {
                event(HomeEvent.OnLocationTrigger(true))
            },
            modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE),
            text = "GUNAKAN LOKASI SAYA",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightPurpleColor,
                contentColor = PrimaryColor
            )
        )
        Spacer_8dp()
        AnimatedVisibility(state.statusMessage.isNotEmpty()){
            Text(
                state.statusMessage,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.error
                )
            )
        }
    }
}

@Composable
fun ButtonNextSection(state: HomeState,event: (HomeEvent) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        DefaultButton(
            progressBarState = state.progressBarState,
            onClick = { event(HomeEvent.RampcheckStart) },
            modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE),
            text = "LANJUT",
            enabled = (state.tanggalPemeriksaan.isNotEmpty() && state.locationId.isNotEmpty() && state.latitude.isNotEmpty() && state.longitude.isNotEmpty()),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryColor,
                contentColor = Color.White
            )
        )
    }
}