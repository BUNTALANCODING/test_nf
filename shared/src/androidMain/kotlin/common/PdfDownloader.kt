package common

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpStatement
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import org.koin.compose.koinInject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AndroidPdfDownloader(private val context: Context, private val httpClient: HttpClient) : PdfDownloader {

    // 1. Signature updated to accept URL and is now suspend
    override suspend fun download(url: String, fileName: String) {
        try {
            // 2. Use Ktor to stream the file data
            httpClient.get(url).body<HttpStatement>().execute { httpResponse ->

                if (httpResponse.status.value != 200) {
                    throw IOException("Server returned status ${httpResponse.status.value}")
                }

                // Get the input stream from Ktor's streaming channel
                val pdfInputStream = httpResponse.bodyAsChannel().toInputStream()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // 3. MediaStore Logic
                    val resolver = context.contentResolver
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    }
                    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                    if (uri != null) {
                        resolver.openOutputStream(uri).use { outputStream ->
                            // 4. STREAM COPY: Copy bytes from network stream directly to file output stream
                            if (outputStream != null) {
                                pdfInputStream.copyTo(outputStream)
                            }
                        }
                    } else {
                        throw IOException("Gagal membuat entri MediaStore.")
                    }
                } else {
                    // Implementasi lama (API < Q) menggunakan FileOutputStream

                    // PENTING: Implementasi ini membutuhkan izin WRITE_EXTERNAL_STORAGE
                    // yang harus diminta saat runtime.
                    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

                    // Pastikan direktori ada
                    if (!downloadsDir.exists()) downloadsDir.mkdirs()

                    val file = File(downloadsDir, fileName)

                    FileOutputStream(file).use { outputStream ->
                        pdfInputStream.copyTo(outputStream)
                    }
                }
            }
            Toast.makeText(context, "Disimpan di Downloads", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Gagal menyimpan: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}

// 'actual' untuk remember harus mengambil HttpClient
@Composable
actual fun rememberPdfDownloader(): PdfDownloader {
    val context = LocalContext.current

    // 1. Inject the configured HttpClient from the Koin graph
    val httpClient: HttpClient = koinInject()

    // 2. Instantiate the downloader with the retrieved client
    return remember(context, httpClient) {
        AndroidPdfDownloader(context, httpClient)
    }
}