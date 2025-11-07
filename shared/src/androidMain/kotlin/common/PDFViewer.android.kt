package common

import android.util.Base64
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import business.constants.GetContext
import decodeBase64AndSaveToFile
import java.io.File
import java.io.FileOutputStream



@Composable
actual fun Base64PdfViewer(base64Pdf: String, modifier: Modifier) {
    val context = LocalContext.current

    // 1. Dekode dan simpan ke file sementara
    val pdfFile = decodeBase64AndSaveToFile(context, base64Pdf)

    // Tampilkan jika file gagal diproses
    if (pdfFile == null || !pdfFile.exists()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text("Gagal memuat dokumen PDF.")
        }
        return
    }

    // 2. Dapatkan Uri yang aman dari FileProvider
    val fileUri = try {
        FileProvider.getUriForFile(
            context,
            // Ganti dengan AUTHORITIES FileProvider Anda dari AndroidManifest.xml
            context.packageName + ".fileprovider",
            pdfFile
        )
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text("ERROR: FileProvider tidak terkonfigurasi dengan benar.")
        }
        return
    }

    // 3. Tampilkan PDF menggunakan WebView di dalam AndroidView
    AndroidView(
        modifier = modifier,
        factory = {
            WebView(it).apply {
                settings.javaScriptEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)
                webViewClient = WebViewClient()
            }
        },
        update = { webView ->
            // Gunakan Google Docs Viewer untuk merender PDF dari Uri file lokal
            val googleDocsUrl = "https://docs.google.com/gview?embedded=true&url="

            // Perlu melakukan URL encoding pada URI lokal
            val encodedUri = java.net.URLEncoder.encode(fileUri.toString(), "UTF-8")
            val urlToLoad = googleDocsUrl + encodedUri

            // Catatan: Google Docs Viewer harus dapat mengakses konten melalui Uri yang aman
            // yang disediakan oleh FileProvider.
            webView.loadUrl(urlToLoad)
        }
    )
}