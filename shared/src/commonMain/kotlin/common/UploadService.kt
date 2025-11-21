package common

import app.net2software.rampcheck.BuildKonfig
import business.datasource.network.main.responses.ChunkResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.ChannelProvider
import io.ktor.client.request.forms.FormPart
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.CancellationException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import presentation.ui.main.uploadChunk.UploadController
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

expect class FileContainer {
    val fileName: String
    val size: Long

    /**
     * Iterates through the file in chunks (default 1MB)
     */
    suspend fun forEachChunk(
        chunkSize: Int = 1_000_000,
        block: suspend (chunk: ByteArray) -> Unit
    )
}

// A function to convert that container into a Ktor FormPart
expect fun createFormPart(
    key: String,
    fileContainer: FileContainer
): FormPart<*>

object NetworkClient {

    private val client = createHttpClient()

    @OptIn(ExperimentalUuidApi::class)
    suspend fun uploadVideoInterior(
        token: String,
        container: FileContainer,
        controller: UploadController,
        onProgress: (Int) -> Unit // Keeping this simple as requested
    ): String = withContext(Dispatchers.IO) {

        var result: String? = null
        val chunkSize = 1_000_000 // 1 MB
        val total = container.size
        var uploadedBytes = 0L

        // Standard: Start at 1. If your backend expects 0, change this to 0.
        var chunkIndex = 1
        val totalChunks = ((total + chunkSize - 1) / chunkSize).toInt()

        // Generate a unique ID for this session (Simulated UUID if you don't have the lib)
        // Ideally use: Uuid.random().toString()
        val sessionUniqueKey = "${container.fileName}_${Uuid.random().toString()}"

        try {
            container.forEachChunk(chunkSize) { chunk ->

                // 1. CHECK CANCELLATION
                if (controller.isCancelled()) {
                    result = "Upload cancelled"
                    throw CancellationException("User cancelled")
                }

                controller.waitIfPaused()

                // 2. UPLOAD
                val success = uploadChunkToInterior(
                    token = token,
                    chunk = chunk,
                    fileName = container.fileName,
                    uniqueKey = sessionUniqueKey, // Use the session key, not just filename
                    chunkIndex = chunkIndex,
                    totalChunks = totalChunks
                )

                if (!success) {
                    result = "Upload failed"
                    // 🛑 FIX: Throw exception to STOP the file reader loop
                    throw IOException("Upload failed")
                }

                // 3. UPDATE PROGRESS
                uploadedBytes += chunk.size
                val percent = ((uploadedBytes.toFloat() / total) * 100).toInt()
                onProgress(percent)

                chunkIndex++
            }

            result = "Upload completed!"

        } catch (e: CancellationException) {
            result = "Upload cancelled"
        } catch (e: IOException) {
            result = result ?: "Upload failed"
        } catch (e: Exception) {
            result = "Error: ${e.message}"
        }

        return@withContext result ?: "Unknown Error"
    }

    private suspend fun uploadChunkToInterior(
        token: String,
        chunk: ByteArray,
        fileName: String,
        uniqueKey: String,
        chunkIndex: Int,
        totalChunks: Int
    ): Boolean {
        println("➡️ [Upload] Start Chunk $chunkIndex/$totalChunks")

        // 🛡️ SECURITY FIX 1: Strict Token Cleaning
        // Remove "Bearer", quotes, newlines, and spaces. We will rebuild it cleanly.
        val justTheToken = token
            .replace("Bearer", "", ignoreCase = true)
            .replace("\"", "")
            .trim()

        return try {
            val response = client.submitFormWithBinaryData(
                url = "${BuildKonfig.base_url}interioridentify",
                formData = formData {
                    // File Part
                    append("file", ChannelProvider(size = chunk.size.toLong()) { ByteReadChannel(chunk) }, Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                        append(HttpHeaders.ContentType, "application/octet-stream")
                    })
                    // Text Parts
                    append("unique_key", uniqueKey)
                    append("chunk_index", chunkIndex.toString())
                    append("total_chunks", totalChunks.toString())
                }
            ) {
                headers {
                    // 🛡️ SECURITY FIX 2: Remove any existing header first
                    // This prevents "Duplicate Header" issues if your HttpClient has a default config
                    remove(HttpHeaders.Authorization)

                    // Rebuild the header strictly
                    append(HttpHeaders.Authorization, "Bearer $justTheToken")
                }
            }

            if (response.status.value in 200..299) {
                println("✅ [Upload] Chunk $chunkIndex Success")
                true
            } else {
                // Print error to debug
                val errorBody = response.body<String>()
                println("❌ [Upload] Failed. Status: ${response.status}. Body: $errorBody")
                false
            }

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

expect fun createHttpClient(): HttpClient


expect fun io.github.vinceglb.filekit.core.PlatformFile.toFileContainer(context: Any? = null): FileContainer


