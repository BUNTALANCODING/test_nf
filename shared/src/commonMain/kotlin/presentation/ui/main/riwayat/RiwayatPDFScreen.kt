package presentation.ui.main.riwayat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import business.core.UIComponent
import common.Base64PdfViewer
import kotlinx.coroutines.flow.Flow
import presentation.component.DefaultScreenUI
import presentation.ui.main.riwayat.viewmodel.RiwayatEvent
import presentation.ui.main.riwayat.viewmodel.RiwayatState
import common.rememberPdfDownloader
import kotlinx.coroutines.launch
import presentation.component.SendEmailDialog
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_kemenhub
import kotlin.io.encoding.ExperimentalEncodingApi

private fun normalizePdfUrl(input: String): String {
    return input
        .replace("\\/", "/")
        .replace(Regex("^https:\\/\\/https:\\/\\/"), "https://")
        .trim()
}

@Composable
fun RiwayatPDFScreen(
    state: RiwayatState,
    events: (RiwayatEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Preview Berita Acara",
        endIconToolbar = Res.drawable.ic_kemenhub,
        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
        onClickStartIconToolbar = { popup() },
    ) {
        RiwayatPDFContent(
            state = state,
            events = events,
        )
    }
}

@OptIn(ExperimentalEncodingApi::class)
@Composable
private fun RiwayatPDFContent(
    state: RiwayatState,
    events: (RiwayatEvent) -> Unit,
) {
    LaunchedEffect(Unit) {
        events(RiwayatEvent.PreviewBA)
        events(RiwayatEvent.UpdateMyEmail)
    }

    val scope = rememberCoroutineScope()
    val downloader = rememberPdfDownloader()

    val pdfUrl = state.urlPreviewBA
    val purpleColor = Color(0xFF3F006E)

    if (state.isSendEmailDialogOpen) {
        SendEmailDialog(
            title = "Kirim via Email",
            subtitle = "Masukkan email penerima",
            myEmail = state.myEmail,
            onDismissRequest = { events(RiwayatEvent.HideSendEmailDialog) },
            onSendClick = { emails, sendToMyEmail ->
                events(
                    RiwayatEvent.SendEmailBA(
                        emails = emails,
                        sendToMyEmail = sendToMyEmail
                    )
                )
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
                url = normalizePdfUrl(pdfUrl),
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
                        val cleanUrl = normalizePdfUrl(pdfUrl)
                        scope.launch {
                            downloader.download(cleanUrl, "berita_acara.pdf")
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
                        events(RiwayatEvent.ShowSendEmailDialog)
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




