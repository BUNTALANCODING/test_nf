package common

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toPixelMap

expect fun ByteArray.toBase64():String

fun imageBitmapToBase64(imageBitmap: ImageBitmap, quality: Int = 100): String {
    val safeQuality = quality.coerceIn(0, 100)
    val pixelMap = imageBitmap.toPixelMap()

    return encodeImageBitmapFromPixels(
        width = pixelMap.width,
        height = pixelMap.height,
        pixels = IntArray(pixelMap.width * pixelMap.height) { i ->
            pixelMap[i % pixelMap.width, i / pixelMap.width].toArgb()
        },
        quality = safeQuality
    )
}

expect fun encodeImageBitmapFromPixels(width: Int, height: Int, pixels: IntArray, quality: Int): String