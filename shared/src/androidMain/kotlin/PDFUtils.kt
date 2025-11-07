import android.content.Context
import android.util.Base64
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun decodeBase64AndSaveToFile(
    context: Context,
    base64String: String,
    fileName: String = "temp_pdf.pdf"
): File? {
    val decodedBytes: ByteArray
    try {
        // Menggunakan android.util.Base64
        decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        return null
    }

    val file = File(context.cacheDir, fileName)

    try {
        FileOutputStream(file).use { fos ->
            fos.write(decodedBytes)
            fos.flush()
        }
        return file
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
}