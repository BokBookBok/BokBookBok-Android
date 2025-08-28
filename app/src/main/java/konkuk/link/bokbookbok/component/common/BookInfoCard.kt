package konkuk.link.bokbookbok.component.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography
import konkuk.link.bokbookbok.util.AppGradientBrush

@Composable
fun BookInfoCard(
    modifier: Modifier = Modifier,
    imageUrl: String,
    title: String,
    author: String,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = bokBookBokColors.main,
                    shape = RoundedCornerShape(4.dp),
                ).background(
                    brush = AppGradientBrush,
                    shape = RoundedCornerShape(4.dp),
                ).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "책 표지",
            modifier =
                Modifier
                    .size(width = 70.dp, height = 103.dp)
                    .clip(RoundedCornerShape(3.dp)),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier.padding(start = 10.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = title, color = bokBookBokColors.fontDarkBrown, style = defaultBokBookBokTypography.subHeader)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = author, color = bokBookBokColors.fontLightGray, style = defaultBokBookBokTypography.body)
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun BookInfoCardPreview() {
    BookInfoCard(
        imageUrl = "https://example.com/book_cover.jpg",
        title = "코끼리는 생각하지마",
        author = "조지 레이코프",
    )
}
