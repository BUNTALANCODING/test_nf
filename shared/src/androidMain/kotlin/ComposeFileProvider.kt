import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import app.net2software.rampcheck.android.R

import java.io.File
import java.util.Objects

class ComposeFileProvider : FileProvider(
    R.xml.path_provider
) {
    companion object {
        fun getImageUri(context: Context): Uri {
            val tempFile = File.createTempFile(
                "picture_${System.currentTimeMillis()}", ".png", context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ).apply {
                createNewFile()
            }
            val authority = context.applicationContext.packageName + ".provider"
            return getUriForFile(
                Objects.requireNonNull(context),
                authority,
                tempFile,
            )
        }
    }
}