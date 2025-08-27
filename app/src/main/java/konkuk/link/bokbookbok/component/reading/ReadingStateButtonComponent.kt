package konkuk.link.bokbookbok.component.reading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import konkuk.link.bokbookbok.R
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography

sealed class ReadingStatus {
    object BEFORE_READING : ReadingStatus()         // 독서 전
    object IN_PROGRESS : ReadingStatus()            // 독서 중
    object COMPLETED : ReadingStatus()              // 독서 완료
    object REVIEWED : ReadingStatus()               // 리뷰 완료
    data class TOTAL_BOOKS_READ(val count: Int) : ReadingStatus() // 총 읽은 권수 (데이터 포함)
}

@Composable
fun ReadingStatusButtonComponent(
    status: ReadingStatus,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (titleText, backgroundColor) = when (status) {
        ReadingStatus.BEFORE_READING -> "읽어보기" to bokBookBokColors.green
        ReadingStatus.IN_PROGRESS -> "읽기완료" to bokBookBokColors.second
        ReadingStatus.COMPLETED -> "감상쓰기" to bokBookBokColors.main
        ReadingStatus.REVIEWED -> "감상완료" to bokBookBokColors.blue
        is ReadingStatus.TOTAL_BOOKS_READ -> "총 ${status.count}권" to bokBookBokColors.green
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .width(130.dp)
            .height(38.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(size = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = Color.White
        )
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_book_round),
                contentDescription = titleText,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = titleText,
                style = defaultBokBookBokTypography.body,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}