package konkuk.link.bokbookbok.screen.reading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import konkuk.link.bokbookbok.component.common.ModalComponent
import konkuk.link.bokbookbok.component.common.ModalContentData
import konkuk.link.bokbookbok.component.common.ReviewComponent
import konkuk.link.bokbookbok.component.common.ReviewType
import konkuk.link.bokbookbok.component.reading.ReadingStatus
import konkuk.link.bokbookbok.component.reading.ReadingStatusButtonComponent
import konkuk.link.bokbookbok.component.reading.wongoji.WonGoJiBoard
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography

@Composable
fun ReadingScreen(modifier: Modifier = Modifier) {
    var showModal by remember { mutableStateOf(false) }
    var currentStatus by remember { mutableStateOf<ReadingStatus>(ReadingStatus.BEFORE_READING) }

    val gradientBrush =
        Brush.verticalGradient(
            colorStops =
                arrayOf(
                    0.3f to bokBookBokColors.backGroundStart,
                    1.0f to bokBookBokColors.backGroundEnd,
                ),
        )

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(bokBookBokColors.white)
                .padding(top = 48.dp, start = 28.dp, end = 28.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "이번주",
                    style = defaultBokBookBokTypography.subHeader,
                    color = bokBookBokColors.fontLightGray
                )
                Text(
                    text = "선정도서",
                    style = defaultBokBookBokTypography.header,
                    color = bokBookBokColors.fontDarkBrown
                )
            }
            ReadingStatusButtonComponent(
                status = currentStatus,
                onClick = {
                    showModal = true
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .drawBehind {
                    drawRect(color = bokBookBokColors.backGroundBG)
                    drawRect(brush = gradientBrush)
                }
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WonGoJiBoard(text = "괭이부리말아이들")
            Text(
                text = "김중미",
                style = defaultBokBookBokTypography.body,
                color = bokBookBokColors.fontLightGray,
                textAlign = TextAlign.Center
            )
            // todo : 서버 uri로 AsyncImage로 바꿔야 함
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Image Area",
                    style = defaultBokBookBokTypography.body,
                    color = Color.White
                )
            }
            Text(
                text = "\"세상 모든 아이들이\n" + "동무가 되기를\"",
                style = defaultBokBookBokTypography.body,
                color = bokBookBokColors.fontDarkGray,
                textAlign = TextAlign.Center
            )
        }
        ReviewComponent(
            writer = "독서왕",
            content = "정말 인생 책입니다. 모두가 꼭 읽어봤으면 하는 바람입니다. 감동과 교훈이 있는 최고의 소설!",
            type = ReviewType.BEST,
        )
    }
    if (showModal) {
        val modalContentData = when (currentStatus) {
            ReadingStatus.BEFORE_READING -> ModalContentData(
                title = "읽어보기",
                message = "독서를\n시작하시겠습니까?",
                primaryButtonText = "시작",
                secondaryButtonText = "취소",
                onPrimaryClick = {
                    currentStatus = ReadingStatus.IN_PROGRESS
                    showModal = false
                }
            )
            ReadingStatus.IN_PROGRESS -> ModalContentData(
                title = "독서 완료",
                message = "독서를\n완료하시겠습니까?",
                primaryButtonText = "완료",
                secondaryButtonText = "취소",
                onPrimaryClick = {
                    currentStatus = ReadingStatus.COMPLETED
                    showModal = false
                },
            )
            ReadingStatus.COMPLETED,
            ReadingStatus.REVIEWED,
            is ReadingStatus.TOTAL_BOOKS_READ -> null
        }

        modalContentData?.let { data ->
            ModalComponent (
                onDismissRequest = { showModal = false },
                primaryButtonText = data.primaryButtonText,
                onPrimaryButtonClick = data.onPrimaryClick,
                secondaryButtonText = data.secondaryButtonText,
                onSecondaryButtonClick = { showModal = false },
            ) { modifier ->
                Column(modifier = modifier.fillMaxWidth()) {
                    Text(
                        text = data.title,
                        style = defaultBokBookBokTypography.header,
                        textAlign = TextAlign.Center,
                        color = bokBookBokColors.fontDarkBrown,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = data.message,
                        style = defaultBokBookBokTypography.subHeader,
                        color = bokBookBokColors.fontLightGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReadingScreenPreview() {
    ReadingScreen()
}