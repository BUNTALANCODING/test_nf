package presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun LabeledBox(
    icon: DrawableResource? = null,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color(0xFFF2F2F2), shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp), // space between icon and text
            modifier = Modifier.wrapContentWidth() // prevents stretching
        ) {
            if (icon != null) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp),
                    tint = Color.Black
                )
            }

            Text(
                text = label,
                color = Color.Black
            )
            
        }
    }
}


