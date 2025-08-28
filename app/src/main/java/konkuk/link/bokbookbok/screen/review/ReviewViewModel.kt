package konkuk.link.bokbookbok.screen.review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import konkuk.link.bokbookbok.data.model.request.review.VoteRequest
import konkuk.link.bokbookbok.data.model.response.reading.ReadingApiStatus
import konkuk.link.bokbookbok.data.model.response.review.BookReviewResponse
import konkuk.link.bokbookbok.data.model.response.review.CurrentBook
import konkuk.link.bokbookbok.data.model.response.review.VoteResponse
import konkuk.link.bokbookbok.data.repository.ReviewRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.jvm.java

sealed interface ReviewHomeEvent {
    data class ShowToast(
        val message: String,
    ) : ReviewHomeEvent
}

sealed interface VoteState {
    object Loading : VoteState

    data class NotCreated(
        val reason: String,
    ) : VoteState

    data class CanVote(
        val voteData: VoteResponse,
    ) : VoteState

    data class Voted(
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
    val loadErrorMessage: String? = null,
    val canVote: Boolean = false,
)

class ReviewHomeViewModel(
    private val reviewRepository: ReviewRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReviewHomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ReviewHomeEvent>()
    val event = _event.asSharedFlow()

    init {
        val bookIdFromNav: Int? = savedStateHandle.get<Int>("bookId")
        if (bookIdFromNav != null && bookIdFromNav != -1) {
            fetchReviews(bookIdFromNav)
            fetchVoteResult(bookIdFromNav)
        } else {
            loadInitialData()
        }
    }

    fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loadErrorMessage = null) }

            reviewRepository
                .getCurrentBook()
                .onSuccess { book ->
                    val bookStatusDeferred = async { reviewRepository.getBookStatus(book.id) }
                    val reviewsDeferred = async { reviewRepository.getBookReviews(book.id) }
                    val voteDeferred = async { reviewRepository.getVoteResult(book.id) }
                    val bookStatusResult = bookStatusDeferred.await()
                    val reviewsResult = reviewsDeferred.await()
                    val voteResult = voteDeferred.await()

                    _uiState.update {
                        val status = bookStatusResult.getOrNull()?.status
                        it.copy(
                            isLoading = false,
                            currentBook = book,
                            bookReview = reviewsResult.getOrNull(),
                            voteState = voteResult.getOrElse { e -> VoteState.Error(e.message ?: "투표 정보 로딩 실패") },
                            loadErrorMessage = if (reviewsResult.isFailure) reviewsResult.exceptionOrNull()?.message else null,
                            canVote = (status == ReadingApiStatus.READ_COMPLETED || status == ReadingApiStatus.REVIEWED),
                        )
                    }
                }.onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, loadErrorMessage = error.message) }
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
                        it.copy(isLoading = false, loadErrorMessage = error.message)
                    }
                }
        }
    }

    fun fetchReviews(bookId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            reviewRepository
                .getBookReviews(bookId)
                .onSuccess { responseData ->
                    _uiState.update {
                        it.copy(isLoading = false, bookReview = responseData)
                    }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loadErrorMessage = error.message ?: "감상평 목록을 불러오지 못했습니다.",
                        )
                    }
                }
        }
    }

    fun fetchVoteResult(bookId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(voteState = VoteState.Loading) }
            reviewRepository
                .getVoteResult(bookId)
                .onSuccess { voteResultState ->
                    _uiState.update { it.copy(voteState = voteResultState) }
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
                    _event.emit(ReviewHomeEvent.ShowToast(error.message ?: "공감에 실패했습니다."))
                }
        }
    }

    fun postVote(option: String) {
        val bookId = uiState.value.currentBook?.id ?: return

        if (!uiState.value.canVote) {
            viewModelScope.launch {
                _event.emit(ReviewHomeEvent.ShowToast("책을 다 읽은 후에만 투표할 수 있습니다."))
            }
            return
        }

        if (_uiState.value.voteState !is VoteState.CanVote) {
            return
        }

        viewModelScope.launch {
            val request = VoteRequest(option = option)
            reviewRepository
                .postVote(bookId, request)
                .onSuccess { updatedVoteData ->
                    _uiState.update { it.copy(voteState = VoteState.Voted(updatedVoteData)) }
                }.onFailure { error ->
                    _event.emit(ReviewHomeEvent.ShowToast(error.message ?: "투표에 실패했습니다."))
                }
        }
    }
}

class ReviewHomeViewModelFactory(
    private val reviewRepository: ReviewRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        if (modelClass.isAssignableFrom(ReviewHomeViewModel::class.java)) {
            val savedStateHandle = extras.createSavedStateHandle()
            @Suppress("UNCHECKED_CAST")
            return ReviewHomeViewModel(reviewRepository, savedStateHandle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}