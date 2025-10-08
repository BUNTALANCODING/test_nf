package common

import android.graphics.Bitmap
import android.util.Base64
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import io.ktor.util.encodeBase64
import java.io.ByteArrayOutputStream

actual fun ByteArray.toBase64(): String {
    val bitmap: Bitmap = this.decodeToImageBitmap().asAndroidBitmap()
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return byteArray.encodeBase64()
}

actual fun encodeImageBitmapFromPixels(width: Int, height: Int, pixels: IntArray, quality: Int): String {
    val bitmap = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888)
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, quality, stream)
    val byteArray = stream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.NO_WRAP)
}