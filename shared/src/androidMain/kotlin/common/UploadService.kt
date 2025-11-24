//package common
//
//import android.content.Context
//import android.net.Uri
//import io.ktor.client.HttpClient
//import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
//import io.ktor.client.request.forms.FormPart
//import io.ktor.client.request.forms.InputProvider
//import io.ktor.http.ContentType
//import io.ktor.http.Headers
//import io.ktor.http.HttpHeaders
//import io.ktor.http.append
//import io.ktor.utils.io.core.ByteReadPacket
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import java.io.InputStream
//import io.ktor.client.engine.cio.CIO
//import io.ktor.client.plugins.logging.DEFAULT
//import io.ktor.client.plugins.logging.LogLevel
//import io.ktor.client.plugins.logging.Logger
//import io.ktor.client.plugins.logging.Logging
//
//
//actual class FileContainer(
//    val context: Context,
//    val uri: Uri
//) {
//
//    actual val fileName: String by lazy {
//        val cursor = context.contentResolver.query(uri, null, null, null, null)
//        val name = if (cursor != null && cursor.moveToFirst()) {
//            val idx = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
//            cursor.getString(idx)
//        } else "file.bin"
//        cursor?.close()
//        name
//    }
//
//    actual val size: Long by lazy {
//        val cursor = context.contentResolver.query(uri, null, null, null, null)
//        val length = if (cursor != null && cursor.moveToFirst()) {
//            val idx = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE)
//            cursor.getLong(idx)
//        } else 0L
//        cursor?.close()
//        length
//    }
//
//    fun openStream(): InputStream =
//        context.contentResolver.openInputStream(uri)
//            ?: error("Cannot open stream for uri: $uri")
//
//    actual suspend fun forEachChunk(
//        chunkSize: Int,
//        block: suspend (chunk: ByteArray) -> Unit
//    ) = withContext(Dispatchers.IO) {
//
//        val buffer = ByteArray(chunkSize)
//
//        openStream().use { input ->
//            while (true) {
//                val bytesRead = input.read(buffer)
//                if (bytesRead <= 0) break
//
//                val chunk = buffer.copyOf(bytesRead)
//                block(chunk)
//            }
//        }
//    }
//}
//
//actual fun createFormPart(
//    key: String,
//    fileContainer: FileContainer
//): FormPart<*> {
//    // Open the stream and read fully into memory (BEWARE for large files)
//    val stream: InputStream = (fileContainer as FileContainer).run {
//        // Access the underlying Uri stream; adapt if your FileContainer exposes an accessor
//        openStream()
//    }
//
//    val bytes = stream.use { it.readBytes() } // <-- reads whole file into memory
//
//    return FormPart(
//        key = key,
//        value = InputProvider(
//            size = bytes.size.toLong(),
//            block = { ByteReadPacket(bytes) }  // <-- RETURNS Input (VALID)
//        ),
//        headers = Headers.build {
//            append(HttpHeaders.ContentType, ContentType.Application.OctetStream)
//            append(
//                HttpHeaders.ContentDisposition,
//                "filename=\"${fileContainer.fileName}\""
//            )
//        }
//    )
//}
//
//actual fun createHttpClient(): HttpClient = HttpClient(CIO) {
//    install(ContentNegotiation) {
//        // JSON config if needed
//    }
//
//    install(Logging) {
//        // android.util.Log on Android, println on iOS
//        logger = Logger.DEFAULT
//
//        // 🛑 CRITICAL: Use HEADERS to avoid freezing on large files
//        level = LogLevel.HEADERS
//
//        // Optional: Hide sensitive tokens
//        sanitizeHeader { header -> header == HttpHeaders.Authorization }
//    }
//}
//
//actual fun io.github.vinceglb.filekit.core.PlatformFile.toFileContainer(context: Any?): FileContainer {
//    // FileKit on Android holds the Uri
//    return FileContainer(
//        context = (context as android.content.Context),
//        uri = this.uri // FileKit exposes .uri on Android
//    )
//}