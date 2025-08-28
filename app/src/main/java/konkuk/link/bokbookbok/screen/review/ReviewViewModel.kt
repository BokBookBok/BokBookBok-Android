package konkuk.link.bokbookbok.screen.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import konkuk.link.bokbookbok.data.model.request.VoteRequest
import konkuk.link.bokbookbok.data.model.response.BookReviewResponse
import konkuk.link.bokbookbok.data.model.response.CurrentBook
import konkuk.link.bokbookbok.data.model.response.VoteResponse
import konkuk.link.bokbookbok.data.repository.ReviewRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.jvm.java

sealed interface VoteState {
    object Loading : VoteState

    data class Success(
        val voteData: VoteResponse,
    ) : VoteState

    data class Error(
        val message: String,
    ) : VoteState
}

data class ReviewHomeUiState(
    val isLoading: Boolean = true,
    val currentBook: CurrentBook? = null,
    val bookReview: BookReviewResponse? = null,
    val voteState: VoteState = VoteState.Loading,
    val errorMessage: String? = null,
)

class ReviewHomeViewModel(
    private val reviewRepository: ReviewRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReviewHomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _currentBookId = MutableStateFlow<Int?>(null)
    val currentBookId = _currentBookId.value

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val currentBookResult = reviewRepository.getCurrentBook()

            currentBookResult
                .onSuccess { book ->
                    _currentBookId.value = book.id

                    val reviewsResultDeferred = async { reviewRepository.getBookReviews(book.id) }
                    val voteResultDeferred = async { reviewRepository.getVoteResult(book.id) }

                    val reviewsResult = reviewsResultDeferred.await()
                    val voteResult = voteResultDeferred.await()

                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            currentBook = book,
                            bookReview = reviewsResult.getOrNull(),
                            voteState =
                                voteResult.fold(
                                    onSuccess = { VoteState.Success(it) },
                                    onFailure = { VoteState.Error(it.message ?: "투표 정보 로딩 실패") },
                                ),
                            errorMessage = if (reviewsResult.isFailure) reviewsResult.exceptionOrNull()?.message else null,
                        )
                    }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message)
                    }
                }
        }
    }

    fun fetchCurrentBook() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            reviewRepository
                .getCurrentBook()
                .onSuccess { book ->
                    _uiState.update {
                        it.copy(isLoading = false, currentBook = book)
                    }
                    fetchReviews(book.id)
                    fetchVoteResult(book.id)
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message)
                    }
                }
        }
    }

    private fun fetchReviews(bookId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            reviewRepository
                .getBookReviews(bookId)
                .onSuccess { responseData ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            bookReview = responseData,
                        )
                    }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "감상평 목록을 불러오지 못했습니다.",
                        )
                    }
                }
        }
    }

    private fun fetchVoteResult(bookId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(voteState = VoteState.Loading) }
            reviewRepository
                .getVoteResult(bookId)
                .onSuccess { voteData ->
                    _uiState.update { it.copy(voteState = VoteState.Success(voteData)) }
                }.onFailure { error ->
                    _uiState.update { it.copy(voteState = VoteState.Error(error.message ?: "투표 정보 로딩 실패")) }
                }
        }
    }

    fun toggleLike(reviewId: Int) {
        viewModelScope.launch {
            reviewRepository
                .postLike(reviewId)
                .onSuccess { likeResponse ->
                    _uiState.update { currentState ->
                        val currentBookReview = currentState.bookReview ?: return@update currentState
                        val updatedMyReview =
                            if (currentBookReview.myReview?.reviewId == likeResponse.reviewId) {
                                currentBookReview.myReview.copy(
                                    liked = likeResponse.liked,
                                    likeCount = likeResponse.likeCount,
                                )
                            } else {
                                currentBookReview.myReview
                            }
                        val updatedOtherReviews =
                            currentBookReview.otherReviews.map { review ->
                                if (review.reviewId == likeResponse.reviewId) {
                                    review.copy(
                                        liked = likeResponse.liked,
                                        likeCount = likeResponse.likeCount,
                                    )
                                } else {
                                    review
                                }
                            }
                        currentState.copy(
                            bookReview =
                                currentBookReview.copy(
                                    myReview = updatedMyReview,
                                    otherReviews = updatedOtherReviews,
                                ),
                        )
                    }
                }.onFailure { error ->
                    _uiState.update { it.copy(errorMessage = error.message) }
                }
        }
    }

    fun postVote(option: String) {
        val bookId = _currentBookId.value ?: return
        val currentVoteState = _uiState.value.voteState
        if (currentVoteState is VoteState.Loading ||
            (currentVoteState is VoteState.Success && currentVoteState.voteData.myVote != null)
        ) {
            return
        }
        viewModelScope.launch {
            val request = VoteRequest(option = option)
            reviewRepository
                .postVote(bookId, request)
                .onSuccess { updatedVoteData ->
                    _uiState.update { it.copy(voteState = VoteState.Success(updatedVoteData)) }
                }.onFailure { error ->
                    _uiState.update { it.copy(voteState = VoteState.Error(error.message ?: "투표 실패")) }
                }
        }
    }
}

class ReviewHomeViewModelFactory(
    private val reviewRepository: ReviewRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewHomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReviewHomeViewModel(reviewRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
