package common

import androidx.compose.runtime.Composable

/**
 * Antarmuka multiplatform untuk mengunduh file PDF dari byte-nya.
 */
interface PdfDownloader {
    // The download function must be 'suspend' for the Ktor network call
    suspend fun download(url: String, fileName: String)
}

/**
 * Composable 'expect' untuk mendapatkan instance PdfDownloader
 * yang spesifik untuk platform saat ini.
 */
@Composable
expect fun rememberPdfDownloader(): PdfDownloader