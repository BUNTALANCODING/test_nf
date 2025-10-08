package presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import rampcheck.shared.generated.resources.*
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.lato_black_italic
import rampcheck.shared.generated.resources.lato_bold
import rampcheck.shared.generated.resources.lato_bold_italic
import rampcheck.shared.generated.resources.lato_italic
import rampcheck.shared.generated.resources.lato_light
import rampcheck.shared.generated.resources.lato_light_italic
import rampcheck.shared.generated.resources.lato_regular
import rampcheck.shared.generated.resources.lato_thin
import rampcheck.shared.generated.resources.lato_thin_italic


@Composable
fun CedoraTypography(): Typography {
    val cedora = FontFamily(
        Font(
            resource = Res.font.cedora_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
        Font(
            resource = Res.font.cedora_regular_italic,
            weight = FontWeight.Normal,
            style = FontStyle.Italic
        ),
        Font(
            resource = Res.font.cedora_bold_italic,
            weight = FontWeight.Bold,
            style = FontStyle.Italic
        ),
        Font(
            resource = Res.font.cedora_bold,
            weight = FontWeight.Bold,
            style = FontStyle.Normal
        ),
    )

    return Typography(
        headlineSmall = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            fontFamily = cedora
        ),
        titleLarge = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            fontFamily = cedora
        ),
        bodyLarge = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            fontFamily = cedora
        ),
        bodyMedium = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            fontFamily = cedora
        ),
        labelMedium = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            fontFamily = cedora
        ),
    )
}


@Composable
fun StallionTypography(): Typography {
    val stallion = FontFamily(
        Font(
            resource = Res.font.stallion_beatsides_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
    )

    return Typography(
        headlineSmall = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            fontFamily = stallion
        ),
        titleLarge = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            fontFamily = stallion
        ),
        bodyLarge = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            fontFamily = stallion
        ),
        bodyMedium = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            fontFamily = stallion
        ),
        labelMedium = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            fontFamily = stallion
        ),
    )
}
