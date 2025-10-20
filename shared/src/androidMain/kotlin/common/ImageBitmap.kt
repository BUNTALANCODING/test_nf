package common

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.ByteArrayOutputStream

actual fun ImageBitmap.toBytes(): ByteArray {
//    return try {
//        val androidBitmap: Bitmap = this.asAndroidBitmap().copy(Bitmap.Config.ARGB_8888, false)
//        val stream = ByteArrayOutputStream()
//        val success = androidBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//        if (!success) {
//            throw IllegalStateException("Failed to compress bitmap")
//        }
//        stream.toByteArray()
//    } catch (e: Exception) {
//        e.printStackTrace()
//        ByteArray(0)
//    }
    val maxSizeInBytes = 1024 * 1024 // 1 MB limit

    return try {
        val androidBitmap: Bitmap = this.asAndroidBitmap().copy(Bitmap.Config.ARGB_8888, true)

        var quality = 95
        var outputStream = ByteArrayOutputStream()


        androidBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)


        while (outputStream.size() > maxSizeInBytes && quality > 10) {
            quality -= 5
            outputStream.reset()

            androidBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

            println("Kompresi Ulang: Kualitas $quality, Ukuran ${outputStream.size()} bytes")
        }

        if (outputStream.size() > maxSizeInBytes) {
            println("Peringatan: Kompresi gagal mencapai target 1MB. Ukuran akhir: ${outputStream.size()} bytes")
        }

        outputStream.toByteArray()

    } catch (e: Exception) {
        e.printStackTrace()
        ByteArray(0)
    }

//    val androidBitmap: Bitmap = this.asAndroidBitmap()
//    val stream = ByteArrayOutputStream()
//    androidBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//    return stream.toByteArray()
}