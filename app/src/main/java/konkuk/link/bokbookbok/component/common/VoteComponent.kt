package konkuk.link.bokbookbok.component.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import konkuk.link.bokbookbok.data.model.response.review.VoteResponse
import konkuk.link.bokbookbok.data.model.response.review.VoteResult
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography

/**
 * 투표 전체를 감싸는 컴포넌트
 * @param question 질문 텍스트
 * @param option1Text 첫 번째 선택지 텍스트
 * @param option2Text 두 번째 선택지 텍스트
 * @param pollResultData 투표 결과 데이터. null일 경우 투표 전 상태로 표시됩니다.
 * @param onVote 투표 시 호출될 콜백 함수. 선택한 옵션(e.g., "A")을 인자로 받습니다.
 */
@Composable
fun PollComponent(
    question: String,
    option1Text: String,
    option2Text: String,
    pollResultData: VoteResponse?,
    onVote: (option: String) -> Unit,
) {
    val isVoted = pollResultData?.myVote != null
    val myVoteOption = pollResultData?.myVote

    val optionA = pollResultData?.voteResult?.find { it.option == "A" }
    val optionB = pollResultData?.voteResult?.find { it.option == "B" }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(bokBookBokColors.white, shape = RoundedCornerShape(20.dp))
                .border(1.dp, bokBookBokColors.borderLightGray, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 12.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = question,
            style = defaultBokBookBokTypography.body,
            color = bokBookBokColors.fontDarkBrown,
        )

        PollOptionItem(
            text = option1Text,
            percentage = optionA?.percentage ?: 0.0,
            isVoted = isVoted,
            isSelectedOption = myVoteOption == "A",
            onClick = {
                if (!isVoted) {
                    onVote("A")
                }
            },
        )

        PollOptionItem(
            text = option2Text,
            percentage = optionB?.percentage ?: 0.0,
            isVoted = isVoted,
            isSelectedOption = myVoteOption == "B",
            onClick = {
                if (!isVoted) {
                    onVote("B")
                }
            },
        )
    }
}

/**
 * 각 투표 항목을 나타내는 컴포넌트
 * @param text 선택지 텍스트
 * @param percentage 투표율 (0-100)
 * @param isVoted 투표가 진행되었는지 여부
 * @param isSelectedOption 첫 번째 옵션인지 여부 (색상 구분을 위함)
 * @param onClick 클릭 이벤트 콜백
 */
@Composable
private fun PollOptionItem(
    text: String,
    percentage: Double,
    isVoted: Boolean,
    isSelectedOption: Boolean,
    onClick: () -> Unit,
) {
    val animatedPercentage by animateFloatAsState(
        targetValue = if (isVoted) (percentage / 100f).toFloat() else 0f,
        animationSpec = tween(durationMillis = 1000),
    )

    val backgroundColor =
        when {
            !isVoted -> bokBookBokColors.beige
            isSelectedOption -> bokBookBokColors.second
            else -> bokBookBokColors.main
        }

    val textColor =
        when {
            !isVoted -> bokBookBokColors.fontDarkBrown
            isSelectedOption -> bokBookBokColors.white
            else -> bokBookBokColors.fontDarkBrown
        }

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(36.dp)
                .clip(RoundedCornerShape(4.dp))
                .clickable(onClick = onClick)
                .background(bokBookBokColors.beige),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = animatedPercentage)
                    .background(backgroundColor),
        )

        Row(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = text,
                color = textColor,
                style = defaultBokBookBokTypography.subBody,
            )

            if (isVoted) {
                Text(
                    text = "$percentage%",
                    color = bokBookBokColors.fontDarkBrown,
                    style = defaultBokBookBokTypography.subBody,
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, name = "투표 전 상태")
@Composable
fun PollComponentPreview() {
    var pollData by remember { mutableStateOf<VoteResponse?>(null) }

    val question = "명희의 삶은 과연\n올바른 선택으로만 이루어져 있었을까?"
    val option1Text = "가장 적절한 선택이다."
    val option2Text = "그렇지 않다."

    val handleVote = { option: String ->
        pollData =
            VoteResponse(
                question = question,
                voteResult =
                    listOf(
                        VoteResult(option = "A", text = option1Text, count = 83, percentage = 83.0),
                        VoteResult(option = "B", text = option2Text, count = 17, percentage = 17.0),
                    ),
                myVote = option,
            )
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.1f))
                .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        PollComponent(
            question = question,
            option1Text = option1Text,
            option2Text = option2Text,
            pollResultData = pollData,
            onVote = handleVote,
        )
    }
}

@Preview(showBackground = true, widthDp = 360, name = "투표 결과가 바로 보이는 상태")
@Composable
fun PollComponentVotedPreview() {
    val question = "명희의 삶은 과연\n올바른 선택으로만 이루어져 있었을까?"
    val option1Text = "가장 적절한 선택이다."
    val option2Text = "그렇지 않다."

    val initialPollData =
        VoteResponse(
            question = question,
            voteResult =
                listOf(
                    VoteResult(option = "A", text = option1Text, count = 67, percentage = 67.0),
                    VoteResult(option = "B", text = option2Text, count = 33, percentage = 33.0),
                ),
            myVote = "A",
        )

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.1f))
                .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        PollComponent(
            question = question,
            option1Text = option1Text,
            option2Text = option2Text,
            pollResultData = initialPollData,
            onVote = {}, // 이미 투표가 끝난 상태이므로 클릭 이벤트는 비워둠
        )
    }
}
