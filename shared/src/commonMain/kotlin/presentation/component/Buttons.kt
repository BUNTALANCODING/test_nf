package presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import business.core.ProgressBarState
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import presentation.theme.BorderColor
import presentation.theme.ButtonPrimaryColor
import presentation.theme.DarkPrimaryColor
import presentation.theme.DefaultButtonTheme
import presentation.theme.DefaultButtonWithBorderPrimaryTheme
import presentation.theme.DefaultCardColorsTheme
import presentation.theme.PrimaryColor
import presentation.theme.YellowColor

val DEFAULT__BUTTON_SIZE = 50.dp
val DEFAULT__BUTTON_SIZE_EXTRA = 60.dp

@Composable
fun CircleButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onClick: () -> Unit,
    backgroundColor: Color = Color.White.copy(alpha = 0.25f),
    iconColor: Color = Color.White
) {
    Card(
        modifier = modifier.size(50.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, BorderColor),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector, contentDescription = null, tint = iconColor)
        }
    }
}

@Composable
fun ButtonLoading(
    modifier: Modifier = Modifier,
//    progressBarState: ProgressBarState,
    onClick: () -> Unit,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = PrimaryColor
    ), // <- BEFORE colors: ButtonColors = ButtonDefaults.buttonColors()
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        enabled = enabled,
        modifier = modifier,
        interactionSource = interactionSource,
        elevation = elevation,
        shape = shape,
        border = border,
        colors = colors,
        contentPadding = contentPadding,
        onClick = onClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

//            AnimatedVisibility(visible = (progressBarState == ProgressBarState.ButtonLoading || progressBarState == ProgressBarState.FullScreenLoading)) {
//                CircularProgressIndicator(
//                    modifier = Modifier
//                        .size(25.dp),
//                    strokeWidth = 2.dp,
//                    color = if (enabled) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
//                )
//            }

            content()
        }
    }
}

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
//    progressBarState: ProgressBarState = ProgressBarState.Idle,
    enabled: Boolean = true,
    enableElevation: Boolean = false,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    shape: Shape = MaterialTheme.shapes.small,
    text: String,
    onClick: () -> Unit,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = PrimaryColor
    )
) {
    ButtonLoading(
        enabled = enabled,
        modifier = modifier,
        elevation = if (enableElevation) ButtonDefaults.buttonElevation() else ButtonDefaults.buttonElevation(
            0.dp
        ),
        colors = colors,
        /*colors = if (enabled) DefaultButtonTheme() else DefaultButtonWithBorderPrimaryTheme(),*//**/
        /*border = BorderStroke(
            1.dp,
            ButtonPrimaryColor
        ),*/
        shape = shape,
        onClick = onClick,
//        progressBarState = progressBarState,
    ) {
        Text(
            text = text,
            style = style,
        )
    }
}

@Composable
fun SegmentedButtonListrik(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(40.dp)
            .width(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) YellowColor else DarkPrimaryColor)
            .border(1.dp, Color(0xFF3F51B5), RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) Color.Black else Color.White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}


@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    icon: ImageVector,
    text: String,
    color: ButtonColors = DefaultButtonTheme(),
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        colors = color,
        shape = shape,
        onClick = onClick
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.background)
        Spacer_4dp()
        Text(
            text = text,
            style = style,
        )
    }
}

@Composable
fun IconDrawableButton(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    icon: DrawableResource,
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        colors = DefaultButtonTheme(),
        shape = shape,
        onClick = onClick
    ) {
        Icon(painter = painterResource(icon), null, tint = MaterialTheme.colorScheme.background)
        Spacer_4dp()
        Text(
            text = text,
            style = style,
        )
    }
}

@Composable
fun ButtonVerticalSection(positiveButtonLabel: String, negativeButtonLabel: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        DefaultButton(
            modifier = Modifier.width(258.dp).height(DEFAULT__BUTTON_SIZE),
            enabled = true,
            text = positiveButtonLabel,
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryColor,
                contentColor = Color.White
            ),
        )
        Spacer_16dp()
        DefaultButton(
            modifier = Modifier.width(258.dp).height(DEFAULT__BUTTON_SIZE),
            enabled = true,
            text = negativeButtonLabel,
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = Color.White
            ),
        )
    }
}