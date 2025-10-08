package presentation.ui.main.pemeriksaanadministrasi

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import business.core.UIComponent
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import presentation.component.DEFAULT__BUTTON_SIZE
import presentation.component.DefaultButton
import presentation.component.DefaultScreenUI
import presentation.component.Spacer_16dp
import presentation.component.Spacer_8dp
import presentation.ui.main.datapemeriksaan.fotopetugas.ButtonNextSection
import presentation.ui.main.datapemeriksaan.fotopetugas.GuideRow
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_card_guide
import rampcheck.shared.generated.resources.ic_identity
import rampcheck.shared.generated.resources.ic_kemenhub
import rampcheck.shared.generated.resources.ic_user_guide

@Composable
fun GuidePemeriksaanAdministrasiScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToCameraFace: () -> Unit
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Rampcheck Kemenhub",
        endIconToolbar = Res.drawable.ic_kemenhub
    ) {
        GuidePemeriksaanAdministrasiContent(
            state = state,
            events = events,
            navigateToCameraFace = navigateToCameraFace
        )

    }
}

@Composable
private fun GuidePemeriksaanAdministrasiContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToCameraFace: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HeaderSection()
            InformasiFaceContent()
            Spacer(modifier = Modifier.weight(1f))
            presentation.ui.main.datapemeriksaan.kir.ButtonNextSection("MULAI PEMERIKSAAN")
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
                painter = painterResource(Res.drawable.ic_identity),
                contentDescription = "wajah"
            )
            Spacer_8dp()
            Text(
                "PEMERIKSAAN ADMINISTRASI",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            Spacer_16dp()
            Text(
                "Harap persiapkan dokumen yang akan diperiksa berikut ini:",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Composable
private fun InformasiFaceContent() {
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {

            GuideRow(
                iconRes = Res.drawable.ic_card_guide,
                description = "Kartu Uji/STUK"
            )
            Spacer_8dp()
            GuideRow(
                iconRes = Res.drawable.ic_card_guide,
                description = "KP Reguler"
            )
            Spacer_8dp()
            GuideRow(
                iconRes = Res.drawable.ic_card_guide,
                description = "KP Cadangan (Untuk Kendaraan Cadangan)"
            )
            Spacer_8dp()
            GuideRow(
                iconRes = Res.drawable.ic_card_guide,
                description = "SIM Pengemudi"
            )
        }
    }
}

