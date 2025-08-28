package konkuk.link.bokbookbok.screen.record

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import konkuk.link.bokbookbok.component.common.BookInfoCard
import konkuk.link.bokbookbok.component.common.ModalComponent
import konkuk.link.bokbookbok.component.reading.ReadingButtonState
import konkuk.link.bokbookbok.component.reading.ReadingStatusButtonComponent
import konkuk.link.bokbookbok.component.record.RecordBookComponent
import konkuk.link.bokbookbok.data.model.response.record.Record
import konkuk.link.bokbookbok.data.model.response.record.RecordHomeResponse
import konkuk.link.bokbookbok.navigation.Screen
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography
import konkuk.link.bokbookbok.util.AppGradientBrush

@Composable
fun RecordScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: RecordViewModel,
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
                    style = defaultBokBookBokTypography.body
                )
            }
        }
        uiState.recordData != null -> {
            RecordScreenContent(
                modifier = modifier,
                nickname = uiState.userNickname,
                navController = navController,
                recordData = uiState.recordData!!
            )
        }
    }
}

@Composable
private fun RecordScreenContent(
    modifier: Modifier = Modifier,
    nickname: String?,
    navController: NavController,
    recordData: RecordHomeResponse
) {
    var selectedBook by remember { mutableStateOf<Record?>(null) }

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
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Text(
                    text = "${nickname} 님의",
                    style = defaultBokBookBokTypography.subHeader,
                    color = bokBookBokColors.fontLightGray,
                )
                Text(
                    text = "책, 책, 책",
                    style = defaultBokBookBokTypography.header,
                    color = bokBookBokColors.fontDarkBrown,
                )
            }
            ReadingStatusButtonComponent(
                state = ReadingButtonState.TotalCount(count = recordData.totalCount),
                onClick = {  }
            )
        }

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(width = 0.5.dp, color = bokBookBokColors.borderLightGray, shape = RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .drawBehind {
                        drawRect(color = bokBookBokColors.backGroundBG)
                        drawRect(brush = AppGradientBrush)
                    },
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(24.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                items(recordData.records, key = { it.bookInfoResponse.id }) { record ->
                    RecordBookComponent(
                        date = record.weekLabel,
                        title = record.bookInfoResponse.title,
                        bookImageUrl = record.bookInfoResponse.imageUrl,
                        author = record.bookInfoResponse.author,
                        onClick = {
                            selectedBook = record
                        },
                    )
                }
            }
        }
    }

    if (selectedBook != null) {
        ModalComponent(
            onDismissRequest = { selectedBook = null },
            modifier = Modifier.height(336.dp),
            primaryButtonText = "감상평",
            onPrimaryButtonClick = {
                val bookToNavigate = selectedBook!!
                selectedBook = null
                navController.navigate(
                    Screen.RecordDetail.createRoute(
                        bookId = bookToNavigate.bookInfoResponse.id,
                        title = bookToNavigate.bookInfoResponse.title,
                        weekLabel = bookToNavigate.weekLabel
                    )
                )
            },
            secondaryButtonText = "닫기",
            onSecondaryButtonClick = { selectedBook = null },
        ) { modifier ->
            RecordDetailModalContent(
                modifier = modifier
                    .fillMaxSize(),
                record = selectedBook!!
            )
        }
    }
}

@Composable
private fun RecordDetailModalContent(
    modifier: Modifier = Modifier,
    record: Record
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        BookInfoCard(
            imageUrl = record.bookInfoResponse.imageUrl,
            title = record.bookInfoResponse.title,
            author = record.bookInfoResponse.author
        )
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(color = bokBookBokColors.fontLightGray)
                ) {
                    append("독서 시간  ")
                }
                append("${record.readDays}일")
            },
            style = defaultBokBookBokTypography.body,
            textAlign = TextAlign.Center,
            color = bokBookBokColors.second,
            modifier = Modifier.fillMaxWidth()
        )
    }
}