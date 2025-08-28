package konkuk.link.bokbookbok.screen.review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import konkuk.link.bokbookbok.data.model.request.review.ReviewWriteRequest
import konkuk.link.bokbookbok.data.model.response.review.CurrentBook
import konkuk.link.bokbookbok.data.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface ReviewWritePostState {
    object Idle : ReviewWritePostState

    object Loading : ReviewWritePostState

    object Success : ReviewWritePostState

    data class Error(
        val message: String,
    ) : ReviewWritePostState
}

data class ReviewWriteUiState(
    val isLoading: Boolean = true,
    val currentBook: CurrentBook? = null,
    val postState: ReviewWritePostState = ReviewWritePostState.Idle,
)

class ReviewWriteViewModel(
    private val reviewRepository: ReviewRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val bookId: Int = savedStateHandle.get<Int>("bookId") ?: -1

    private val _uiState = MutableStateFlow(ReviewWriteUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchCurrentBook()
    }

    private fun fetchCurrentBook() {
        if (bookId == -1) {
            _uiState.update { it.copy(isLoading = false, postState = ReviewWritePostState.Error("책 정보가 올바르지 않습니다.")) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            reviewRepository
                .getCurrentBook()
                .onSuccess { book ->
                    _uiState.update {
                        it.copy(isLoading = false, currentBook = book)
                    }
                }.onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    fun postReview(content: String) {
        val bookId = _uiState.value.currentBook?.id
        if (bookId == null) {
            _uiState.update { it.copy(postState = ReviewWritePostState.Error("책 정보가 올바르지 않습니다.")) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(postState = ReviewWritePostState.Loading) }
            val request = ReviewWriteRequest(bookId = bookId, content = content)
            reviewRepository
                .postReviewWrite(request)
                .onSuccess {
                    _uiState.update { it.copy(postState = ReviewWritePostState.Success) }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(postState = ReviewWritePostState.Error(error.message ?: "알 수 없는 오류가 발생했습니다."))
                    }
                }
        }
    }
}

class ReviewWriteViewModelFactory(
    private val reviewRepository: ReviewRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        if (modelClass.isAssignableFrom(ReviewWriteViewModel::class.java)) {
            val savedStateHandle = extras.createSavedStateHandle()
            @Suppress("UNCHECKED_CAST")
            return ReviewWriteViewModel(reviewRepository, savedStateHandle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
