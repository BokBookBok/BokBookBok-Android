package konkuk.link.bokbookbok.screen.review

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import konkuk.link.bokbookbok.R
import konkuk.link.bokbookbok.component.common.BookInfoCard
import konkuk.link.bokbookbok.component.common.PollComponent
import konkuk.link.bokbookbok.component.common.ReviewComponent
import konkuk.link.bokbookbok.component.common.ReviewType
import konkuk.link.bokbookbok.component.common.WriteFAB
import konkuk.link.bokbookbok.data.model.response.review.BookReviewResponse
import konkuk.link.bokbookbok.data.model.response.review.VoteResponse
import konkuk.link.bokbookbok.data.model.response.review.VoteResult
import konkuk.link.bokbookbok.navigation.Screen
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography
import konkuk.link.bokbookbok.util.AppGradientBrush
import konkuk.link.bokbookbok.util.BokToast
import kotlinx.coroutines.launch

@Composable
fun ReviewScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ReviewHomeViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    val tabs = listOf("감상평", "투표")

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ReviewHomeEvent.ShowToast -> {
                    BokToast(context).makeText(
                        message = event.message,
                        lifecycleOwner = lifecycleOwner,
                    )
                }
            }
        }
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        modifier = Modifier.background(color = bokBookBokColors.white),
        floatingActionButton = {
            if (uiState.bookReview?.myReview == null) {
                WriteFAB(
                    onClick = {
                        uiState.currentBook?.id?.let { bookId ->
                            navController.navigate(Screen.WriteReview.createRoute(bookId = bookId))
                        }
                    },
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier =
                modifier
                    .background(color = bokBookBokColors.white)
                    .fillMaxSize(),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(horizontal = 28.dp),
            ) {
                Text(
                    text = "이번주",
                    style = defaultBokBookBokTypography.subHeader,
                    color = bokBookBokColors.fontLightGray,
                )
                Text(
                    text = "감상평",
                    style = defaultBokBookBokTypography.header,
                    color = bokBookBokColors.fontDarkBrown,
                )
            }

            uiState.currentBook?.let {
                Spacer(Modifier.height(20.dp))
                BookInfoCard(
                    modifier = Modifier.padding(horizontal = 28.dp),
                    imageUrl = it.imageUrl,
                    title = it.title,
                    author = it.author,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            ScrollableTabRow(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp),
                containerColor = bokBookBokColors.white,
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier =
                            Modifier
                                .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                                .width(55.dp),
                        color = bokBookBokColors.second,
                    )
                },
                divider = {},
                edgePadding = 0.dp,
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        modifier = Modifier.width(55.dp),
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch { pagerState.animateScrollToPage(index) }
                        },
                        text = {
                            Text(
                                text = title,
                                color = if (pagerState.currentPage == index) bokBookBokColors.second else bokBookBokColors.fontLightGray,
                                style = defaultBokBookBokTypography.subHeader,
                            )
                        },
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                when (page) {
                    0 ->
                        ReviewContent(
                            bookReview = uiState.bookReview,
                            onLikeClick = { reviewId -> viewModel.toggleLike(reviewId) },
                            contentPadding = paddingValues,
                        )
                    1 ->
                        VoteContent(
                            voteState = uiState.voteState,
                            onVoteSubmit = { option -> viewModel.postVote(option) },
                            contentPadding = paddingValues,
                        )
                }
            }
        }
    }
}

@Composable
fun ReviewContent(
    bookReview: BookReviewResponse?,
    onLikeClick: (Int) -> Unit,
    contentPadding: PaddingValues,
) {
    if (bookReview == null) return

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
    ) {
        bookReview.myReview?.let { myReview ->
            item {
                ReviewComponent(
                    writer = myReview.nickname,
                    content = myReview.content,
                    type = ReviewType.MYREVIEW,
                    onIconClick = { onLikeClick(myReview.reviewId) },
                )
            }
        }

        items(bookReview.otherReviews) { review ->
            ReviewComponent(
                writer = review.nickname,
                content = review.content,
                type = ReviewType.OTHERS(isLiked = review.liked),
                onIconClick = { onLikeClick(review.reviewId) },
            )
        }
    }
}

