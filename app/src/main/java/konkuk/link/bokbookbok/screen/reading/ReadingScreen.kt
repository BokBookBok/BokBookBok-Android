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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import konkuk.link.bokbookbok.component.common.ModalComponent
import konkuk.link.bokbookbok.component.common.ModalContentData
import konkuk.link.bokbookbok.component.common.ReviewComponent
import konkuk.link.bokbookbok.component.common.ReviewType
import konkuk.link.bokbookbok.component.reading.ReadingButtonState
import konkuk.link.bokbookbok.component.reading.ReadingStatusButtonComponent
import konkuk.link.bokbookbok.component.reading.WonGoJiBoard
import konkuk.link.bokbookbok.data.model.response.reading.ReadingApiStatus
import konkuk.link.bokbookbok.data.model.response.reading.ReadingHomeResponse
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography
import konkuk.link.bokbookbok.util.AppGradientBrush

@Composable
fun ReadingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ReadingViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        uiState.errorMessage != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "오류가 발생했습니다: ${uiState.errorMessage}",
                    style = defaultBokBookBokTypography.body,
                    color = bokBookBokColors.fontDarkGray,
                )
            }
        }
        uiState.homeData != null -> {
            ReadingScreenContent(
                modifier = modifier,
                homeData = uiState.homeData!!,
                userNickname = uiState.userNickname,
                onStartReading = viewModel::startReading,
                onCompleteReading = viewModel::completeReading,
            )
        }
    }
}

@Composable
private fun ReadingScreenContent(
    modifier: Modifier = Modifier,
    homeData: ReadingHomeResponse,
    userNickname: String?,
    onStartReading: () -> Unit,
    onCompleteReading: () -> Unit,
) {
    var showModal by remember { mutableStateOf(false) }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(bokBookBokColors.white)
                .padding(top = 48.dp, start = 28.dp, end = 28.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(text = "이번주", style = defaultBokBookBokTypography.subHeader, color = bokBookBokColors.fontLightGray)
                Text(text = "선정도서", style = defaultBokBookBokTypography.header, color = bokBookBokColors.fontDarkBrown)
            }
            // todo : 감상쓰기 페이지로 navigation 필요
            ReadingStatusButtonComponent(
                state = ReadingButtonState.Status(homeData.status),
                onClick = { showModal = true },
            )
        }
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .drawBehind {
                        drawRect(color = bokBookBokColors.backGroundBG)
                        drawRect(brush = AppGradientBrush)
                    }.padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            WonGoJiBoard(text = homeData.book.title)
            Text(
                text = homeData.book.author,
                style = defaultBokBookBokTypography.body,
                color = bokBookBokColors.fontLightGray,
                textAlign = TextAlign.Center,
            )
            AsyncImage(
                model = homeData.book.imageUrl,
                contentDescription = "${homeData.book.title} 책 표지",
                modifier =
                    Modifier
                        .size(132.dp, 196.dp)
                        .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
            )
            BookStatusDescription(homeData = homeData, nickname = userNickname)
        }

        if (homeData.bestReview != null) {
            ReviewComponent(
                writer = "독서왕",
                content = homeData.bestReview.content,
                type = ReviewType.BEST,
            )
        } else {
            NoBestReviewMessage()
        }
    }

    if (showModal) {
        val modalContentData =
            when (homeData.status) {
                ReadingApiStatus.NOT_STARTED ->
                    ModalContentData(
                        title = "읽어보기",
                        message = "독서를\n시작하시겠습니까?",
                        primaryButtonText = "시작",
                        secondaryButtonText = "취소",
                        onPrimaryClick = {
                            onStartReading()
                            showModal = false
                        },
                    )
                ReadingApiStatus.READING ->
                    ModalContentData(
                        title = "독서 완료",
                        message = "독서를\n완료하시겠습니까?",
                        primaryButtonText = "완료",
                        secondaryButtonText = "취소",
                        onPrimaryClick = {
                            onCompleteReading()
                            showModal = false
                        },
                    )
                ReadingApiStatus.READ_COMPLETED, ReadingApiStatus.REVIEWED -> null
            }
        modalContentData?.let { data ->
            ModalComponent(
                onDismissRequest = { showModal = false },
                primaryButtonText = data.primaryButtonText,
                onPrimaryButtonClick = data.onPrimaryClick,
                secondaryButtonText = data.secondaryButtonText,
                onSecondaryButtonClick = { showModal = false },
            ) { modifier ->
                Column(modifier = modifier.fillMaxWidth()) {
                    Text(text = data.title, style = defaultBokBookBokTypography.header, textAlign = TextAlign.Center, color = bokBookBokColors.fontDarkBrown, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(text = data.message, style = defaultBokBookBokTypography.subHeader, color = bokBookBokColors.fontLightGray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
private fun BookStatusDescription(
    homeData: ReadingHomeResponse,
    nickname: String?,
) {
    when (homeData.status) {
        ReadingApiStatus.NOT_STARTED -> {
            Text(
                text = homeData.book.description ?: "해당 도서에 대한 설명이 준비 중이에요",
                style = defaultBokBookBokTypography.body,
                textAlign = TextAlign.Center,
                color = bokBookBokColors.fontDarkGray,
            )
        }
        ReadingApiStatus.READING -> {
            homeData.record?.let {
                StatusInfoColumn(
                    highlightText = "${it.readingDays}일",
                    normalText = " 째, 독서 중",
                    subText = "다른 복복이들은 ${it.averageDays}일 동안 읽었어요",
                )
            }
        }
        ReadingApiStatus.READ_COMPLETED -> {
            homeData.record?.let {
                StatusInfoColumn(
                    highlightText = "${it.readingDays}일",
                    normalText = " 동안 읽었어요",
                    subText = "감상을 남겨 함께 토론해보아요",
                )
            }
        }
        ReadingApiStatus.REVIEWED -> {
            homeData.myReview?.let {
                StatusInfoColumn(
                    highlightText = nickname ?: "회원",
                    normalText = " 님의 감상",
                    subText = "\"${it.content}\"",
                )
            }
        }
    }
}

@Composable
private fun StatusInfoColumn(
    highlightText: String,
    normalText: String,
    subText: String,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text =
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(color = bokBookBokColors.second),
                    ) {
                        append(highlightText)
                    }
                    append(normalText)
                },
            style = defaultBokBookBokTypography.body,
            textAlign = TextAlign.Center,
            color = bokBookBokColors.fontDarkGray,
        )
        Text(
            text = subText,
            style = defaultBokBookBokTypography.subBody,
            textAlign = TextAlign.Center,
            color = bokBookBokColors.fontLightGray,
        )
    }
}

@Composable
private fun NoBestReviewMessage() {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "아직 등록된 베스트 감상평이 없어요.\n첫 번째 감상을 남겨보는 건 어떨까요?",
            style = defaultBokBookBokTypography.body,
            color = bokBookBokColors.fontLightGray,
            textAlign = TextAlign.Center,
        )
    }
}
