package common

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual class FileContainer actual constructor(
    context: Any?,
    rawUri: String
) {

    private val appContext: Context = context as Context
    private val uri: Uri = Uri.parse(rawUri)

    actual val fileName: String by lazy {
        val cr = appContext.contentResolver
        val cursor = cr.query(uri, null, null, null, null)
        val name = if (cursor != null && cursor.moveToFirst()) {
            val idx = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            cursor.getString(idx)
        } else {
            "file.bin"
        }
        cursor?.close()
        name
    }

    actual val size: Long by lazy {
        val cr = appContext.contentResolver
        val cursor = cr.query(uri, null, null, null, null)
        val length = if (cursor != null && cursor.moveToFirst()) {
            val idx = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE)
            cursor.getLong(idx)
        } else {
            0L
        }
        cursor?.close()
        length
    }

    private fun openStream() =
        appContext.contentResolver.openInputStream(uri)
            ?: error("Cannot open input stream for uri: $uri")

    actual suspend fun forEachChunk(
        chunkSize: Int,
        block: suspend (chunk: ByteArray) -> Unit
    ) = withContext(Dispatchers.IO) {
        val buffer = ByteArray(chunkSize)
        openStream().use { input ->
            while (true) {
                val bytesRead = input.read(buffer)
                if (bytesRead <= 0) break
                val chunk = buffer.copyOf(bytesRead)
                block(chunk)
            }
        }
    }
}
