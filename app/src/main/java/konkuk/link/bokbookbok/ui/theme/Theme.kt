package konkuk.link.bokbookbok.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView

object BokBookBokTheme {
    val bokBookBokColors: BokBookBokColors
        @Composable
        @ReadOnlyComposable
        get() = LocalBokBookBokColors.current
}

@Composable
fun ProvideMementoColorsAndTypography(
    bokBookBokColors: BokBookBokColors,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalBokBookBokColors provides bokBookBokColors,
        content = content,
    )
}

@Composable
fun BOKBOOKBOKTheme(
    content: @Composable () -> Unit
) {
    val bokBookBokColors = bokBookBokColors

    ProvideMementoColorsAndTypography(
        bokBookBokColors = bokBookBokColors
    ) {
        val view = LocalView.current
        if (!view.isInEditMode) {
            SideEffect {
                val window = (view.context as Activity).window
            }
        }
        MaterialTheme(content = content)
    }
}