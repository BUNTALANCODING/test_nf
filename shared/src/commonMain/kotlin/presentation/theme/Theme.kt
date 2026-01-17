package presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightScheme = lightColorScheme(
    primary = YellowPrimary,
    onPrimary = TextDark,              // kuning -> teks hitam
    primaryContainer = YellowContainer,
    onPrimaryContainer = TextDark,

    secondary = RedAccent,
    onSecondary = Color.White,

    tertiary = BlueAccent,
    onTertiary = Color.White,

    background = Bg,
    onBackground = TextDark,
    surface = Surface,
    onSurface = TextDark,

    outline = Outline,
)

private val DarkScheme = darkColorScheme(
    primary = YellowPrimary,
    onPrimary = TextDark,
    primaryContainer = Color(0xFF3A2E00),
    onPrimaryContainer = Color(0xFFFFF2C7),

    secondary = RedAccent,
    onSecondary = Color.White,

    background = Color(0xFF0F0F0F),
    onBackground = Color(0xFFEFEFEF),
    surface = Color(0xFF151515),
    onSurface = Color(0xFFEFEFEF),

    outline = Color(0xFF3A3A3A),
)

@Composable
fun TransportTheme(
    dark: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (dark) DarkScheme else LightScheme,
        typography = AppTypography(),
        content = content
    )
}
