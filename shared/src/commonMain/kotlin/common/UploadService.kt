//package common
//
//import business.constants.BASE_URL
//import business.datasource.network.main.responses.ChunkResponse
//import io.ktor.client.HttpClient
//import io.ktor.client.call.body
//import io.ktor.client.request.forms.ChannelProvider
//import io.ktor.client.request.forms.FormPart
//import io.ktor.client.request.forms.formData
//import io.ktor.client.request.forms.submitFormWithBinaryData
//import io.ktor.client.utils.EmptyContent.headers
//import io.ktor.http.ContentType
//import io.ktor.http.Headers
//import io.ktor.http.HttpHeaders
//import io.ktor.http.headers
//import io.ktor.utils.io.ByteReadChannel
//import io.ktor.utils.io.CancellationException
//import io.ktor.utils.io.errors.IOException
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.IO
//import kotlinx.coroutines.withContext
//import presentation.ui.main.uploadChunk.UploadController
//import kotlin.uuid.ExperimentalUuidApi
//import kotlin.uuid.Uuid
//
//expect class FileContainer {
//    val fileName: String
//    val size: Long
//
//    /**
//     * Iterates through the file in chunks (default 1MB)
//     */
//    suspend fun forEachChunk(
//        chunkSize: Int = 1_000_000,
//        block: suspend (chunk: ByteArray) -> Unit
//    )
//}
//
//// A function to convert that container into a Ktor FormPart
//expect fun createFormPart(
//    key: String,
//    fileContainer: FileContainer
//): FormPart<*>
//
//
//
//object NetworkClient {
//
//    private val client = createHttpClient()
//
//
//    @OptIn(ExperimentalUuidApi::class)
//    suspend fun uploadVideoInterior(
//        token: String,
//        container: FileContainer,
//        controller: UploadController,
//        onProgress: (Int) -> Unit
//    ): String = withContext(Dispatchers.IO) {
//
//        var result: String? = null
//        val chunkSize = 1_000_000 // 1 MB
//        val total = container.size
//        var uploadedBytes = 0L
//
//        // ✅ server minta index mulai dari 0
//        var chunkIndex = 0
//        val totalChunks = ((total + chunkSize - 1) / chunkSize).toInt()
//
//        val sessionUniqueKey = "${container.fileName}_${Uuid.random().toString()}"
//
//        try {
//            container.forEachChunk(chunkSize) { chunk ->
//
//                if (controller.isCancelled()) {
//                    result = "Upload cancelled"
//                    throw CancellationException("User cancelled")
//                }
//
//                controller.waitIfPaused()
//
//                // 🔥 Kirim token mentah ke bawah, bersihkan di uploadChunkToInterior
//                val success = uploadChunkToInterior(
//                    token = token,
//                    chunk = chunk,
//                    fileName = container.fileName,
//                    uniqueKey = sessionUniqueKey,
//                    chunkIndex = chunkIndex,      // ⬅️ sekarang 0,1,2,...
//                    totalChunks = totalChunks
//                )
//
//                if (!success) {
//                    result = "Upload failed"
//                    throw IOException("Upload failed")
//                }
//
//                uploadedBytes += chunk.size
//                val percent = ((uploadedBytes.toFloat() / total) * 100).toInt()
//                onProgress(percent)
//
//                println("🔢 [Upload] selesai kirim chunkIndex = $chunkIndex")
//                chunkIndex++                     // ⬅️ increment di akhir loop
//            }
//
//            result = "Upload completed!"
//        } catch (e: CancellationException) {
//            result = "Upload cancelled"
//        } catch (e: IOException) {
//            result = result ?: "Upload failed"
//        } catch (e: Exception) {
//            result = "Error: ${e.message}"
//        }
//
//        return@withContext result ?: "Unknown Error"
//    }
//
//    private suspend fun uploadChunkToInterior(
//        token: String,
//        chunk: ByteArray,
//        fileName: String,
//        uniqueKey: String,
//        chunkIndex: Int,
//        totalChunks: Int
//    ): Boolean {
//        println("➡️ [Upload] Start Chunk $chunkIndex / ${totalChunks - 1}")
//
//        // 🔥 BERSIHKAN TOKEN DENGAN BENAR
//        val cleanToken = token
//            .replace("Bearer", "", ignoreCase = true)
//            .replace("\\s".toRegex(), "")   // buang semua whitespace
//            .replace("\"", "")
//            .trim()
//
//        val finalToken = "Bearer $cleanToken"
//
//        // 🔍 DEBUG: cek nilai yang akan dikirim
//        println("🔍 [Upload DEBUG] BASE_URL         = '$BASE_URL'")
//        println("🔍 [Upload DEBUG] URL              = '${BASE_URL}interioridentify'")
//        println("🔍 [Upload DEBUG] fileName         = '$fileName'")
//        println("🔍 [Upload DEBUG] unique_key       = '$uniqueKey'")
//        println("🔍 [Upload DEBUG] chunk_index      = $chunkIndex")
//        println("🔍 [Upload DEBUG] total_chunks     = $totalChunks")
//        println("🔍 [Upload DEBUG] chunk.size       = ${chunk.size} bytes")
//        println("🔍 [Upload DEBUG] RAW token        = '$token'")
//        println("🔍 [Upload DEBUG] CLEAN token      = '$cleanToken'")
//        println("🔍 [Upload DEBUG] Authorization    = '$finalToken'")
//
//        return try {
//            val response = client.submitFormWithBinaryData(
//                url = "${BASE_URL}interioridentify",
//                formData = formData {
//                    append(
//                        "file",
//                        ChannelProvider(size = chunk.size.toLong()) { ByteReadChannel(chunk) },
//                        Headers.build {
//                            append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
//                            append(HttpHeaders.ContentType, "application/octet-stream")
//                        }
//                    )
//                    append("unique_key", uniqueKey)
//                    append("chunk_index", chunkIndex.toString())   // ⬅️ sekarang 0-based
//                    append("total_chunks", totalChunks.toString())
//                }
//            ) {
//                headers {
//                    remove(HttpHeaders.Authorization)
//                    append(HttpHeaders.Authorization, finalToken)
//                }
//            }
//
//            val status = response.status
//            val bodyText = response.body<String>()
//
//            println("📡 [Upload DEBUG] Response status = $status")
//            println("📡 [Upload DEBUG] Response body   = $bodyText")
//
//            if (status.value in 200..299) {
//                println("✅ [Upload] Chunk $chunkIndex Success")
//                true
//            } else {
//                println("❌ [Upload] Chunk $chunkIndex FAILED with status $status")
//                false
//            }
//
//        } catch (e: Exception) {
//            println("💥 [Upload ERROR] Exception: ${e::class.simpleName} -> ${e.message}")
//            e.printStackTrace()
//            false
//        }
//    }
//}
//
//
//expect fun createHttpClient(): HttpClient
//
//
//expect fun io.github.vinceglb.filekit.core.PlatformFile.toFileContainer(context: Any? = null): FileContainer
//
////expect fun PlatformFile.toFileContainer(
////    context: Any? = null
////): FileContainer
//
//
