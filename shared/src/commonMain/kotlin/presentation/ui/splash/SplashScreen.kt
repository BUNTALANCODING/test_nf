package presentation.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import navbuss.shared.generated.resources.Res
import navbuss.shared.generated.resources.ic_city
import navbuss.shared.generated.resources.ic_splash
import org.jetbrains.compose.resources.painterResource

@Composable
fun SplashScreen() {
    val yellow = Color(0xFFE3AF3E)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(yellow),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(Res.drawable.ic_splash),
            contentDescription = "App Logo",
            modifier = Modifier.size(200.dp)
        )

        Image(
            painter = painterResource(Res.drawable.ic_city),
            contentDescription = "City Silhouette",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(140.dp)
        )
    }
}
