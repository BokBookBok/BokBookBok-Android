package konkuk.link.bokbookbok.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
sealed class BokBookBokColorScheme

// Main Color (Dark)
val Main = Color(0xFFFBC94C)
val Second = Color(0xFFEF7173)

// Background (Gradient)
val BackgroundStart = Color(0x1AFFFFFF)
val BackgroundEnd = Color(0x4DFBC94C)

// Colors
val White = Color(0xFFFFFFFF)
val Green = Color(0xFF93B65E)
val Beige = Color(0xFFF8F6EC)
val Blue = Color(0xFF8CBFF5)

// Font
val FontDarkBrown = Color(0xFF1D1500)
val FontLightGray = Color(0xFF979797)
val FontDarkGray = Color(0xFF4C4C4C)

// Border
val BorderLightGray = Color(0xFFD9D9D9)
val BorderDarkGray = Color(0xFF767A7A)
val BorderYellow = Color(0xFFF49E02)

@Immutable
data class BokBookBokColors(
    // Colors
    val main: Color,
    val second: Color,
    val white: Color,
    val green: Color,
    val beige: Color,
    val blue: Color,
    // Background (Gradient)
    val backGroundStart: Color,
    val backGroundEnd: Color,
    val backGroundBG: Color,
    // Fonts
    val fontDarkBrown: Color,
    val fontLightGray: Color,
    val fontDarkGray: Color,
    // Border
    val borderLightGray: Color,
    val borderDarkGray: Color,
    val borderYellow: Color,
) : BokBookBokColorScheme()

val bokBookBokColors =
    BokBookBokColors(
        // Colors
        main = Main,
        second = Second,
        white = White,
        green = Green,
        beige = Beige,
        blue = Blue,
        // Background (Gradient)
        backGroundStart = BackgroundStart,
        backGroundEnd = BackgroundEnd,
        backGroundBG = White,
        // Fonts
        fontDarkBrown = FontDarkBrown,
        fontLightGray = FontLightGray,
        fontDarkGray = FontDarkGray,
        // Border
        borderLightGray = BorderLightGray,
        borderDarkGray = BorderDarkGray,
        borderYellow = BorderYellow,
    )

// Local Colors
val LocalBokBookBokColors =
    staticCompositionLocalOf<BokBookBokColors> {
        error("No ColorsColorScheme provided")
    }
