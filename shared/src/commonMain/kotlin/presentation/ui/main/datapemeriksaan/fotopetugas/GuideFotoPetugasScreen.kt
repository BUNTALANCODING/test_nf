package presentation.ui.main.datapemeriksaan.fotopetugas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import business.core.UIComponent
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import presentation.component.DEFAULT__BUTTON_SIZE
import presentation.component.DefaultButton
import presentation.component.DefaultScreenUI
import presentation.component.Spacer_8dp
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_bus_guide
import rampcheck.shared.generated.resources.ic_kemenhub
import rampcheck.shared.generated.resources.ic_location_guide
import rampcheck.shared.generated.resources.ic_user_guide

@Composable
fun GuideFotoPetugasScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToCameraPetugas: () -> Unit
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Foto Petugas",
        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
        onClickStartIconToolbar = { popup() },
        endIconToolbar = Res.drawable.ic_kemenhub
    ) {
        GuideFotoPetugasContent(
            state = state,
            events = events,
            navigateToCameraPetugas = navigateToCameraPetugas
        )

    }
}

@Composable
private fun GuideFotoPetugasContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToCameraPetugas: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HeaderSection()
            InformasiFaceContent()
            Spacer(modifier = Modifier.weight(1f))
            ButtonSection(navigateToCameraPetugas = navigateToCameraPetugas)
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
            Text(
                "Foto Petugas",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer_8dp()

            Image(
                painter = painterResource(Res.drawable.ic_user_guide),
                contentDescription = "wajah"
            )
        }
    }
}

@Composable
private fun InformasiFaceContent() {
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Ketentuan Foto",
                style = MaterialTheme.typography.labelMedium
            )
            Spacer_8dp()
            GuideRow(
                iconRes = Res.drawable.ic_location_guide,
                description = "Petugas harus mengambil foto dilokasi rampcheck"
            )
            Spacer_8dp()
            GuideRow(
                iconRes = Res.drawable.ic_bus_guide,
                description = "Petugas mengambil foto didepan kendaraan yang akan diperiksa"
            )
        }
    }
}

@Composable
fun GuideRow(
    iconRes: DrawableResource,
    description: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = "guide"
        )
        Spacer_8dp()

        Text(
            description,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal,
            )
        )
        Spacer_8dp()

    }
}

@Composable
private fun ButtonSection(navigateToCameraPetugas: () -> Unit) {

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        DefaultButton(
            modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE),
            enabled = true,
            enableElevation = false,
            text = "AMBIL FOTO",
            onClick = { navigateToCameraPetugas() }
        )
    }
}


