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
import com.github.barteksc.pdfviewer.PDFView
import decodeBase64AndSaveToFile
import java.io.File
import java.io.FileOutputStream


@Composable
actual fun Base64PdfViewer(url: String, modifier: Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                // Konfigurasi dasar untuk menampilkan PDF
                settings.javaScriptEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)

                // Pastikan WebView dapat menangani URL HTTPS
                webViewClient = WebViewClient()
            }
        },
        update = { webView ->
            // Muat URL PDF remote secara langsung.
            // Engine WebView akan menampilkan PDF secara default.
            webView.loadUrl(url)
        }
    )
}