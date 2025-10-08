package common

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import logger.Logger
import java.io.InputStream

object BitmapUtils {
    fun getBitmapFromUri(uri: Uri, contentResolver: ContentResolver): android.graphics.Bitmap? {
        var inputStream: InputStream? = null
        return try {
            inputStream = contentResolver.openInputStream(uri)
            val s = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            s
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("getBitmapFromUri Exception: ${e.message}")
            Logger.e("getBitmapFromUri Exception: ${e.localizedMessage}")
            null
        }
    }
}