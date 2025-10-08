package presentation.ui.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_frame_decor
import presentation.theme.GradientSplash
import presentation.theme.gradientSamsatCeriaVertical
import rampcheck.shared.generated.resources.blur_red
import rampcheck.shared.generated.resources.blur_yellow
import rampcheck.shared.generated.resources.ic_logo
import kotlin.math.min
import kotlin.math.sqrt

@Composable
fun SplashScreen() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val density = LocalDensity.current
        val screenWidthPx = with(density) { maxWidth.toPx() }
        val screenHeightPx = with(density) { maxHeight.toPx() }

        // Calculate responsive dimensions
        val dimensions = remember(maxWidth, maxHeight) {
            calculateResponsiveDimensions(maxWidth, maxHeight)
        }

        // Calculate circle radius for animation
        val circleRadius = remember(screenWidthPx, screenHeightPx) {
            sqrt(screenWidthPx * screenWidthPx + screenHeightPx * screenHeightPx) * 0.7f
        }

        val showContent = produceState(initialValue = false) {
            delay(800)
            value = true
        }

        // Animated background circle
//        AnimatedCircleBackground(
//            modifier = Modifier.fillMaxSize(),
//            radiusEnd = circleRadius,
//            color = MaterialTheme.colorScheme.primary,
//        )

        // Main content
        Box(
            modifier = Modifier.fillMaxSize().background(gradientSamsatCeriaVertical),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(Res.drawable.blur_yellow),
                contentDescription = "decoration",
                modifier = Modifier.align(Alignment.TopEnd). blur(187.dp),
                contentScale = ContentScale.Crop
            )

            Image(
                painter = painterResource(Res.drawable.blur_red),
                contentDescription = "decoration",
                modifier = Modifier.align(Alignment.BottomStart). blur(187.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(dimensions.contentPadding)
            ) {
                AnimatedVisibility(
                    visible = showContent.value,
                    enter = fadeIn(animationSpec = tween(600)),
                    exit = fadeOut(animationSpec = tween(300))
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_logo),
                        contentDescription = "App Logo",
                    )
                }

                Spacer(modifier = Modifier.height(dimensions.spacerHeight))
            }
        }

        // Progress indicator
//        CircularProgressIndicator(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(bottom = dimensions.bottomPadding)
//                .size(dimensions.progressSize),
//            color = Color.White.copy(alpha = 0.8f),
//            strokeWidth = dimensions.progressStrokeWidth
//        )
    }
}

@Composable
private fun AnimatedCircleBackground(
    modifier: Modifier = Modifier,
    color: Color,
    radiusEnd: Float
) {
    val animationProgress = produceState(initialValue = 0f) {
        delay(300)
        value = 1f
    }

    val animatedRadius by animateFloatAsState(
        targetValue = if (animationProgress.value > 0f) radiusEnd else 0f,
        animationSpec = keyframes {
            durationMillis = 1000
            0f at 0
            radiusEnd * 0.2f at 200
            radiusEnd * 0.5f at 500
            radiusEnd * 0.8f at 800
            radiusEnd at 1000
        }
    )

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2f, size.height * 0.3f)

        drawCircle(
            color = color,
            radius = animatedRadius,
            center = center
        )
    }
}

// Data class to hold responsive dimensions
private data class ResponsiveDimensions(
    val logoSize: Dp,
    val progressSize: Dp,
    val progressStrokeWidth: Dp,
    val contentPadding: Dp,
    val bottomPadding: Dp,
    val spacerHeight: Dp
)

// Calculate responsive dimensions based on screen size
private fun calculateResponsiveDimensions(
    screenWidth: Dp,
    screenHeight: Dp
): ResponsiveDimensions {
    val minDimension = min(screenWidth.value, screenHeight.value)
    val isTablet = minDimension > 600
    val isLargeScreen = minDimension > 840

    return when {
        isLargeScreen -> ResponsiveDimensions(
            logoSize = (minDimension * 0.15f).dp.coerceIn(120.dp, 200.dp),
            progressSize = 48.dp,
            progressStrokeWidth = 4.dp,
            contentPadding = 48.dp,
            bottomPadding = 64.dp,
            spacerHeight = 48.dp
        )
        isTablet -> ResponsiveDimensions(
            logoSize = (minDimension * 0.2f).dp.coerceIn(100.dp, 160.dp),
            progressSize = 40.dp,
            progressStrokeWidth = 3.dp,
            contentPadding = 32.dp,
            bottomPadding = 48.dp,
            spacerHeight = 32.dp
        )
        else -> ResponsiveDimensions(
            logoSize = (minDimension * 0.25f).dp.coerceIn(80.dp, 120.dp),
            progressSize = 32.dp,
            progressStrokeWidth = 3.dp,
            contentPadding = 24.dp,
            bottomPadding = 32.dp,
            spacerHeight = 24.dp
        )
    }
}

