package presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun RoundedRectangleBackground(modifier: Modifier = Modifier, backgroundColor: Color = Color.White, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp)) // Clipping for rounded corners
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val cornerRadius = 20.dp.toPx()
            val strokeWidth = 4.dp.toPx()

            // Draw background
            drawRoundRect(
                color = backgroundColor, // Background color
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )

            // Draw dotted border
            drawRoundRect(
                color = Color.Gray.copy(alpha = 0.08F), // Border color
                style = Stroke(
                    width = strokeWidth
                ),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )
        }

        // Content inside the background
        Box(// Ensure content is not too close to the border
        ) {
            content()
        }
    }
}