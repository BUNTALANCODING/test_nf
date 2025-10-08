package presentation.util

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldView(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "placeholder...",
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        singleLine = true,
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(text = placeholder, color = Color.Gray, fontSize = textStyle.fontSize, )
            }
            innerTextField()
        },
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
        modifier = modifier
    )
}

fun Modifier.dashedBorder(
    color: Color,
    shape: Shape,
    width: Dp = 1.dp,
    dashWidth: Dp = 4.dp,
    gapWidth: Dp = 4.dp,
    cap: StrokeCap = StrokeCap.Round
) = this.drawWithContent {
    val outline = shape.createOutline(size, layoutDirection, this)
    val path = Path()
    path.addOutline(outline)
    val stroke = Stroke(
        cap = cap,
        width = width.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashWidth.toPx() + width.toPx(), gapWidth.toPx() + width.toPx()),
            phase = 0f
        )
    )
    this.drawContent()
    drawPath(
        path = path,
        style = stroke,
        color = color
    )
}