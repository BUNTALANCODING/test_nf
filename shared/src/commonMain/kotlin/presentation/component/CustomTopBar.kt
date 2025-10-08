package presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_background_bottom_rounded
import rampcheck.shared.generated.resources.ic_bg_rectangle
import rampcheck.shared.generated.resources.ic_edit
import rampcheck.shared.generated.resources.ic_pencil_edit
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.theme.PrimaryLighterColor
import rampcheck.shared.generated.resources.ic_bg_boarding

@Composable
fun CustomTopBarOld(
    title: String,
    subtitle: String,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        val screenWidth = maxWidth
        val isCompact = screenWidth < 600.dp

        // Responsive dimensions
        val topBarHeight = if (isCompact) 80.dp else 96.dp
        val horizontalPadding = if (isCompact) 16.dp else 24.dp
        val verticalPadding = if (isCompact) 12.dp else 16.dp
        val buttonSize = if (isCompact) 32.dp else 36.dp
        val iconSize = if (isCompact) 12.dp else 14.dp

        // Background Image
        Image(
            painter = painterResource(Res.drawable.ic_bg_boarding),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(topBarHeight)
        )

        // Foreground Content
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(topBarHeight)
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Back Button
            CircleButton(
                modifier = Modifier.size(buttonSize),
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                onClick = onBack,
                backgroundColor = Color.White.copy(alpha = 0.25f),
                iconColor = Color.White
            )

            // Title and Subtitle - Flexible width
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = if (isCompact) {
                        MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                    } else {
                        MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    },
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        style = if (isCompact) {
                            MaterialTheme.typography.bodySmall
                        } else {
                            MaterialTheme.typography.bodyMedium
                        },
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            // Edit Button
            ResponsiveEditButton(
                onClick = onEdit,
                isCompact = isCompact,
                buttonHeight = buttonSize,
                iconSize = iconSize
            )
        }
    }
}

@Composable
private fun ResponsiveEditButton(
    onClick: () -> Unit,
    isCompact: Boolean,
    buttonHeight: Dp,
    iconSize: Dp
) {
    if (isCompact) {
        // Compact version - Icon only
        CircleButton(
            modifier = Modifier.size(buttonHeight),
            imageVector = Icons.Default.Edit,
            onClick = onClick,
            backgroundColor = Color(0xFFFFB278),
            iconColor = Color.White
        )
    } else {
        // Full version - Text + Icon
        TextButton(
            onClick = onClick,
            colors = ButtonDefaults.textButtonColors(
                containerColor = Color(0xFFFFB278),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.height(buttonHeight),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = "UBAH",
                style = MaterialTheme.typography.labelMedium,
                fontSize = if (isCompact) 10.sp else 12.sp
            )

            Spacer(modifier = Modifier.width(6.dp))

            Icon(
                painter = painterResource(Res.drawable.ic_pencil_edit),
                contentDescription = "Edit",
                tint = Color.White,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

// Alternative implementation with better layout control
@Composable
fun CustomTopBar(
    title: String,
    subtitle: String,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Box {
            // Background
            TopBarBackground()

            // Content
            TopBarContent(
                title = title,
                subtitle = subtitle,
                onBack = onBack,
                onEdit = onEdit
            )
        }
    }
}

@Composable
private fun TopBarBackground() {
    Image(
        painter = painterResource(Res.drawable.ic_bg_boarding),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    )
}

@Composable
private fun TopBarContent(
    title: String,
    subtitle: String,
    onBack: () -> Unit,
    onEdit: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        val maxContentWidth = maxWidth

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            BackButton(onClick = onBack)

            // Title section - takes remaining space
            TitleSection(
                title = title,
                subtitle = subtitle,
                maxWidth = maxContentWidth - 120.dp, // Reserve space for buttons
                modifier = Modifier.weight(1f)
            )

            // Edit button
            EditButton(onClick = onEdit)
        }
    }
}

@Composable
private fun BackButton(onClick: () -> Unit) {
    CircleButton(
        modifier = Modifier.size(32.dp),
        imageVector = Icons.AutoMirrored.Default.ArrowBack,
        onClick = onClick,
        backgroundColor = Color.White.copy(alpha = 0.25f),
        iconColor = Color.White
    )
}

@Composable
private fun TitleSection(
    title: String,
    subtitle: String,
    maxWidth: Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .widthIn(max = maxWidth)
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if (subtitle.isNotEmpty()) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun EditButton(onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            containerColor = Color(0xFFFFB278),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.height(32.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "UBAH",
            style = MaterialTheme.typography.labelMedium,
            fontSize = 10.sp
        )

        Spacer(modifier = Modifier.width(4.dp))

        Icon(
            painter = painterResource(Res.drawable.ic_pencil_edit),
            contentDescription = "Edit",
            tint = Color.White,
            modifier = Modifier.size(12.dp)
        )
    }
}

// Utility composable for CircleButton if not already defined
@Composable
fun CircleButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    iconColor: Color = MaterialTheme.colorScheme.onPrimary,
    enabled: Boolean = true
) {
    /*IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(16.dp)
        )
    }*/
}

// Extension for responsive spacing
@Composable
fun ResponsiveTopBarSpacer(isCompact: Boolean) {
    Spacer(modifier = Modifier.width(if (isCompact) 8.dp else 12.dp))
}

// Preview composable for testing
@Preview
@Composable
private fun CustomTopBarPreview() {
    MaterialTheme {
        CustomTopBar(
            title = "Jakarta → Batam",
            subtitle = "15 Nov 2024 · Ekonomi",
            onBack = {},
            onEdit = {}
        )
    }
}

@Composable
fun HeaderView(
    title: String,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(Res.drawable.ic_bg_rectangle),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            CircleButton(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterStart),
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                onClick = onBack,
                backgroundColor = Color.White.copy(alpha = 0.25f),
                iconColor = Color.White
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