// Alternative responsive splash screen with window size classes
@Composable
fun ResponsiveSplashScreen() {
    BoxWithConstraints {
        val windowSizeClass = calculateWindowSizeClass(maxWidth, maxHeight)

        when (windowSizeClass) {
            WindowSizeClass.Compact -> CompactSplashScreen()
            WindowSizeClass.Medium -> MediumSplashScreen()
            WindowSizeClass.Expanded -> ExpandedSplashScreen()
        }
    }
}

@Composable
private fun CompactSplashScreen() {
    SplashScreenLayout(
        logoSize = 80.dp,
        progressSize = 28.dp,
        padding = 16.dp,
        bottomPadding = 24.dp
    )
}

@Composable
private fun MediumSplashScreen() {
    SplashScreenLayout(
        logoSize = 120.dp,
        progressSize = 36.dp,
        padding = 24.dp,
        bottomPadding = 40.dp
    )
}

@Composable
private fun ExpandedSplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Center content in a constrained width for desktop
        Box(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            SplashScreenLayout(
                logoSize = 160.dp,
                progressSize = 44.dp,
                padding = 32.dp,
                bottomPadding = 56.dp
            )
        }
    }
}

@Composable
private fun SplashScreenLayout(
    logoSize: Dp,
    progressSize: Dp,
    padding: Dp,
    bottomPadding: Dp
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val density = LocalDensity.current
        val screenWidthPx = with(density) { maxWidth.toPx() }
        val screenHeightPx = with(density) { maxHeight.toPx() }

        val circleRadius = remember(screenWidthPx, screenHeightPx) {
            sqrt(screenWidthPx * screenWidthPx + screenHeightPx * screenHeightPx) * 0.7f
        }

        val showContent = produceState(initialValue = false) {
            delay(800)
            value = true
        }

        // Background circle
        AnimatedCircleBackground(
            modifier = Modifier.fillMaxSize(),
            radiusEnd = circleRadius,
            color = MaterialTheme.colorScheme.primary
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = showContent.value,
                enter = fadeIn(animationSpec = tween(600)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_logo),
                    contentDescription = "App Logo",
                    tint = Color.White,
                    modifier = Modifier.size(logoSize)
                )
            }
        }

        // Progress indicator
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = bottomPadding)
                .size(progressSize),
            color = Color.White.copy(alpha = 0.8f),
            strokeWidth = (progressSize.value * 0.08f).dp
        )
    }
}

// Window size class enum
private enum class WindowSizeClass {
    Compact,    // < 600dp
    Medium,     // 600dp - 840dp
    Expanded    // > 840dp
}

// Calculate window size class
private fun calculateWindowSizeClass(width: Dp, height: Dp): WindowSizeClass {
    val minDimension = min(width.value, height.value)
    return when {
        minDimension < 600 -> WindowSizeClass.Compact
        minDimension < 840 -> WindowSizeClass.Medium
        else -> WindowSizeClass.Expanded
    }
}

// Utility composable for responsive spacing
@Composable
fun ResponsiveSpacer(
    compactSize: Dp = 16.dp,
    mediumSize: Dp = 24.dp,
    expandedSize: Dp = 32.dp
) {
    BoxWithConstraints {
        val windowSizeClass = calculateWindowSizeClass(maxWidth, maxHeight)
        val spacerSize = when (windowSizeClass) {
            WindowSizeClass.Compact -> compactSize
            WindowSizeClass.Medium -> mediumSize
            WindowSizeClass.Expanded -> expandedSize
        }
        Spacer(modifier = Modifier.height(spacerSize))
    }
}

// Extension function for responsive modifiers
fun Modifier.responsiveSize(
    compactSize: Dp,
    mediumSize: Dp,
    expandedSize: Dp
): Modifier = this.then(
    Modifier.size(compactSize) // Default fallback
)
