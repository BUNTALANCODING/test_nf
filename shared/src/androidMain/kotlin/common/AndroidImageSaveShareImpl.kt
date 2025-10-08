package common

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

/**
 * Implementation of the `ImageSaveShare` interface for saving and sharing images on Android devices.
 *
 * @param context The context used for accessing application-specific resources and files.
 *
 * This class provides methods to save images in the app's internal storage and share them using
 * Android's sharing mechanism. The images are saved in a dedicated directory named "QrApps".
 */
class AndroidImageSaveShareImpl(val context: Context) : ImageSaveShare {

    /**
     * Saves an [ImageBitmap] to the internal storage of the app.
     *
     * @param bitmap The [ImageBitmap] to be saved.
     * @param fileName The name of the file to save the image under.
     * @return `true` if the image was successfully saved, `false` otherwise.
     *
     * This method creates a directory named "QrApps" in the app's internal storage if it does not exist,
     * and saves the image as a PNG file. The operation is performed using `FileOutputStream`, and
     * `Dispatchers.IO` is utilized to ensure that this blocking call does not affect the main thread.
     */
    override suspend fun saveImage(bitmap: ImageBitmap, fileName: String): Boolean {
        val androidBitmap = bitmap.asAndroidBitmap()

        val file: File
        if (AndroidQOrBelow()) {
            file = File.createTempFile(
                fileName, ".png", Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES)
            ).apply {
                createNewFile()
            }
        } else {
            file = File.createTempFile(
                fileName, ".png", Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES)
            ).apply {
                createNewFile()
            }
        }

        return try {
            /**
             * While FileOutputStream is a blocking call, using Dispatchers.IO ensures that it doesn't
             * block your main thread or any other threads handling lightweight coroutines.
             * */
            FileOutputStream(file).use { outputStream ->
                androidBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Shares an [ImageBitmap] after saving it to the internal storage.
     *
     * @param bitmap The [ImageBitmap] to be shared.
     * @param fileName The name of the file to save and share the image under.
     * @return `true` if the image was successfully shared, `false` otherwise.
     *
     * This method first attempts to save the image using the `saveImage` method. If successful, it
     * constructs a share intent using Android's sharing mechanism, allowing the user to choose an
     * application to share the image with. It grants read URI permissions for the file being shared.
     */
    override suspend fun shareImage(bitmap: ImageBitmap, fileName: String): Boolean {
        // First, save the image
        if (!saveImage(bitmap, fileName)) {
            return false
        }

        // Now, share the image
        //val file = File(context.filesDir, "cache_images/$fileName")
        val file = File.createTempFile(
            fileName, ".png", Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)
        ).apply {
            createNewFile()
        }

        if (!file.exists()) {
            return false
        }

        val uri: Uri = FileProvider.getUriForFile(context, "${context.applicationContext.packageName}.provider", file)
        //val uri: Uri = ComposeFileProvider.getImageUri(context)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
        return true
    }


}