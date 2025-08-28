package konkuk.link.bokbookbok.screen.record

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import konkuk.link.bokbookbok.component.common.ModalComponent
import konkuk.link.bokbookbok.component.record.RecordBookComponent
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography

data class BookRecord(
    val id: Int,
    val date: String,
    val title: String,
    val imageUrl: String?,
    val author: String,
)

@Composable
fun RecordScreen(modifier: Modifier = Modifier) {
    // todo : 실제 서버 데이터로 바꿀 것
    val bookList =
        remember {
            List(11) { i ->
                BookRecord(
                    id = i,
                    date = "${8 - (i / 2)}월 첫째주",
                    title = listOf("여행의 이유", "아몬드", "불편한 편의점", "달러구트 꿈 백화점", "오래된 미래")[i % 5],
                    imageUrl = "https://image.aladin.co.kr/product/26/49/cover500/k552736024_1.jpg",
                    author = listOf("김영하", "손원평", "김호연", "이미예", "헬레나 노르베리호지")[i % 5],
                )
            }
        }

    var selectedBook by remember { mutableStateOf<BookRecord?>(null) }

    val gradientBrush =
        Brush.verticalGradient(
            colorStops =
                arrayOf(
                    0.1f to bokBookBokColors.backGroundStart,
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
            verticalAlignment = Alignment.Bottom,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Text(
                    text = "지뿡이 님의",
                    style = defaultBokBookBokTypography.subHeader,
                    color = bokBookBokColors.fontLightGray,
                )
                Text(
                    text = "책, 책, 책",
                    style = defaultBokBookBokTypography.header,
                    color = bokBookBokColors.fontDarkBrown,
                )
            }
            // todo : 머지하고 주석 풀 것
//            ReadingStatusButtonComponent(
//                status = ReadingButtonState.TotalCount(count = 12),
//                onClick = {  }
//            )
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
                        drawRect(brush = gradientBrush)
                    },
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(24.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                items(bookList, key = { it.id }) { book ->
                    RecordBookComponent(
                        date = book.date,
                        title = book.title,
                        bookImageUrl = book.imageUrl,
                        author = book.author,
                        onClick = {
                            selectedBook = book
                        },
                    )
                }
            }
        }
    }
    if (selectedBook != null) {
        ModalComponent(
            onDismissRequest = { selectedBook = null },
            primaryButtonText = "감상평",
            onPrimaryButtonClick = { selectedBook = null },
            secondaryButtonText = "닫기",
            onSecondaryButtonClick = { selectedBook = null },
        ) { modifier ->
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // todo : 실제 독서시간과 해당 컴포넌트로 바꿀 것
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecordScreenPreview() {
    RecordScreen()
}
