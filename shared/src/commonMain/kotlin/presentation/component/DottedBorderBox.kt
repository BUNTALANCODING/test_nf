package presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun DottedBorderBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp)) // Clipping for rounded corners
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val cornerRadius = 20.dp.toPx()
            val strokeWidth = 4.dp.toPx()
            val dashWidth = 10.dp.toPx()
            val dashGap = 10.dp.toPx()

            // Draw background
            drawRoundRect(
                color = Color.White, // Background color
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )

            // Draw dotted border
            drawRoundRect(
                color = Color.Gray.copy(alpha = 0.08F), // Border color
                style = Stroke(
                    width = strokeWidth,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashWidth, dashGap), 0f)
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