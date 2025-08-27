package konkuk.link.bokbookbok.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

object BokBookBokTheme {
    val bokBookBokColors: BokBookBokColors
        @Composable
        @ReadOnlyComposable
        get() = LocalBokBookBokColors.current
    val bokBookBokTypography: BokBookBokTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypo.current
}

@Composable
fun ProvideMementoColorsAndTypography(
    bokBookBokColors: BokBookBokColors,
    bokBookBokTypography: BokBookBokTypography,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalBokBookBokColors provides bokBookBokColors,
        LocalTypo provides bokBookBokTypography,
        content = content,
    )
}

@Composable
fun BOKBOOKBOKTheme(
    content: @Composable () -> Unit,
) {
    val bokBookBokColors = bokBookBokColors

    ProvideMementoColorsAndTypography(
        bokBookBokColors = bokBookBokColors,
        bokBookBokTypography = defaultBokBookBokTypography,
    ) {
        MaterialTheme(content = content)
    }
}
