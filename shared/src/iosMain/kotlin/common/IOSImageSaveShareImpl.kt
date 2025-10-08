package common

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGBitmapContextCreate
import platform.CoreGraphics.CGBitmapContextCreateImage
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGContextDrawImage
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.CoreGraphics.kCGBitmapByteOrder32Big
import platform.Foundation.NSArray
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.firstObject
import platform.Foundation.writeToURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetCurrentContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.UIKit.UIImageWriteToSavedPhotosAlbum
import platform.UIKit.UIScreen

/**
 * Implementation of the `ImageSaveShare` interface for saving and sharing images on iOS devices.
 *
 * This class provides methods to save images to the Photos library and the app's Documents directory,
 * as well as to share them using iOS's sharing mechanism.
 */
class IOSImageSaveShareImpl : ImageSaveShare {

    /**
     * Saves an [ImageBitmap] to the iOS Photos library and the app's Documents directory.
     *
     * @param imageBitmap The [ImageBitmap] to be saved.
     * @param fileName The name of the file to save the image under in the Documents directory.
     * @return `true` if the image was successfully saved, `false` otherwise.
     *
     * This method attempts to save the image to the Photos library and also
     * saves it to the app's Documents directory as a PNG file. If successful,
     * it writes the image data to the specified file path.
     * Any exceptions during this process will be caught and logged.
     */
    @OptIn(ExperimentalForeignApi::class)
    override suspend fun saveImage(imageBitmap: ImageBitmap, fileName: String): Boolean {
        try {
            val uiImage = imageBitmap.toUIImage()
            // Save to Photos
            uiImage?.let {
                UIImageWriteToSavedPhotosAlbum(
                    it,
                    null, null, null
                )
            }

            // Optional: Save to Documents Directory
            val imageData = uiImage?.let { UIImagePNGRepresentation(it) } ?: return false
            val fileManager = NSFileManager.defaultManager
            val documentsDirectories = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask) as NSArray
            val documentsDirectory = documentsDirectories.firstObject as NSURL// Get the first NSURL in the array
            val fileURL = NSURL.fileURLWithPath(documentsDirectory.path + "/$fileName")
            return imageData.writeToURL(fileURL, true)
        }catch (e:Exception){
            print("E ${e.message}")
            return false
        }

    }
    /**
     * Shares an [ImageBitmap] after saving it to the iOS Photos library and Documents directory.
     *
     * @param imageBitmap The [ImageBitmap] to be shared.
     * @param fileName The name of the file to save and share the image under.
     * @return `true` if the image was successfully shared, `false` otherwise.
     *
     * This method first attempts to save the image using the `saveImage` method. If successful,
     * it constructs a share intent using iOS's sharing mechanism, allowing the user to share the image
     * with other applications.
     */
    override suspend fun shareImage(imageBitmap: ImageBitmap, fileName: String): Boolean {
        // First, save the image
        if (!saveImage(imageBitmap, fileName)) {
            return false
        }
        val fileManager = NSFileManager.defaultManager
        val documentsDirectories = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask) as NSArray
        val documentsDirectory = documentsDirectories.firstObject as NSURL// Get the first NSURL in the array
        // Now, share the image
        val fileURL = NSURL.fileURLWithPath(documentsDirectory.path + "/$fileName")

        val activityViewController = UIActivityViewController(activityItems = listOf(fileURL), applicationActivities = null)
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(activityViewController, true, null)

        return true
    }


    /**
     * Converts an [ImageBitmap] to a [UIImage].
     *
     * @return A [UIImage] representation of the [ImageBitmap], or `null` if the conversion fails.
     *
     * This extension function creates a new graphics context, draws the image pixels onto it, and
     * generates a [UIImage] that can be saved or shared. It handles the conversion from the
     * [ImageBitmap] pixel data to a format compatible with iOS.
     */
    @OptIn(ExperimentalForeignApi::class)
    fun ImageBitmap.toUIImage(): UIImage? {
        // Create a context with the correct width and height
        val width = this.width.toDouble()
        val height = this.height.toDouble()

        // Begin a new image context
        UIGraphicsBeginImageContextWithOptions(
            CGSizeMake(width, height),
            false, UIScreen.mainScreen.scale
        )

        // Get the current context (CGContext)
        val context = UIGraphicsGetCurrentContext() ?: return null

        // Draw the ImageBitmap onto the CGContext
        val buffer = IntArray(width.toInt() * height.toInt())
        this.readPixels(buffer)
        val bitmapInfo =
            CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value or kCGBitmapByteOrder32Big

        // Fill in the CGContext with pixel data from ImageBitmap
        buffer.usePinned { pinnedArray ->
            val colorSpace = CGColorSpaceCreateDeviceRGB()
            val bitmapContext = CGBitmapContextCreate(
                pinnedArray.addressOf(0),
                width.toInt().convert(),
                height.toInt().convert(),
                8.toULong(),
                (width.toInt() * 4).toULong(),
                colorSpace,
                bitmapInfo
            )

            val cgImage = CGBitmapContextCreateImage(bitmapContext)
            CGContextDrawImage(context, CGRectMake(0.0, 0.0, width, height), cgImage)
        }

        // Create a UIImage from the current image context
        val uiImage = UIGraphicsGetImageFromCurrentImageContext()

        // End the image context
        UIGraphicsEndImageContext()

        return uiImage
    }


}