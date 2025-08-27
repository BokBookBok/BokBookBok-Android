package konkuk.link.bokbookbok.util

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.topSemiCircleShadow(
    elevation: Dp = 16.dp,
    shadowColor: Color = Color.Black.copy(alpha = 0.15f),
): Modifier =
    this.drawBehind {
        val shadowBlurRadius = elevation.toPx()

        val paint =
            Paint().apply {
                isAntiAlias = true
                color = shadowColor.toArgb()
                maskFilter = (BlurMaskFilter(shadowBlurRadius, BlurMaskFilter.Blur.NORMAL))
            }

        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawArc(
                RectF(0f, 0f, this.size.width, this.size.height),
                180f,
                180f,
                false,
                paint,
            )
        }
    }
