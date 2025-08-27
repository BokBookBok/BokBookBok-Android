package konkuk.link.bokbookbok.component.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography

enum class ButtonTypeEnum(
    val color: Color,
) {
    LINE(bokBookBokColors.white),
    FILL(bokBookBokColors.main),
}

@Composable
fun ButtonComponent(
    buttonText: String,
    buttonType: ButtonTypeEnum,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = buttonType.color,
                disabledContainerColor = bokBookBokColors.white,
            ),
        border = BorderStroke(1.dp, bokBookBokColors.borderYellow),
        shape = RoundedCornerShape(12.dp),
        modifier =
            Modifier
                .then(modifier)
                .height(52.dp)
                .fillMaxWidth(),
    ) {
        Text(
            text = buttonText,
            style = defaultBokBookBokTypography.body,
            color = bokBookBokColors.fontDarkGray,
        )
    }
}
