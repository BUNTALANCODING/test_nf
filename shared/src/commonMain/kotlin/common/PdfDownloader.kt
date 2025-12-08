package common

import androidx.compose.runtime.Composable

interface PdfDownloader {
    suspend fun download(url: String, fileName: String)
}

@Composable
expect fun rememberPdfDownloader(): PdfDownloader