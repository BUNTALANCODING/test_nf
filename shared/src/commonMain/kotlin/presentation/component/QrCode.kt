package presentation.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun QRCodeViewer(qrCode: ImageBitmap) {
    var isLoading by rememberSaveable { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(500)
        isLoading = false
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
            //.padding(vertical = 22.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Color.White)
                //.border(BorderStroke(3.dp, Color.Black))
                .size(250.dp)
        ) {
            if (isLoading) {
                QRCodeShimmer()
                return@Column
            }

            Image(
                bitmap = qrCode,
                contentScale = ContentScale.Fit,
                contentDescription = "QR Code",
                modifier = Modifier.fillMaxSize(1f)
            )
        }
    }
}

@Composable
fun QRCodeShimmer() {
    val shimmerColorShades = listOf(
        Color.Gray.copy(0.5f),
        Color.LightGray.copy(0.2f),
        Color.Gray.copy(0.5f),
    )

    val transition = rememberInfiniteTransition()
    val transitionAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColorShades,
        start = Offset(10f, 10f),
        end = Offset(transitionAnim, transitionAnim)
    )

    Surface(
        color = Color.Transparent,
        modifier = Modifier.alpha(.3f)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(300.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = brush)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxSize(.8f)
                    .background(brush = brush)
            )
        }
    }
}