package konkuk.link.bokbookbok.component.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

data class ModalContentData(
    val title: String,
    val message: String,
    val primaryButtonText: String,
    val onPrimaryClick: () -> Unit,
    val secondaryButtonText: String,
)

@Composable
fun ModalComponent(
    onDismissRequest: () -> Unit,
    primaryButtonText: String?,
    onPrimaryButtonClick: () -> Unit = {},
    secondaryButtonText: String?,
    onSecondaryButtonClick: () -> Unit = {},
    content: @Composable (Modifier) -> Unit,
) {
    val shape = RoundedCornerShape(size = 15.dp)

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier =
                Modifier
                    .width(336.dp)
                    .height(287.dp)
                    .clip(shape)
                    .border(width = 0.5.dp, color = Color(0xFF767A7A), shape = shape)
                    .background(color = Color.White),
        ) {
            content(
                Modifier
                    .weight(1f)
                    .padding(start = 36.dp, top = 36.dp, end = 36.dp)
                    .fillMaxWidth(),
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 48.dp, end = 48.dp, bottom = 36.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                if (secondaryButtonText != null) {
                    ButtonComponent(
                        buttonText = secondaryButtonText,
                        buttonType = ButtonTypeEnum.LINE,
                        onClick = onSecondaryButtonClick,
                        modifier = Modifier.weight(0.4f),
                    )
                }
                if (primaryButtonText != null) {
                    ButtonComponent(
                        buttonText = primaryButtonText,
                        buttonType = ButtonTypeEnum.FILL,
                        onClick = onPrimaryButtonClick,
                        modifier = Modifier.weight(0.4f),
                    )
                }
            }
        }
    }
}
