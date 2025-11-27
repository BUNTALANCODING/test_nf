package common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberPdfDownloader(): PdfDownloader {
    val context = LocalContext.current
    return remember { PdfDownloaderImpl(context) }
}
