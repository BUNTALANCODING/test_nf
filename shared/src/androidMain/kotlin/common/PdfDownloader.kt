//package common
//
//import android.content.ContentValues
//import android.content.Context
//import android.os.Build
//import android.os.Environment
//import android.provider.MediaStore
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.platform.LocalContext
//import io.ktor.client.call.body
//import io.ktor.client.request.get
//import io.ktor.client.statement.HttpStatement
//import io.ktor.client.statement.bodyAsChannel
//import io.ktor.utils.io.jvm.javaio.toInputStream
//import org.koin.compose.koinInject
//import java.io.File
//import java.io.FileOutputStream
//import java.io.IOException
//
//import io.ktor.client.statement.*
//import io.ktor.client.call.*
//import io.ktor.utils.io.jvm.javaio.*
//import java.io.*
//import android.content.*
//import android.os.*
//import android.provider.*
//import android.widget.Toast
//import io.ktor.client.request.get
//import io.ktor.client.HttpClient
//import kotlinx.coroutines.cancel
//class AndroidPdfDownloader(private val context: Context, private val httpClient: HttpClient) : PdfDownloader {
//
//    override suspend fun download(url: String, fileName: String) {
//        // 1. Dapatkan HttpClientCall
//        val call = httpClient.get(url)
//
//        try {
//            // 2. Akses respons dari Call
//            val httpResponse = call.body<HttpResponse>()
//
//            // **KUNCI:** Tentukan MIME Type berdasarkan ekstensi di fileName
//            val (fileExtension, mimeType) = getMimeTypeAndExtension(fileName)
//
//            if (httpResponse.status.value != 200) {
//                throw IOException("Server mengembalikan status ${httpResponse.status.value}")
//            }
//
//            // 3. (VALIDASI) Periksa Content-Type dari server
//            val serverContentType = httpResponse.headers["Content-Type"]
//
//            // Periksa apakah serverContentType sesuai dengan mimeType yang diharapkan
//            if (serverContentType == null || !serverContentType.contains(mimeType.substringBefore(';'), ignoreCase = true)) {
//                // Memberi pesan yang lebih informatif
//                throw IOException("Tipe konten server ($serverContentType) tidak sesuai dengan tipe file yang diharapkan ($mimeType). Pastikan nama file (.pdf atau .docx) sesuai dengan yang ada di URL.")
//            }
//
//            // Dapatkan input stream dari Ktor
//            val fileInputStream = httpResponse.bodyAsChannel().toInputStream()
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                // MediaStore Logic (API 29+)
//                val resolver = context.contentResolver
//                val contentValues = ContentValues().apply {
//                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
//                    // **FLEXIBEL:** Gunakan MIME TYPE yang sudah divalidasi dari nama file
//                    put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
//                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
//                }
//                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
//
//                if (uri != null) {
//                    resolver.openOutputStream(uri).use { outputStream ->
//                        if (outputStream != null) {
//                            fileInputStream.copyTo(outputStream)
//                        } else {
//                            throw IOException("Gagal membuka output stream MediaStore.")
//                        }
//                    }
//                } else {
//                    throw IOException("Gagal membuat entri MediaStore.")
//                }
//            } else {
//                // Implementasi lama (API < Q)
//                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                if (!downloadsDir.exists()) downloadsDir.mkdirs()
//
//                val file = File(downloadsDir, fileName)
//
//                FileOutputStream(file).use { outputStream ->
//                    fileInputStream.copyTo(outputStream)
//                }
//            }
//
//            Toast.makeText(context, "File $fileName berhasil disimpan di Downloads", Toast.LENGTH_LONG).show()
//
//        } catch (e: Exception) {
//            Toast.makeText(context, "Gagal menyimpan: ${e.message}", Toast.LENGTH_LONG).show()
//            e.printStackTrace()
//        } finally {
//            // Pastikan untuk menutup koneksi
//            call.cancel()
//        }
//    }
//
//    // Fungsi helper untuk mendapatkan MIME Type dan Ekstensi
//    private fun getMimeTypeAndExtension(fileName: String): Pair<String, String> {
//        val extension = fileName.substringAfterLast('.', "").lowercase()
//        val mimeType = when (extension) {
//            "pdf" -> "application/pdf"
//            "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
//            "doc" -> "application/msword" // .doc
//            "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" // Excel
//            "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation" // PowerPoint
//            else -> "application/octet-stream" // Default/unknown type
//        }
//        return Pair(extension, mimeType)
//    }
//}
////class AndroidPdfDownloader(private val context: Context, private val httpClient: HttpClient) : PdfDownloader {
////
////    override suspend fun download(url: String, fileName: String) {
////        // 1. Dapatkan HttpClientCall
////        val call = httpClient.get(url)
////
////        try {
////            // 2. Akses respons dari Call
////            val httpResponse = call.body<HttpResponse>()
////
////            // Tentukan MIME Type berdasarkan ekstensi file
////            val (fileExtension, mimeType) = getMimeTypeAndExtension(fileName)
////
////            if (httpResponse.status.value != 200) {
////                throw IOException("Server mengembalikan status ${httpResponse.status.value}")
////            }
////
////            // (VALIDASI) Periksa Content-Type dari server
////            val serverContentType = httpResponse.headers["Content-Type"]
////            if (serverContentType == null || !serverContentType.contains(mimeType.substringBefore(';'), ignoreCase = true)) {
////                throw IOException("Tipe konten server ($serverContentType) tidak sesuai dengan tipe file yang diharapkan ($mimeType).")
////            }
////
////            // Dapatkan input stream dari Ktor
////            // bodyAsChannel() adalah metode pada HttpResponse
////            val fileInputStream = httpResponse.bodyAsChannel().toInputStream()
////
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
////                // MediaStore Logic (API 29+)
////                val resolver = context.contentResolver
////                val contentValues = ContentValues().apply {
////                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
////                    put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
////                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
////                }
////                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
////
////                if (uri != null) {
////                    resolver.openOutputStream(uri).use { outputStream ->
////                        if (outputStream != null) {
////                            fileInputStream.copyTo(outputStream)
////                        } else {
////                            throw IOException("Gagal membuka output stream MediaStore.")
////                        }
////                    }
////                } else {
////                    throw IOException("Gagal membuat entri MediaStore.")
////                }
////            } else {
////                // Implementasi lama (API < Q)
////                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
////                if (!downloadsDir.exists()) downloadsDir.mkdirs()
////
////                val file = File(downloadsDir, fileName)
////
////                FileOutputStream(file).use { outputStream ->
////                    fileInputStream.copyTo(outputStream)
////                }
////            }
////
////            Toast.makeText(context, "File $fileName berhasil disimpan di Downloads", Toast.LENGTH_LONG).show()
////
////        } catch (e: Exception) {
////            Toast.makeText(context, "Gagal menyimpan: ${e.message}", Toast.LENGTH_LONG).show()
////            e.printStackTrace()
////        } finally {
////            // 3. Pastikan untuk menutup koneksi setelah selesai/gagal
////            call.cancel()
////        }
////    }
////
////    // Fungsi helper Anda tetap sama
////    private fun getMimeTypeAndExtension(fileName: String): Pair<String, String> {
////        val extension = fileName.substringAfterLast('.', "").lowercase()
////        val mimeType = when (extension) {
////            "pdf" -> "application/pdf"
////            "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
////            "doc" -> "application/msword"
////            "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
////            "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation"
////            else -> "application/octet-stream"
////        }
////        return Pair(extension, mimeType)
////    }
////}
////
////// Tidak ada perubahan di bagian ini karena logic hanya ada di dalam class
//@Composable
//actual fun rememberPdfDownloader(): PdfDownloader {
//    val context = LocalContext.current
//    val httpClient: HttpClient = koinInject()
//
//    return remember(context, httpClient) {
//        AndroidPdfDownloader(context, httpClient)
//    }
//}
////class AndroidPdfDownloader(private val context: Context, private val httpClient: HttpClient) : PdfDownloader {
////
////    // 1. Signature updated to accept URL and is now suspend
////    override suspend fun download(url: String, fileName: String) {
////        try {
////            // 2. Use Ktor to stream the file data
////            httpClient.get(url).body<HttpStatement>().execute { httpResponse ->
////
////                if (httpResponse.status.value != 200) {
////                    throw IOException("Server returned status ${httpResponse.status.value}")
////                }
////
////                // Get the input stream from Ktor's streaming channel
////                val pdfInputStream = httpResponse.bodyAsChannel().toInputStream()
////
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
////                    // 3. MediaStore Logic
////                    val resolver = context.contentResolver
////                    val contentValues = ContentValues().apply {
////                        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
////                        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
////                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
////                    }
////                    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
////
////                    if (uri != null) {
////                        resolver.openOutputStream(uri).use { outputStream ->
////                            // 4. STREAM COPY: Copy bytes from network stream directly to file output stream
////                            if (outputStream != null) {
////                                pdfInputStream.copyTo(outputStream)
////                            }
////                        }
////                    } else {
////                        throw IOException("Gagal membuat entri MediaStore.")
////                    }
////                } else {
////                    // Implementasi lama (API < Q) menggunakan FileOutputStream
////
////                    // PENTING: Implementasi ini membutuhkan izin WRITE_EXTERNAL_STORAGE
////                    // yang harus diminta saat runtime.
////                    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
////
////                    // Pastikan direktori ada
////                    if (!downloadsDir.exists()) downloadsDir.mkdirs()
////
////                    val file = File(downloadsDir, fileName)
////
////                    FileOutputStream(file).use { outputStream ->
////                        pdfInputStream.copyTo(outputStream)
////                    }
////                }
////            }
////            Toast.makeText(context, "Disimpan di Downloads", Toast.LENGTH_LONG).show()
////        } catch (e: Exception) {
////            Toast.makeText(context, "Gagal menyimpan: ${e.message}", Toast.LENGTH_LONG).show()
////        }
////    }
////}
////
////// 'actual' untuk remember harus mengambil HttpClient
////@Composable
////actual fun rememberPdfDownloader(): PdfDownloader {
////    val context = LocalContext.current
////
////    // 1. Inject the configured HttpClient from the Koin graph
////    val httpClient: HttpClient = koinInject()
////
////    // 2. Instantiate the downloader with the retrieved client
////    return remember(context, httpClient) {
////        AndroidPdfDownloader(context, httpClient)
////    }
////}