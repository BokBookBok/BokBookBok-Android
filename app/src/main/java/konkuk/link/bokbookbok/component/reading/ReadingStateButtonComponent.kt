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
import konkuk.link.bokbookbok.data.model.response.reading.ReadingApiStatus
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography

sealed class ReadingButtonState {
    data class Status(
        val value: ReadingApiStatus,
    ) : ReadingButtonState()

    data class TotalCount(
        val count: Int,
    ) : ReadingButtonState()
}

@Composable
fun ReadingStatusButtonComponent(
    state: ReadingButtonState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (titleText, backgroundColor) =
        when (state) {
            is ReadingButtonState.Status ->
                when (state.value) {
                    ReadingApiStatus.NOT_STARTED -> "읽어보기" to bokBookBokColors.green
                    ReadingApiStatus.READING -> "읽기완료" to bokBookBokColors.second
                    ReadingApiStatus.READ_COMPLETED -> "감상쓰기" to bokBookBokColors.main
                    ReadingApiStatus.REVIEWED -> "감상완료" to bokBookBokColors.blue
                }
            is ReadingButtonState.TotalCount -> "총 ${state.count}권" to bokBookBokColors.green
        }

    Button(
        onClick = onClick,
        modifier =
            modifier
                .width(130.dp)
                .height(38.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(size = 8.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = Color.White,
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_book_round),
                contentDescription = titleText,
                tint = Color.White,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = titleText,
                style = defaultBokBookBokTypography.body,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )
        }
    }
}
