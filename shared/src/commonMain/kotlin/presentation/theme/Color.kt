package presentation.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/* Orange */
val Orange = Color( 0xffff9800)
val Orange50 = Color( 0xfffff3e0)
val Orange100 = Color( 0xffffe0b2)
val Orange200 = Color( 0xffffcc80)
val Orange300 = Color( 0xffffb74d)
val Orange400 = Color( 0xffffa726)
val Orange500 = Color( 0xffff9800)
val Orange600 = Color( 0xfffb8c00)
val Orange700 = Color( 0xfff57c00)
val Orange800 = Color( 0xffef6c00)
val Orange900 = Color( 0xffe65100)
val OrangeA100 = Color( 0xffffd180)
val OrangeA200 = Color( 0xffffab40)
val OrangeA400 = Color( 0xffff9100)
val OrangeA700 = Color( 0xffff6d00)

/* Deep Orange */
val DeepOrange = Color( 0xffff5722)
val DeepOrange50 = Color( 0xfffbe9e7)
val DeepOrange100 = Color( 0xffffccbc)
val DeepOrange200 = Color( 0xffffab91)
val DeepOrange300 = Color( 0xffff8a65)
val DeepOrange400 = Color( 0xffff7043)
val DeepOrange500 = Color( 0xffff5722)
val DeepOrange600 = Color( 0xfff4511e)
val DeepOrange700 = Color( 0xffe64a19)
val DeepOrange800 = Color( 0xffd84315)
val DeepOrange900 = Color( 0xffbf360c)
val DeepOrangeA100 = Color( 0xffff9e80)
val DeepOrangeA200 = Color( 0xffff6e40)
val DeepOrangeA400 = Color( 0xffff3d00)
val DeepOrangeA700 = Color( 0xffdd2c00)

val OrangeBorderText = Color(0xFFFFE2D0)

//val PrimaryColor = Color(0xFFFF4747)
val PrimaryColor = Color(0XFF29005E)
val PrimaryVariantColor = Color(0xFFCA3D49)
val PrimaryLighterColor = Color(0xFFFFF4ED) //Background Login Screen
val PrimaryLightColor = Color(0xFFF06E1F) //Background Login Screen
val ButtonPrimaryColor = Color(0xFF0F7F98)
val AccentColor = Color(0xFFC62F79)
val BackgroundContent = Color(0xFFf6f6f6)
val lightSurface =  Color(0xFFf5f5f5)
val BackgroundColor = Color(0xFFF4F4F4)
val GrayButtonColor = Color(0xFF919EAB)
val ColorPrimary300 = Color(0xFFBFDAFF)
val YellowColor = Color(0XFFFFCC00)
val DarkPrimaryColor = Color(0XFF090B57)
val SuccessColor = Color(0XFF00A838)

val LightPurpleColor = Color(0xFFEEE0FF)

val IconBgColor = Color(0XFFEDEDFF)

val GradientSplash = Brush.linearGradient(
    colors = listOf(
        Color(0xFF0D47A1),
        Color(0xFF512DA8)
    ),
)

val RedPinkColor = Color(0xFFFFDFE0)
val Grey = Color(0xFF404649)
val TextGray = Color(0xFF707073)

val iconBg = Color(0xFFEDEDFF)

val PagerDotColor = Color(0xFFC4CDD3)
val IconColorGrey = Grey
val IconLightGrey = Color(0xFF616161)

val BackgroundCustom = Color(0xFFF0F0F0)

val TextFieldColor = Color(0xFFf6f6f6)
val BorderColor = Color(0xFFDBDBDC)
val BorderOptionColor = Color(0xFFEDEDFF)

val splashBackground = Color(0xFF272320)

val grey_050 = Color(0xFFfafafa) // Use with black text
val grey_700 = Color(0xFF616161) // Use with white text

val orange_400 = Color(0xFFffa726) // Use with black text
val bannerUnfocusedIndicatorColor = Color(0xFFADD4FF)
val bannerFocusedIndicatorColor = Color(0xFF2A6CC7)

val redColor = Color(0XFFB32D30)
val BackgroundSamsatCeriaColor = Color(0xFFDADEEE)
val gradientBackground = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFFFDFE0),
        Color(0xFFFFFFFF),
        Color(0xFFD9DAFF)
    )
)

val yellowBackground = Color(0XFFFFAF01)
val BgHeaderHome = Brush.linearGradient(
    colors = listOf(
        Color(0xFF161872),
        Color(0XFF00024C),
    )
)
val gradientSamsatCeriaVertical = Brush.verticalGradient(listOf(Color(0xFFFFDFE0),Color.White, Color(0xFFD9DAFF)))
val gradientSamsatCeriaHorizontal = Brush.horizontalGradient(listOf(Color(0xFFFFDFE0),Color.White, Color(0xFFD9DAFF)))
val gradientMenungguPembayaran = Brush.linearGradient(
    colors = listOf(
        Color(0xFF161872),
        Color(0xFF34378F),
        Color(0xFF090B57)
    )
)

val GradientSaldo = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF3B3EA4),
        Color(0XFF00024C),
    )
)


fun backgroundGradientRadial(
    centerColor: Color = PrimaryLighterColor,
    edgeColor: Color = PrimaryLightColor,
    radius: Float = 700f
): Brush = Brush.radialGradient(
    colors = listOf(centerColor, edgeColor),
    radius = radius
)