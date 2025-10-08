package common

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.set
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGColorRenderingIntent
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGDataProviderCreateWithData
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGImageCreate
import platform.Foundation.NSData
import platform.Foundation.base64EncodedStringWithOptions
import platform.Foundation.base64Encoding
import platform.Foundation.create
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePNGRepresentation

actual fun ByteArray.toBase64(): String {
    val uiImage = this.toUIImage()
    val jpegData =  UIImageJPEGRepresentation(uiImage, 0.5)
    return jpegData?.base64Encoding() ?: ""
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun ByteArray.toUIImage(): UIImage{
    val nsData = this.usePinned { pinned ->
        NSData.create(
            bytes = pinned.addressOf(0),
            length = this.size.toULong()
        )
    }
    return UIImage(data = nsData)
}

@OptIn(ExperimentalForeignApi::class)
actual fun encodeImageBitmapFromPixels(width: Int, height: Int, pixels: IntArray, quality: Int): String {
    memScoped {
        val bytesPerPixel = 4
        val bitsPerComponent = 8
        val bytesPerRow = width * bytesPerPixel
        val dataSize = width * height * bytesPerPixel

        val rawData = allocArray<ByteVar>(dataSize)
        for (i in 0 until width * height) {
            val color = pixels[i]
            val r = (color shr 16 and 0xFF).toByte()
            val g = (color shr 8 and 0xFF).toByte()
            val b = (color and 0xFF).toByte()
            val a = (color shr 24 and 0xFF).toByte()
            rawData[i * 4 + 0] = r
            rawData[i * 4 + 1] = g
            rawData[i * 4 + 2] = b
            rawData[i * 4 + 3] = a
        }

        val colorSpace = CGColorSpaceCreateDeviceRGB()
        val provider = CGDataProviderCreateWithData(null, rawData, dataSize.convert(), null)
        val bitmapInfo = CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value

        val cgImage = CGImageCreate(
            width.toULong(),
            height.toULong(),
            bitsPerComponent.convert(),
            (bytesPerPixel * 8).convert(),
            bytesPerRow.convert(),
            colorSpace,
            bitmapInfo.convert(),
            provider,
            null,
            false,
            CGColorRenderingIntent.kCGRenderingIntentDefault
        ) ?: return ""

        val uiImage = UIImage.imageWithCGImage(cgImage)

        // Use UIImagePNGRepresentation (Objective-C interop)
        val pngData: NSData? = UIImagePNGRepresentation(uiImage)
        return pngData?.base64EncodedStringWithOptions(0u) ?: ""
    }
}