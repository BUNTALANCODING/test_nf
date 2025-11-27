package common

import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

//class PdfDownloaderImpl(
//    private val context: Context
//) : PdfDownloader {
//
//    override suspend fun download(url: String, fileName: String) {
//        withContext(Dispatchers.IO) {
//            try {
//                val connection = URL(url).openConnection()
//                connection.connect()
//
//                val input = connection.getInputStream()
//
//                // Simpan ke folder Downloads
//                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                if (!downloadsDir.exists()) downloadsDir.mkdirs()
//
//                val file = File(downloadsDir, fileName)
//
//                FileOutputStream(file).use { output ->
//                    input.copyTo(output)
//                }
//
//                // Notify media scanner agar file muncul di File Manager
//                MediaScannerConnection.scanFile(
//                    context,
//                    arrayOf(file.absolutePath),
//                    arrayOf("application/pdf"),
//                    null
//                )
//
//                // Buka file setelah didownload
//                val uri = FileProvider.getUriForFile(
//                    context,
//                    context.packageName + ".fileprovider",
//                    file
//                )
//
//                val intent = Intent(Intent.ACTION_VIEW).apply {
//                    setDataAndType(uri, "application/pdf")
//                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                }
//
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                context.startActivity(intent)
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//}

class PdfDownloaderImpl(
    private val context: Context
) : PdfDownloader {

    override suspend fun download(url: String, fileName: String) {
        withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection()
                connection.connect()

                val input = connection.getInputStream()

                // Buat nama file unik setiap download
                val uniqueName = fileName.replace(".pdf", "") +
                        "_${System.currentTimeMillis()}.pdf"

                // Simpan di folder Downloads
                val downloadsDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                downloadsDir.mkdirs()

                val file = File(downloadsDir, uniqueName)

                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }

                // Scan supaya file muncul di File Manager
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(file.absolutePath),
                    arrayOf("application/pdf"),
                    null
                )

                // 🔥 SUCCESS MESSAGE — Tidak membuka file
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Berhasil mengunduh: $uniqueName",
                        Toast.LENGTH_LONG
                    ).show()
                }

                // ⛔ JANGAN buka file → HAPUS bagian FileProvider + Intent
                // Selesai.

            } catch (e: Exception) {
                e.printStackTrace()

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Gagal mengunduh file",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
