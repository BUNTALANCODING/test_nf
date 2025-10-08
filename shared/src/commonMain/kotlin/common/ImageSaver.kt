package common

import androidx.compose.ui.graphics.ImageBitmap

expect fun getImageSaveShare(): ImageSaveShare

interface ImageSaveShare {

    suspend fun saveImage(bitmap: ImageBitmap, fileName: String): Boolean

    suspend fun shareImage(bitmap: ImageBitmap, fileName: String): Boolean

}
