package common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.kashif.cameraK.utils.toNSData
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.call.body
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.compose.koinInject
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.stringByAppendingPathComponent
import platform.Foundation.writeToFile
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

class IosPdfDownloader(private val httpClient: HttpClient) : PdfDownloader {

    @OptIn(ExperimentalForeignApi::class)
    // 1. Signature diubah menjadi suspend dan menerima URL
    override suspend fun download(url: String, fileName: String) {

        // 2. Tentukan Path File Sementara
        val tempDir = NSTemporaryDirectory()
        // Gabungkan direktori sementara dan nama file
        val fullPath = (tempDir as NSString).stringByAppendingPathComponent(fileName)
        val tempFileUrl = NSURL.fileURLWithPath(fullPath)

        try {
            // 3. Ambil data biner dari URL menggunakan Ktor
            val bytes = httpClient.get(url).body<ByteArray>()

            // 4. Konversi ByteArray Ktor ke NSData
            val data = bytes.toNSData() as NSData

            // 5. Simpan ke file sementara
            data.writeToFile(fullPath, atomically = true)

            // 6. Tampilkan Share Sheet
            val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController
            val activityVC = UIActivityViewController(
                activityItems = listOf(tempFileUrl),
                applicationActivities = null
            )
            rootVC?.presentViewController(activityVC, animated = true, completion = null)

        } catch (e: Exception) {
            println("PDF Download Error (iOS): ${e.message}")
            // Tampilkan Alert/Error UI jika perlu
        }
    }
}

@Composable
actual fun rememberPdfDownloader(): PdfDownloader {

    // 1. Inject the configured HttpClient from the Koin graph
    val httpClient: HttpClient = koinInject()

    // 2. Instantiate the downloader and pass the injected client
    return remember(httpClient) {
        IosPdfDownloader(httpClient)
    }
}