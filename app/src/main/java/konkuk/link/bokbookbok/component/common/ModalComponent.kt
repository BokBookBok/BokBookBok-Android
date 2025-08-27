package konkuk.link.bokbookbok.component.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography

@Composable
fun ActionModal(
    onDismissRequest: () -> Unit,
    primaryButtonText: String?,
    onPrimaryButtonClick: () -> Unit = {},
    secondaryButtonText: String?,
    onSecondaryButtonClick: () -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    val shape = RoundedCornerShape(size = 15.dp)

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .width(336.dp)
                .height(287.dp)
                .clip(shape)
                .border(width = 0.5.dp, color = Color(0xFF767A7A), shape = shape)
                .background(color = Color.White)
        ) {

            content(
                Modifier
                    .weight(1f)
                    .padding(start = 36.dp, top = 36.dp, end = 36.dp)
                    .fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 48.dp, end = 48.dp, bottom = 36.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                if (secondaryButtonText != null) {
                    ButtonComponent(
                        buttonText = secondaryButtonText,
                        buttonType = ButtonTypeEnum.LINE,
                        onClick = onSecondaryButtonClick,
                        modifier = Modifier.weight(0.4f)
                    )
                }
                if (primaryButtonText != null) {
                    ButtonComponent(
                        buttonText = primaryButtonText,
                        buttonType = ButtonTypeEnum.FILL,
                        onClick = onPrimaryButtonClick,
                        modifier = Modifier.weight(0.4f)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ActionModalPreview() {
    // 배경을 만들어 Dialog가 잘 보이도록 함
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // 실제 사용 시에는 상태(state)로 onDismissRequest를 제어해야 합니다.
        ActionModal(
            onDismissRequest = {},
            primaryButtonText = "시작",
            onPrimaryButtonClick = { /* 시작하기 로직 */ },
            secondaryButtonText = "취소",
            onSecondaryButtonClick = { /* 돌아가기 로직 */ }
        ) { modifier ->
            Column(modifier = modifier.fillMaxWidth()) {
                Text(
                    text = "읽어보기",
                    style = defaultBokBookBokTypography.header,
                    textAlign = TextAlign.Center,
                    color = bokBookBokColors.fontDarkBrown,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "독서를\n" + "시작하시겠습니까?",
                    style = defaultBokBookBokTypography.subHeader,
                    color = bokBookBokColors.fontLightGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}