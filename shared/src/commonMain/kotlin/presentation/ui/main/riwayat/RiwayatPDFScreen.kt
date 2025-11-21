package presentation.ui.main.riwayat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_kemenhub
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

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
        endIconToolbar = Res.drawable.ic_kemenhub
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
        // Asumsi event ini memuat URL ke dalam state (misalnya, state.pdfUrl)
        events(RiwayatEvent.PreviewBA)
    }

    val scope = rememberCoroutineScope()

    val downloader = rememberPdfDownloader()

    // 1. Ambil URL dari state (asumsi nama field di state adalah pdfUrl)
    val pdfUrl = state.urlPreviewBA

    // Gunakan warna ungu gelap dari gambar Anda
    val purpleColor = Color(0xFF3F006E)

    Column(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
        ) {
            // 2. Panggil Viewer dengan URL
            Base64PdfViewer(
                url = pdfUrl,
                modifier = Modifier.fillMaxSize()
            )
        }

        // 3. Bagian Tombol (Tetap di Bawah)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp, // Bayangan untuk memisahkan dari konten
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
                        // 4. Perbaiki logika download untuk memanggil URL
                        if (pdfUrl.isNotEmpty()) {
                            // downloader.download sekarang menerima URL!

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

                // Tombol 2: KIRIM KE EMAIL
                OutlinedButton(
                    onClick = { /* TODO: Aksi Kirim ke Email */ },
                    modifier = Modifier
                        .weight(1f) // Mengambil setengah ruang lainnya
                        .padding(start = 8.dp), // Jarak antar tombol
                    border = BorderStroke(2.dp, purpleColor), // Garis luar ungu
                    shape = RoundedCornerShape(8.dp) // Sudut melengkung
                ) {
                    Text(
                        text = "KIRIM KE EMAIL",
                        color = purpleColor, // Teks warna ungu
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}