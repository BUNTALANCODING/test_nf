package presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

import org.jetbrains.compose.resources.Font
import testcasenf.shared.generated.resources.Res
import testcasenf.shared.generated.resources.plus_jakarta_sans_extra_light
import testcasenf.shared.generated.resources.plus_jakarta_sans_extra_light_italic
import testcasenf.shared.generated.resources.plus_jakarta_sans_light
import testcasenf.shared.generated.resources.plus_jakarta_sans_light_italic
import testcasenf.shared.generated.resources.plus_jakarta_sans_regular
import testcasenf.shared.generated.resources.plus_jakarta_sans_italic
import testcasenf.shared.generated.resources.plus_jakarta_sans_medium
import testcasenf.shared.generated.resources.plus_jakarta_sans_medium_italic
import testcasenf.shared.generated.resources.plus_jakarta_sans_semi_bold
import testcasenf.shared.generated.resources.plus_jakarta_sans_semi_bold_italic
import testcasenf.shared.generated.resources.plus_jakarta_sans_bold
import testcasenf.shared.generated.resources.plus_jakarta_sans_bold_italic
import testcasenf.shared.generated.resources.plus_jakarta_sans_extra_bold
import testcasenf.shared.generated.resources.plus_jakarta_sans_extra_bold_italic


@Composable
fun AppTypography(): Typography {
    val jakarta = FontFamily(
        Font(Res.font.plus_jakarta_sans_extra_light, weight = FontWeight.ExtraLight),
        Font(Res.font.plus_jakarta_sans_extra_light_italic, weight = FontWeight.ExtraLight, style = FontStyle.Italic),

        Font(Res.font.plus_jakarta_sans_light, weight = FontWeight.Light),
        Font(Res.font.plus_jakarta_sans_light_italic, weight = FontWeight.Light, style = FontStyle.Italic),

        Font(Res.font.plus_jakarta_sans_regular, weight = FontWeight.Normal),
        Font(Res.font.plus_jakarta_sans_italic, weight = FontWeight.Normal, style = FontStyle.Italic),

        Font(Res.font.plus_jakarta_sans_medium, weight = FontWeight.Medium),
        Font(Res.font.plus_jakarta_sans_medium_italic, weight = FontWeight.Medium, style = FontStyle.Italic),

        Font(Res.font.plus_jakarta_sans_semi_bold, weight = FontWeight.SemiBold),
        Font(Res.font.plus_jakarta_sans_semi_bold_italic, weight = FontWeight.SemiBold, style = FontStyle.Italic),

        Font(Res.font.plus_jakarta_sans_bold, weight = FontWeight.Bold),
        Font(Res.font.plus_jakarta_sans_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),

        Font(Res.font.plus_jakarta_sans_extra_bold, weight = FontWeight.ExtraBold),
        Font(Res.font.plus_jakarta_sans_extra_bold_italic, weight = FontWeight.ExtraBold, style = FontStyle.Italic),
    )

    val base = Typography()
    return base.copy(
        displayLarge = base.displayLarge.copy(fontFamily = jakarta),
        displayMedium = base.displayMedium.copy(fontFamily = jakarta),
        displaySmall = base.displaySmall.copy(fontFamily = jakarta),

        headlineLarge = base.headlineLarge.copy(fontFamily = jakarta),
        headlineMedium = base.headlineMedium.copy(fontFamily = jakarta),
        headlineSmall = base.headlineSmall.copy(fontFamily = jakarta),

        titleLarge = base.titleLarge.copy(fontFamily = jakarta),
        titleMedium = base.titleMedium.copy(fontFamily = jakarta),
        titleSmall = base.titleSmall.copy(fontFamily = jakarta),

        bodyLarge = base.bodyLarge.copy(fontFamily = jakarta),
        bodyMedium = base.bodyMedium.copy(fontFamily = jakarta),
        bodySmall = base.bodySmall.copy(fontFamily = jakarta),

        labelLarge = base.labelLarge.copy(fontFamily = jakarta),
        labelMedium = base.labelMedium.copy(fontFamily = jakarta),
        labelSmall = base.labelSmall.copy(fontFamily = jakarta),
    )
}
