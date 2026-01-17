package common.profile

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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.request.*
import org.jetbrains.skia.Image
import androidx.compose.ui.graphics.toComposeImageBitmap

@Composable
actual fun ProfileImage(photoUrl: String?, name: String) {
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(photoUrl) {
        bitmap = null
        if (!photoUrl.isNullOrEmpty()) {
            try {
                val client = HttpClient(Darwin)
                val bytes: ByteArray = client.get(photoUrl).body()
                client.close()

                val skiaImage = Image.makeFromEncoded(bytes)
                bitmap = skiaImage.toComposeImageBitmap()
            } catch (e: Exception) {
                println("🚨 iOS load image error: ${e.message}")
                bitmap = null
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
        val b = bitmap
        if (b != null) {
            Image(
                bitmap = b,
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

