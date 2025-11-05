package common

import android.net.Uri
import java.io.File

actual suspend fun copyUriToTempFile(uri: String): String {
    val context = ContextProvider.context // global context dari androidApp
    val parsedUri = Uri.parse(uri)
    val inputStream = context.contentResolver.openInputStream(parsedUri)
        ?: throw IllegalArgumentException("Cannot open input stream from URI: $uri")

    val tempFile = File.createTempFile("temp_video", ".mp4", context.cacheDir)
    tempFile.outputStream().use { output ->
        inputStream.copyTo(output)
    }
    return tempFile.absolutePath
}