@Composable
fun VoteContent(
    voteState: VoteState,
    onVoteSubmit: (String) -> Unit,
    contentPadding: PaddingValues,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(contentPadding),
        contentAlignment = Alignment.TopCenter,
    ) {
        when (voteState) {
            is VoteState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is VoteState.NotCreated -> {
                VoteNotCreatedView()
            }

            is VoteState.CanVote -> {
                VotePollComponent(
                    question = voteState.voteData.question,
                    option1Text = voteState.voteData.voteResult[0].text,
                    option2Text = voteState.voteData.voteResult[1].text,
                    pollResultData = voteState.voteData,
                    onVote = onVoteSubmit,
                )
            }

            is VoteState.Voted -> {
                VotePollComponent(
                    question = voteState.voteData.question,
                    option1Text = voteState.voteData.voteResult[0].text,
                    option2Text = voteState.voteData.voteResult[1].text,
                    pollResultData = voteState.voteData,
                    onVote = { },
                )
            }

            is VoteState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = voteState.message)
                }
            }
        }
    }
}

@Composable
fun VotePollComponent(
    modifier: Modifier = Modifier,
    question: String,
    option1Text: String,
    option2Text: String,
    pollResultData: VoteResponse?,
    onVote: (option: String) -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(brush = AppGradientBrush)
                .padding(start = 28.dp, end = 28.dp, top = 20.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text =
                "같은 책, 다른 생각. \n" +
                    "지금 당신의 선택은?",
            style = defaultBokBookBokTypography.subHeader,
            color = bokBookBokColors.fontDarkBrown,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(30.dp))
        PollComponent(
            question = question,
            option1Text = option1Text,
            option2Text = option2Text,
            pollResultData = pollResultData,
            onVote = onVote,
        )
    }
}

@Composable
fun VoteNotCreatedView() {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(brush = AppGradientBrush)
                .padding(top = 20.dp, start = 69.dp, end = 69.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier =
                Modifier
                    .border(width = 2.dp, color = bokBookBokColors.second, shape = RoundedCornerShape(10.dp))
                    .padding(horizontal = 30.dp, vertical = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_lock),
                contentDescription = "lock",
                tint = Color.Unspecified,
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "투표가 열리지 않았어요",
                style = defaultBokBookBokTypography.body,
                color = bokBookBokColors.second,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "독후감이 5개 이상 모이면\n투표를 할 수 있어요.",
                style = defaultBokBookBokTypography.body,
                color = bokBookBokColors.fontDarkBrown,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(backgroundColor = 0xFFFFF, showBackground = true)
@Composable
private fun VoteNotCreatedViewPrev() {
    VoteNotCreatedView()
}

@Preview(showBackground = true, name = "투표 전 상태")
@Composable
fun VotePollComponentBeforeVotePreview() {
    val fakeVoteData =
        VoteResponse(
            question = "주인공의 선택에 공감하시나요?",
            voteResult =
                listOf(
                    VoteResult("A", "공감해요", 0, 0.0),
                    VoteResult("B", "공감하지 않아요", 0, 0.0),
                ),
            myVote = null,
        )

    VotePollComponent(
        question = fakeVoteData.question,
        option1Text = fakeVoteData.voteResult[0].text,
        option2Text = fakeVoteData.voteResult[1].text,
        pollResultData = fakeVoteData,
        onVote = {},
    )
}

@Preview(showBackground = true, name = "투표 후 상태")
@Composable
fun VotePollComponentAfterVotePreview() {
    val fakeVoteData =
        VoteResponse(
            question = "주인공의 선택에 공감하시나요?",
            voteResult =
                listOf(
                    VoteResult("A", "공감해요", 65, 65.0),
                    VoteResult("B", "공감하지 않아요", 35, 35.0),
                ),
            myVote = "A",
        )

    VotePollComponent(
        question = fakeVoteData.question,
        option1Text = fakeVoteData.voteResult[0].text,
        option2Text = fakeVoteData.voteResult[1].text,
        pollResultData = fakeVoteData,
        onVote = {},
    )
}
