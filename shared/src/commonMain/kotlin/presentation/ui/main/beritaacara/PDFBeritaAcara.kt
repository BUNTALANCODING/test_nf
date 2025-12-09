package presentation.ui.main.beritaacara

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
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
import common.rememberPdfDownloader
import common.toBase64
import common.toBytes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import presentation.component.AlertDialog
import presentation.component.DEFAULT__BUTTON_SIZE
import presentation.component.DefaultButton
import presentation.component.DefaultScreenUI
import presentation.component.DefaultTextField
import presentation.component.DottedBorderBackground
import presentation.component.SendEmailDialog
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
import presentation.ui.main.riwayat.viewmodel.RiwayatEvent
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

fun normalizePdfUrl(input: String): String {
    return input
        .replace("\\/", "/")
        .replace(Regex("^https:\\/\\/https:\\/\\/"), "https://")
        .trim()
}

@Composable
private fun PDFBeritaAcaraContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
) {
    LaunchedEffect(Unit){
        events(HomeEvent.PreviewBA)
    }

    val scope = rememberCoroutineScope()
    val downloader = rememberPdfDownloader()

    val purpleColor = Color(0xFF3F006E)

    val pdfUrl = normalizePdfUrl(state.urlPreviewBA)


    if (state.isSendEmailDialogOpen) {
        SendEmailDialog(
            title = "Kirim via Email",
            subtitle = "Masukkan email penerima",
            myEmail = state.myEmail,
            onDismissRequest = { events(HomeEvent.HideSendEmailDialog) },
            onSendClick = { emails, sendToMyEmail ->
                events(
                    HomeEvent.SendEmailBA(
                        emails = emails,
                        sendToMyEmail = sendToMyEmail
                    )
                )
            }
        )
    }

    if (state.isSuccessEmailDialogOpen) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { events(HomeEvent.HideSuccessEmailDialog) },
            title = { Text("Berhasil") },
            text = { Text("Berita acara berhasil dikirim ke email.") },
            confirmButton = {
                Button(
                    onClick = { events(HomeEvent.HideSuccessEmailDialog) }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
        ) {
            Base64PdfViewer(
                url = pdfUrl,
                modifier = Modifier.fillMaxSize()
            )
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp,
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(
                    onClick = {
                        if (pdfUrl.isNotEmpty()) {
                            scope.launch {
                                downloader.download(
                                    url = pdfUrl,
                                    fileName = "Laporan_${state.rampcheckId}.pdf"
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = purpleColor
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "UNDUH",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = {
                        events(HomeEvent.ShowSendEmailDialog)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    border = BorderStroke(2.dp, purpleColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "KIRIM KE EMAIL",
                        color = purpleColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


