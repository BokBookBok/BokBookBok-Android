package konkuk.link.bokbookbok.screen.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import konkuk.link.bokbookbok.R
import konkuk.link.bokbookbok.component.common.PollComponent
import konkuk.link.bokbookbok.component.common.ReviewComponent
import konkuk.link.bokbookbok.component.common.ReviewType
import konkuk.link.bokbookbok.data.model.response.review.BookReviewResponse
import konkuk.link.bokbookbok.screen.review.ReviewHomeViewModel
import konkuk.link.bokbookbok.screen.review.VoteState
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography
import konkuk.link.bokbookbok.util.AppGradientBrush

@Composable
fun RecordDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ReviewHomeViewModel,
    bookId: Int,
    title: String,
    weekLabel: String
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect (key1 = bookId) {
        viewModel.fetchReviews(bookId)
        viewModel.fetchVoteResult(bookId)
    }

    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        uiState.loadErrorMessage != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "오류가 발생했습니다: ${uiState.loadErrorMessage}",
                    style = defaultBokBookBokTypography.body
                )
            }
        }
        uiState.bookReview != null -> {
            RecordDetailScreenContent(
                modifier = modifier,
                navController = navController,
                reviewData = uiState.bookReview!!,
                title = title,
                weekLabel = weekLabel,
                voteState = uiState.voteState,
                onLikeClick = { reviewId -> viewModel.toggleLike(reviewId) },
            )
        }
    }
}

@Composable
private fun RecordDetailScreenContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    reviewData: BookReviewResponse,
    title: String,
    weekLabel: String,
    voteState: VoteState,
    onLikeClick: (Int) -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(bokBookBokColors.white)
                .padding(top = 48.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back_arrow),
                    contentDescription = "Action Icon",
                    modifier = Modifier.size(20.dp),
                    tint = bokBookBokColors.fontLightGray,
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = weekLabel,
                    style = defaultBokBookBokTypography.subHeader,
                    color = bokBookBokColors.fontLightGray,
                )
                Text(
                    text = title,
                    style = defaultBokBookBokTypography.header,
                    color = bokBookBokColors.fontDarkBrown,
                )
            }
        }

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .drawBehind {
                        drawRect(color = bokBookBokColors.backGroundBG)
                        drawRect(brush = AppGradientBrush)
                    },
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .drawBehind {
                        drawRect(color = bokBookBokColors.backGroundBG)
                        drawRect(brush = AppGradientBrush)
                    },
                contentPadding = PaddingValues(start = 28.dp, end = 28.dp, top = 10.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                item {
                    when (voteState) {
                        is VoteState.CanVote -> {
                            PollComponent(
                                question = voteState.voteData.question,
                                option1Text = voteState.voteData.voteResult[0].text,
                                option2Text = voteState.voteData.voteResult[1].text,
                                pollResultData = voteState.voteData,
                                onVote = {}
                            )
                        }

                        is VoteState.Voted -> {
                            PollComponent(
                                question = voteState.voteData.question,
                                option1Text = voteState.voteData.voteResult[0].text,
                                option2Text = voteState.voteData.voteResult[1].text,
                                pollResultData = voteState.voteData,
                                onVote = { },
                            )
                        }

                        is VoteState.NotCreated -> {
                            Text(
                                text = "생성된 투표가 없습니다.",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = TextAlign.Center,
                                style = defaultBokBookBokTypography.body,
                                color = bokBookBokColors.fontLightGray
                            )
                        }

                        is VoteState.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                        }

                        is VoteState.Error -> {
                            Text(
                                text = voteState.message,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
                reviewData.myReview?.let { myReview ->
                    item {
                        ReviewComponent(
                            writer = myReview.nickname,
                            content = myReview.content,
                            type = ReviewType.MYREVIEW,
                            onIconClick = { onLikeClick(myReview.reviewId) },
                        )
                    }
                }

                items(reviewData.otherReviews) { review ->
                    ReviewComponent(
                        writer = review.nickname,
                        content = review.content,
                        type = ReviewType.OTHERS(isLiked = review.liked),
                        onIconClick = { onLikeClick(review.reviewId) },
                    )
                }
            }
        }
    }
}