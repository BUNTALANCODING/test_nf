package presentation.ui.main.beritaacara

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import business.core.UIComponent
import business.core.UIComponentState
import common.Base64PdfViewer
import common.toBase64
import common.toBytes
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.painterResource
import presentation.component.AlertDialog
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
import rampcheck.shared.generated.resources.ic_check
import rampcheck.shared.generated.resources.ic_checked_figma
import rampcheck.shared.generated.resources.ic_guie_teknis_utama
import rampcheck.shared.generated.resources.ic_identity
import rampcheck.shared.generated.resources.ic_kemenhub
import rampcheck.shared.generated.resources.ic_location_guide
import rampcheck.shared.generated.resources.ic_tanda_tangan

@Composable
fun PDFBeritaAcaraScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Preview Berita Acara",
        endIconToolbar = Res.drawable.ic_kemenhub
    ) {
        PDFBeritaAcaraContent(
            state = state,
            events = events,
        )

    }
}

@Composable
private fun PDFBeritaAcaraContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
) {

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Base64PdfViewer(
                    base64Pdf = dummyBase64,
                    modifier = Modifier.fillMaxSize()
                        .padding(16.dp)
                )
            }
        }
    }
}

