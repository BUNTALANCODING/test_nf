package presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PinDot(isActive: Boolean) {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .size(16.dp)
            .background(
                color = if (isActive) Color(0xFF1F48D1) else Color(0xFFD3D3D3),
                shape = CircleShape
            )
    )
}
