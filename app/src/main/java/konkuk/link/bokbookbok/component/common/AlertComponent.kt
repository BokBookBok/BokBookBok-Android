package konkuk.link.bokbookbok.component.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography

@Composable
fun AlertComponent(
    onDismissRequest: () -> Unit,
    title: String,
    text: String,
    confirmButtonText: String,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = title,
                style = defaultBokBookBokTypography.subHeader,
                color = bokBookBokColors.second,
            )
        },
        text = {
            Text(
                text = text,
                style = defaultBokBookBokTypography.body,
                color = bokBookBokColors.fontLightGray,
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismissRequest,
            ) {
                Text(
                    text = confirmButtonText,
                    style = defaultBokBookBokTypography.body,
                    color = bokBookBokColors.second,
                )
            }
        },
        containerColor = bokBookBokColors.white,
    )
}
