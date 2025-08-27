package konkuk.link.bokbookbok.util

import androidx.compose.ui.graphics.Brush
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors

val AppGradientBrush =
    Brush.verticalGradient(
        colors =
            listOf(
                bokBookBokColors.white.copy(alpha = 0.1f),
                bokBookBokColors.main.copy(alpha = 0.3f),
            ),
    )
