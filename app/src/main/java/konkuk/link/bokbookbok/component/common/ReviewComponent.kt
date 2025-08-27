package konkuk.link.bokbookbok.component.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import konkuk.link.bokbookbok.R
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography

sealed class ReviewType {
    object BEST : ReviewType()
    object MY_REVIEW : ReviewType()
    data class OTHERS(val isLiked: Boolean) : ReviewType()
}

@Composable
fun ReviewComponent(
    writer: String,
    content: String,
    type: ReviewType,
    modifier: Modifier = Modifier,
    onIconClick: () -> Unit = {}
) {
    val (leftBarColor: Color, @DrawableRes iconResId: Int?, iconColor: Color) = when (type) {
        is ReviewType.BEST -> Triple(bokBookBokColors.main, R.drawable.ic_review_best, bokBookBokColors.main)
        is ReviewType.MY_REVIEW -> Triple(bokBookBokColors.second, null, Color.Unspecified)
        is ReviewType.OTHERS -> {
            if (type.isLiked) {
                Triple(bokBookBokColors.main, R.drawable.ic_review_bookmark_fill, bokBookBokColors.second)
            } else {
                Triple(bokBookBokColors.main, R.drawable.ic_review_bookmark_round, bokBookBokColors.second)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .border(width = 0.5.dp, color = Color(0xFFD9D9D9))
            .background(color = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(8.dp)
                    .background(leftBarColor)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Text(
                    text = writer,
                    style = defaultBokBookBokTypography.body,
                    color = bokBookBokColors.fontLightGray
                )
                Spacer(modifier = Modifier.height(8.dp))

                val scrollState = rememberScrollState()

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        text = content,
                        style = defaultBokBookBokTypography.body,
                        color = bokBookBokColors.fontDarkGray,
                    )
                }
            }
        }

        iconResId?.let {
            IconButton(
                onClick = onIconClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = "Action Icon",
                    tint = iconColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewComponentPreview() {
    Column(modifier = Modifier
        .padding(16.dp)
        .width(380.dp)) {
        ReviewComponent(
            writer = "독서왕",
            content = "정말 인생 책입니다. 모두가 꼭 읽어봤으면 하는 바람입니다. 감동과 교훈이 있는 최고의 소설!",
            type = ReviewType.BEST
        )
        Spacer(modifier = Modifier.height(16.dp))
        ReviewComponent(
            writer = "책읽는펭귄 (긴 글 테스트)",
            content = "이 컴포넌트는 이제 내용의 길이에 따라 높이가 동적으로 변합니다. 이렇게 여러 줄에 걸쳐서 텍스트를 입력해도 레이아웃이 깨지지 않고 자연스럽게 늘어나는 것을 확인할 수 있습니다. 스크롤 없이 모든 내용을 보여줄 때 유용하게 사용할 수 있는 기능입니다. 아이콘 색상도 이제 타입별로 다르게 지정할 수 있죠.",
            type = ReviewType.OTHERS(isLiked = true)
        )
        Spacer(modifier = Modifier.height(16.dp))
        ReviewComponent(
            writer = "나그네",
            content = "음... 저는 조금 아쉬웠어요. 전개가 너무 느린 느낌?",
            type = ReviewType.OTHERS(isLiked = false)
        )
        Spacer(modifier = Modifier.height(16.dp))
        ReviewComponent(
            writer = "나",
            content = "내가 쓴 감상평입니다. 아이콘이 표시되지 않습니다.",
            type = ReviewType.MY_REVIEW
        )
    }
}
