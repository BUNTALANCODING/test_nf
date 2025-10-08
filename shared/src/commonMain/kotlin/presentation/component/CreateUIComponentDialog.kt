package presentation.component

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.exit
import rampcheck.shared.generated.resources.ic_checked_figma
import rampcheck.shared.generated.resources.ic_info
import rampcheck.shared.generated.resources.ic_info_fill
import rampcheck.shared.generated.resources.ic_tutup_akun

@Composable
fun CreateUIComponentDialog(
    title: String,
    description: String,
    status: Boolean = true,
    onRemoveHeadFromQueue: () -> Unit
) {

    GenericDialog(
        modifier = Modifier.fillMaxWidth(0.9f),
        title = title,
        description = description,
        status = status,
        onRemoveHeadFromQueue = onRemoveHeadFromQueue
    )

}

@Composable
fun GenericDialog(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    status: Boolean,
    onRemoveHeadFromQueue: () -> Unit,
) {
    CustomAlertDialog(
        onDismissRequest = {
            onRemoveHeadFromQueue()
        },
        modifier = modifier
    ) {

        DialogContentAdvanced(
            title = description,
            description = description,
            isShowIcon = true,
            iconImage = if (!status) Res.drawable.ic_tutup_akun else Res.drawable.ic_checked_figma
        )

    }
}

@Composable
private fun DialogContent(
    modifier: Modifier = Modifier,
    title: String,
    description: String = "",
    positiveAction: String,
    onPositiveClick: () -> Unit,
    negativeAction: String = "",
    onNegativeClick: (() -> Unit)? = null,
    highRisk: Boolean = false,
) {
    Column(
        modifier = modifier.padding(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Medium
        )
        Text(text = description)
        Spacer(modifier = Modifier.height(4.dp))

        if (highRisk && negativeAction.isNotBlank() && onNegativeClick != null) {
            SecondaryButton(onClick = onPositiveClick, text = positiveAction)
            PrimaryButton(onClick = onNegativeClick, text = negativeAction)
        } else if (negativeAction.isNotBlank() && onNegativeClick != null) {
            PrimaryButton(onClick = onPositiveClick, text = positiveAction)
            SecondaryButton(onClick = onNegativeClick, text = negativeAction)
        } else {
            PrimaryButton(onClick = onPositiveClick, text = positiveAction)
        }
    }
}

@Composable
private fun PrimaryButton(onClick: () -> Unit, text: String) {
    DefaultButton(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.small
    )
}

@Composable
private fun SecondaryButton(onClick: () -> Unit, text: String) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

// For native iOS look, use: import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveAlertDialogNative

