// androidMain/common/profile/ProfileImage.android.kt
package common.profile

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*

@Composable
actual fun ProfileImage(photoUrl: String?, name: String) {
    var bmp by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    LaunchedEffect(photoUrl) {
        bmp = null
        if (!photoUrl.isNullOrEmpty()) {
            try {
                val client = HttpClient(OkHttp)
                val bytes: ByteArray = client.get(photoUrl).body()
                client.close()

                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (e: Exception) {
                println("ANDROID image load error: ${e.message}")
                bmp = null
            }
        }
    }

    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(Color(0xFFD8EEFF)),
        contentAlignment = Alignment.Center
    ) {
        val b = bmp
        if (b != null) {
            Image(
                bitmap = b.asImageBitmap(),
                contentDescription = "Foto Profil",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = name.take(2).uppercase(),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2B5D87)
            )
        }
    }
}
