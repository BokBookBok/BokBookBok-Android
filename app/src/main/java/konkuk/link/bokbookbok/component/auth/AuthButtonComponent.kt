package konkuk.link.bokbookbok.component.auth

import androidx.compose.foundation.BorderStroke
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

enum class AuthButtonTypeEnum(
    val color: Color,
) {
    BEFORE(bokBookBokColors.second),
    SUCCESS(bokBookBokColors.green),
    FAIL(bokBookBokColors.borderYellow),
}

@Composable
fun AuthButtonComponent(
    buttonText: String,
    buttonType: AuthButtonTypeEnum,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = bokBookBokColors.white),
        border = BorderStroke(1.dp, buttonType.color),
        shape = RoundedCornerShape(8.dp),
        modifier =
            Modifier
                .then(modifier)
                .height(53.dp),
    ) {
        Text(
            text = buttonText,
            style = defaultBokBookBokTypography.body,
            color = buttonType.color,
        )
    }
}
