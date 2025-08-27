package konkuk.link.bokbookbok.screen.review

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import konkuk.link.bokbookbok.data.model.request.ReviewWriteRequest
import konkuk.link.bokbookbok.data.model.request.VoteRequest
import konkuk.link.bokbookbok.data.model.response.VoteResponse
import konkuk.link.bokbookbok.data.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.jvm.java

class ReviewViewModelFactory(
    private val reviewRepository: ReviewRepository,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null,
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle,
    ): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReviewViewModel(reviewRepository, handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

sealed interface ReviewPostState {
    object Idle : ReviewPostState

    object Loading : ReviewPostState

    object Success : ReviewPostState

    data class Error(
        val message: String,
    ) : ReviewPostState
}

sealed interface VoteState {
    object Loading : VoteState

    data class Success(
        val voteData: VoteResponse,
    ) : VoteState

    data class Error(
        val message: String,
    ) : VoteState
}

data class ReviewScreenUiState(
    val reviewPostState: ReviewPostState = ReviewPostState.Idle,
    val voteState: VoteState = VoteState.Loading,
)

class ReviewViewModel(
    private val reviewRepository: ReviewRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val bookId: Int = savedStateHandle.get<Int>("bookId") ?: -1

    private val _uiState = MutableStateFlow(ReviewScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun postReview(content: String) {
        if (isInvalidBookId()) return

        viewModelScope.launch {
            _uiState.update { it.copy(reviewPostState = ReviewPostState.Loading) }
            val request = ReviewWriteRequest(bookId = bookId, content = content)
            reviewRepository
                .postReviewWrite(request)
                .onSuccess {
                    _uiState.update { it.copy(reviewPostState = ReviewPostState.Success) }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(reviewPostState = ReviewPostState.Error(error.message ?: "알 수 없는 오류가 발생했습니다."))
                    }
                }
        }
    }

    private fun fetchVoteResult() {
        if (isInvalidBookId()) return

        viewModelScope.launch {
            _uiState.update { it.copy(voteState = VoteState.Loading) }
            reviewRepository
                .getVoteResult(bookId)
                .onSuccess { response ->
                    _uiState.update { it.copy(voteState = VoteState.Success(response)) }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(voteState = VoteState.Error(error.message ?: "투표 정보를 불러오는데 실패했습니다."))
                    }
                }
        }
    }

    fun postVote(option: String) {
        if (isInvalidBookId()) return

        val currentVoteState = _uiState.value.voteState
        if (currentVoteState is VoteState.Success && currentVoteState.voteData.myVote != null) {
            return // 이미 투표했으면 다시 요청하지 않음
        }

        viewModelScope.launch {
            val request = VoteRequest(option = option)
            reviewRepository
                .postVote(bookId, request)
                .onSuccess { response ->
                    _uiState.update { it.copy(voteState = VoteState.Success(response)) }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(voteState = VoteState.Error(error.message ?: "투표에 실패했습니다."))
                    }
                }
        }
    }

    private fun isInvalidBookId(): Boolean {
        if (bookId == -1) {
            _uiState.update {
                it.copy(
                    reviewPostState = ReviewPostState.Error("책 정보가 올바르지 않습니다."),
                    voteState = VoteState.Error("책 정보가 올바르지 않습니다."),
                )
            }
            return true
        }
        return false
    }
}