@Composable
fun CreateUIComponentDialogAdvanced(
    title: String,
    description: String = "",
    onRemoveHeadFromQueue: () -> Unit = {},
    onRemoveHeadFromQueueDelay: () -> Unit = {},
    isShowIcon: Boolean = false,
    iconImage: DrawableResource = Res.drawable.ic_checked_figma,  // Use compose-resources for multiplatform drawables
    isDelay: Boolean = true
) {
    // For multiplatform, use Dialog; on Android, could switch to AlertDialog
    // For iOS native: AdaptiveAlertDialogNative(title = ..., message = description, ...)
    Dialog(
        onDismissRequest = { onRemoveHeadFromQueue() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Card(
                shape = RoundedCornerShape(28.dp),  // Modern rounded corners per Material3
                modifier = Modifier
                    .fillMaxWidth(0.85f)  // Adaptive width for different screen sizes
                    .padding(16.dp)
            ) {
                DialogContentAdvanced(
                    title = title,
                    description = description,
                    isShowIcon = isShowIcon,
                    iconImage = iconImage
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        if (isDelay) {
            delay(2000)
            onRemoveHeadFromQueueDelay()
        }
    }
}

@Composable
private fun DialogContentAdvanced(
    modifier: Modifier = Modifier,
    title: String,
    description: String = "",
    isShowIcon: Boolean = true,
    iconImage: DrawableResource = Res.drawable.ic_checked_figma,
) {
    val configuration = LocalViewConfiguration.current
    Column(
        modifier = modifier.fillMaxWidth().padding(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isShowIcon) {
            Image(
                painter = painterResource(iconImage),
                contentDescription = null,
                modifier = Modifier.size(66.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))  // Reduced spacer for tighter modern layout
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = if (description.isNotEmpty()) 8.dp else 0.dp)
        )
        if (description.isNotEmpty()) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Modern dialog configuration data class for better parameter management
 */
data class DialogConfig(
    val title: String,
    val description: String = "",
    val icon: DrawableResource? = null,
    val iconTint: Color? = null,
    val autoDismissDelay: Long? = 2000L,
    val showCloseButton: Boolean = true,
    val enableBackgroundDismiss: Boolean = true,
    val animationType: DialogAnimationType = DialogAnimationType.SCALE_FADE
)

enum class DialogAnimationType {
    SCALE_FADE,
    SLIDE_UP,
    FADE_ONLY
}

/**
 * Modern, platform-adaptive dialog component
 */
@Composable
fun ModernAdaptiveDialog(
    config: DialogConfig,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Auto-dismiss logic
    LaunchedEffect(config.autoDismissDelay) {
        config.autoDismissDelay?.let {
            delay(it)
            onDismiss()
        }
    }

    // Animation state
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showDialog = true
    }

    AnimatedVisibility(
        visible = showDialog,
        enter = when (config.animationType) {
            DialogAnimationType.SCALE_FADE -> scaleIn(
                initialScale = 0.8f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeIn()
            DialogAnimationType.SLIDE_UP -> slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(300)
            ) + fadeIn()
            DialogAnimationType.FADE_ONLY -> fadeIn(animationSpec = tween(200))
        },
        exit = when (config.animationType) {
            DialogAnimationType.SCALE_FADE -> scaleOut(targetScale = 0.8f) + fadeOut()
            DialogAnimationType.SLIDE_UP -> slideOutVertically(
                targetOffsetY = { it / 2 }
            ) + fadeOut()
            DialogAnimationType.FADE_ONLY -> fadeOut()
        }
    ) {
        Dialog(
            onDismissRequest = {
                if (config.enableBackgroundDismiss) {
                    showDialog = false
                    onDismiss()
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = config.enableBackgroundDismiss,
                dismissOnClickOutside = config.enableBackgroundDismiss,
                usePlatformDefaultWidth = false
            )
        ) {
            DialogContent(
                config = config,
                onClose = {
                    showDialog = false
                    onDismiss()
                },
                modifier = modifier
            )
        }
    }
}

@Composable
private fun DialogContent(
    config: DialogConfig,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.85f)
            .wrapContentHeight(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Close button (optional)
            if (config.showCloseButton) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.exit), // Add your close icon
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Icon with animation
            config.icon?.let { icon ->
                AnimatedIcon(
                    icon = icon,
                    tint = config.iconTint
                )
            }

            // Title
            Text(
                text = config.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Description (if provided)
            if (config.description.isNotEmpty()) {
                Text(
                    text = config.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun AnimatedIcon(
    icon: DrawableResource,
    tint: Color? = null,
    modifier: Modifier = Modifier
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "icon_scale"
    )

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    Box(
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(
                color = tint?.copy(alpha = 0.1f)
                    ?: MaterialTheme.colorScheme.primaryContainer
            ),
            //.scale(scale),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            contentScale = ContentScale.Fit,
            colorFilter = tint?.let {
                androidx.compose.ui.graphics.ColorFilter.tint(it)
            }
        )
    }
}

/**
 * Success Dialog Preset
 */
@Composable
fun SuccessDialog(
    title: String,
    description: String = "",
    onDismiss: () -> Unit
) {
    ModernAdaptiveDialog(
        config = DialogConfig(
            title = title,
            description = description,
            icon = Res.drawable.ic_checked_figma,
            iconTint = Color(0xFF4CAF50),
            autoDismissDelay = 2000L,
            showCloseButton = false,
            animationType = DialogAnimationType.SCALE_FADE
        ),
        onDismiss = onDismiss
    )
}

/**
 * Error Dialog Preset
 */
@Composable
fun ErrorDialog(
    title: String,
    description: String = "",
    onDismiss: () -> Unit
) {
    ModernAdaptiveDialog(
        config = DialogConfig(
            title = title,
            description = description,
            icon = Res.drawable.ic_info_fill, // Add your error icon
            iconTint = Color(0xFFF44336),
            autoDismissDelay = null,
            showCloseButton = true,
            animationType = DialogAnimationType.SLIDE_UP
        ),
        onDismiss = onDismiss
    )
}

/**
 * Info Dialog Preset
 */
@Composable
fun InfoDialog(
    title: String,
    description: String = "",
    onDismiss: () -> Unit
) {
    ModernAdaptiveDialog(
        config = DialogConfig(
            title = title,
            description = description,
            icon = Res.drawable.ic_info, // Add your info icon
            iconTint = MaterialTheme.colorScheme.primary,
            autoDismissDelay = 3000L,
            showCloseButton = true,
            animationType = DialogAnimationType.FADE_ONLY
        ),
        onDismiss = onDismiss
    )
}

// Usage Example:
@Composable
fun ExampleUsage() {
    var showSuccessDialog by remember { mutableStateOf(false) }

    if (showSuccessDialog) {
        SuccessDialog(
            title = "Success!",
            description = "Your action was completed successfully.",
            onDismiss = { showSuccessDialog = false }
        )
    }

    // Or use the flexible configuration
    var showCustomDialog by remember { mutableStateOf(false) }

    if (showCustomDialog) {
        ModernAdaptiveDialog(
            config = DialogConfig(
                title = "Custom Dialog",
                description = "This is a custom configured dialog",
                icon = Res.drawable.ic_checked_figma,
                iconTint = Color.Blue,
                autoDismissDelay = 5000L,
                showCloseButton = true,
                enableBackgroundDismiss = true,
                animationType = DialogAnimationType.SCALE_FADE
            ),
            onDismiss = { showCustomDialog = false }
        )
    }
}
