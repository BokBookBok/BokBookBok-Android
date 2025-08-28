package konkuk.link.bokbookbok.screen.review

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import konkuk.link.bokbookbok.data.model.request.ReviewWriteRequest
import konkuk.link.bokbookbok.data.model.request.VoteRequest
import konkuk.link.bokbookbok.data.model.response.ReviewWriteResponse
import konkuk.link.bokbookbok.data.model.response.VoteResponse
import konkuk.link.bokbookbok.data.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.jvm.java

sealed interface VoteState {
    object Loading : VoteState
    data class Success(val voteData: VoteResponse) : VoteState
    data class Error(val message: String) : VoteState
}

// 감상홈 화면 전체의 상태를 나타내는 데이터 클래스
data class ReviewHomeUiState(
    val isReviewLoading: Boolean = true,
    // val reviews: List<Review> = emptyList(),
    val voteState: VoteState = VoteState.Loading, // 투표 상태를 여기에 포함
    val errorMessage: String? = null
)


class ReviewHomeViewModel(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewHomeUiState())
    val uiState = _uiState.asStateFlow()

    // TODO: 감상홈에 표시될 투표의 bookId를 가져오는 로직 필요
    private val featuredBookId = 1

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        fetchReviews()
        fetchVoteResult()
    }

    private fun fetchReviews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isReviewLoading = true) }
        }
    }

    private fun fetchVoteResult() {
        viewModelScope.launch {
            _uiState.update { it.copy(voteState = VoteState.Loading) }
            reviewRepository.getVoteResult(featuredBookId)
                .onSuccess { voteData ->
                    _uiState.update { it.copy(voteState = VoteState.Success(voteData)) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(voteState = VoteState.Error(error.message ?: "투표 정보 로딩 실패")) }
                }
        }
    }

    fun toggleLike(reviewId: Int) {
        viewModelScope.launch {
//            reviewRepository.postLike(reviewId)
//                .onSuccess { likeResponse ->
//                    // 공감 성공 시, 전체 목록을 새로고침하지 않고
//                    // 로컬 상태의 해당 아이템만 업데이트하여 즉각적인 UI 반응을 보여줍니다.
//                    _uiState.update { currentState ->
//                        val updatedReviews = currentState.reviews.map { review ->
//                            if (review.reviewId == likeResponse.reviewId) {
//                                review.copy(liked = likeResponse.liked, likeCount = likeResponse.likeCount)
//                            } else {
//                                review
//                            }
//                        }
//                        currentState.copy(reviews = updatedReviews)
//                    }
//                }
//                .onFailure { error ->
//                    _uiState.update { it.copy(errorMessage = error.message) }
//                }
        }
    }

    fun postVote(option: String) {
        val currentVoteState = _uiState.value.voteState
        if (currentVoteState is VoteState.Loading ||
            (currentVoteState is VoteState.Success && currentVoteState.voteData.myVote != null)) {
            return
        }

        viewModelScope.launch {
            val request = VoteRequest(option = option)
            reviewRepository.postVote(featuredBookId, request)
                .onSuccess { updatedVoteData ->
                    _uiState.update { it.copy(voteState = VoteState.Success(updatedVoteData)) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(voteState = VoteState.Error(error.message ?: "투표 실패")) }
                }
        }
    }
}

class ReviewHomeViewModelFactory(
    private val reviewRepository: ReviewRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewHomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReviewHomeViewModel(reviewRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

