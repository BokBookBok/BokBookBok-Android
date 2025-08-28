package konkuk.link.bokbookbok.screen.review

import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import konkuk.link.bokbookbok.component.common.BookInfoCard
import konkuk.link.bokbookbok.component.common.PollComponent
import konkuk.link.bokbookbok.component.common.ReviewComponent
import konkuk.link.bokbookbok.component.common.ReviewType
import konkuk.link.bokbookbok.component.common.WriteFAB
import konkuk.link.bokbookbok.data.model.response.review.BookReviewResponse
import konkuk.link.bokbookbok.navigation.Screen
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography
import kotlinx.coroutines.launch

@Composable
fun ReviewScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ReviewHomeViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    val tabs = listOf("감상평", "투표")

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (uiState.errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = uiState.errorMessage!!)
        }
        return
    }

    Scaffold(
        containerColor = bokBookBokColors.white,
        floatingActionButton = {
            if ((pagerState.currentPage == 0 && uiState.bookReview?.myReview == null) || pagerState.currentPage == 1) {
                WriteFAB(
                    onClick = {
                        navController.navigate(Screen.WriteReview.createRoute(bookId = uiState.currentBook?.id ?: 1))
                    },
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier =
                modifier
                    .background(color = bokBookBokColors.white)
                    .fillMaxSize()
                    .padding(paddingValues),
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
                        )
                    1 ->
                        VoteContent(
                            voteState = uiState.voteState,
                            onVoteSubmit = { option -> viewModel.postVote(option) },
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
) {
    if (bookReview == null) return

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
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
) {
    when (voteState) {
        is VoteState.Loading -> { /* 로딩 UI */ }
        is VoteState.Success -> {
            PollComponent(
                question = voteState.voteData.question,
                option1Text = voteState.voteData.voteResult[0].text,
                option2Text = voteState.voteData.voteResult[1].text,
                pollResultData = voteState.voteData,
                onVote = onVoteSubmit,
            )
        }
        is VoteState.Error -> { /* 에러 UI */ }
    }
}
