package presentation.ui.main.beritaacara

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import business.core.UIComponent
import business.core.UIComponentState
import common.toBase64
import common.toBytes
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.painterResource
import presentation.component.DEFAULT__BUTTON_SIZE
import presentation.component.DefaultButton
import presentation.component.DefaultScreenUI
import presentation.component.DefaultTextField
import presentation.component.DottedBorderBackground
import presentation.component.SignaturePad
import presentation.component.Spacer_16dp
import presentation.component.Spacer_4dp
import presentation.component.Spacer_8dp
import presentation.theme.BackgroundColor
import presentation.theme.PrimaryColor
import presentation.ui.main.auth.view_model.LoginState
import presentation.ui.main.datapemeriksaan.fotokendaraan.ButtonNextSection
import presentation.ui.main.datapemeriksaan.fotopetugas.GuideRow
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_bus_guide
import rampcheck.shared.generated.resources.ic_camera
import rampcheck.shared.generated.resources.ic_card_guide
import rampcheck.shared.generated.resources.ic_guie_teknis_utama
import rampcheck.shared.generated.resources.ic_identity
import rampcheck.shared.generated.resources.ic_kemenhub
import rampcheck.shared.generated.resources.ic_location_guide
import rampcheck.shared.generated.resources.ic_tanda_tangan

@Composable
fun KemenhubBeritaAcaraScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Pengemudi",
        endIconToolbar = Res.drawable.ic_kemenhub,
        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
        onClickStartIconToolbar = { popup() },
    ) {
        KemenhubBeritaAcaraContent(
            state = state,
            events = events,
            popup = popup
        )

    }
}

@Composable
private fun KemenhubBeritaAcaraContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    popup: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {

                Text(
                    "Nama Petugas Kemenhub",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer_8dp()

                DefaultTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.kemenhubName,
                    onValueChange = {
                        events(HomeEvent.OnUpdateKemenhubName(it))
                    },
                    placeholder = "Nama Petugas Kemenhub"
                )
                Spacer_16dp()
                Text(
                    "NIP",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer_8dp()

                DefaultTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.nipKemenhub,
                    onValueChange = {
                        events(HomeEvent.OnUpdateKemenhubNIP(it))
                    },
                    placeholder = "NIP"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            DefaultButton(
                onClick = popup,
                modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE),
                text = "SIMPAN",
                enabled = (state.kemenhubName.isNotEmpty() && state.nipKemenhub.isNotEmpty()),
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
}