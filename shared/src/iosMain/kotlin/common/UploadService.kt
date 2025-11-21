package common

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.FormPart
import io.ktor.client.request.forms.InputProvider
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import io.ktor.utils.io.core.ByteReadPacket
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSInputStream
import platform.Foundation.NSURL
import platform.Foundation.NSURLFileSizeKey
import platform.Foundation.dataWithContentsOfURL

actual class FileContainer(
    val url: NSURL
) {

    actual val fileName: String
        get() = url.lastPathComponent ?: "file.bin"

    @OptIn(ExperimentalForeignApi::class)
    actual val size: Long
        get() = url.resourceValuesForKeys(listOf(NSURLFileSizeKey), null)?.get(NSURLFileSizeKey) as? Long
            ?: 0L

    private fun openStream(): NSInputStream {
        return NSInputStream(url) ?: error("Cannot open NSInputStream for $url")
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun forEachChunk(
        chunkSize: Int,
        block: suspend (chunk: ByteArray) -> Unit
    ) = withContext(Dispatchers.Default) {


    }
}



actual fun createFormPart(
    key: String,
    fileContainer: FileContainer
): FormPart<*> {
    // Assume your FileContainer has a property `url: NSURL`
    val nsUrl: NSURL = fileContainer.url

    // Read the file as NSData
    val data: NSData? = NSData.dataWithContentsOfURL(nsUrl)
        ?: throw IllegalArgumentException("Cannot read file at ${nsUrl.absoluteString}")

    val bytes = data?.toByteArray()  // extension function to convert NSData -> ByteArray

    return FormPart(
        key = key,
        value = InputProvider(
            size = bytes?.size?.toLong(),
            block = { ByteReadPacket(bytes!!) }  // Ktor Input
        ),
        headers = Headers.build {
            append(HttpHeaders.ContentType, ContentType.Application.OctetStream)
            append(
                HttpHeaders.ContentDisposition,
                "filename=\"${fileContainer.fileName}\""
            )
        }
    )
}

// Extension function to convert NSData to ByteArray
@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
    val length = this.length.toInt()
    val byteArray = ByteArray(length)
    return byteArray
}

actual fun createHttpClient(): HttpClient = HttpClient(Darwin) {
    install(ContentNegotiation) {
        // JSON config if needed
    }
}

actual fun io.github.vinceglb.filekit.core.PlatformFile.toFileContainer(context: Any?): FileContainer {
    // FileKit on iOS holds the NSURL
    return FileContainer(
        url = this.nsUrl // FileKit exposes .nsUrl on iOS
    )
}