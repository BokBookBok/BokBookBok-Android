package konkuk.link.bokbookbok.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import konkuk.link.bokbookbok.R

// Set of Material typography styles to start with
val tstjktb = FontFamily(Font(R.font.tstjktb))
val noonnnu = FontFamily(Font(R.font.noonnu))
val gmarketsans =  FontFamily(Font(R.font.gmarketsans))

val lineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None
)

@Immutable
data class BokBookBokTypography(
    //logo
    val logo: TextStyle,
    val subLogo: TextStyle,

    //body
    val body: TextStyle,
    val subBody: TextStyle,

    //header
    val header: TextStyle,
    val subHeader: TextStyle,
)

val defaultBokBookBokTypography = BokBookBokTypography(
    logo = TextStyle(
        fontFamily = gmarketsans,
        fontSize = 30.sp,
    ),
    subLogo = TextStyle(
        fontFamily = tstjktb,
        fontSize = 20.sp,
    ),
    body = TextStyle(
        fontFamily = noonnnu,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    subBody = TextStyle(
        fontFamily = noonnnu,
        fontSize = 12.sp,
        lineHeight = 20.sp
    ),
    header = TextStyle(
        fontFamily = noonnnu,
        fontSize = 28.sp,
        lineHeight = 30.sp
    ),
    subHeader = TextStyle(
        fontFamily = noonnnu,
        fontSize = 20.sp,
        lineHeight = 30.sp
    ),
)

val LocalTypo = staticCompositionLocalOf { defaultBokBookBokTypography }