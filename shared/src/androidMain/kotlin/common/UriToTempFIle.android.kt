package common

import ContextProvider
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual suspend fun copyUriToTempFile(uri: String): String {
   /* val context = ContextProvider.context // global context dari androidApp
    val parsedUri = uri.toUri()
    val inputStream = context.contentResolver.openInputStream(parsedUri)
        ?: throw IllegalArgumentException("Cannot open input stream from URI: $uri")

    val tempFile = withContext(Dispatchers.IO) {
        File.createTempFile("temp_video", ".mp4", context.cacheDir)
    }
    tempFile.outputStream().use { output ->
        inputStream.copyTo(output)
    }
    return tempFile.absolutePath*/

    val context = ContextProvider.context
    val parsedUri = uri.toUri()
    val inputStream = context.contentResolver.openInputStream(parsedUri)
        ?: throw IllegalArgumentException("Cannot open input stream from URI: $uri")

    val tempFile = withContext(Dispatchers.IO) {
        File.createTempFile("temp_video", ".mp4", context.cacheDir)
    }

    try {
        tempFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        return tempFile.absolutePath
    } finally {
        // Menutup input stream secara eksplisit
        withContext(Dispatchers.IO) {
            inputStream.close()
        }
    }
}