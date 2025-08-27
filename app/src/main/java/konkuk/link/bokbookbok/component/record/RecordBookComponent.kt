package konkuk.link.bokbookbok.component.record

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography

@Composable
fun RecordBookComponent(
    date: String,
    title: String,
    bookImageUrl: String?,
    author: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 2.dp, alignment = Alignment.Top),
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .wrapContentHeight()
            .width(116.dp)
            .clickable(onClick = onClick),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp),
            text = date,
            style = defaultBokBookBokTypography.subBody,
            color = bokBookBokColors.second,
            textAlign = TextAlign.Right,
            overflow = TextOverflow.Ellipsis,
        )
        AsyncImage(
            model = bookImageUrl,
            contentDescription = "$title book cover",
            modifier = Modifier
                .fillMaxWidth()
                .height(158.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            text = title,
            style = defaultBokBookBokTypography.body,
            color = bokBookBokColors.fontDarkBrown,
            textAlign = TextAlign.Left,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = author,
            style = defaultBokBookBokTypography.subBody,
            color = bokBookBokColors.fontDarkGray,
            textAlign = TextAlign.Left,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}