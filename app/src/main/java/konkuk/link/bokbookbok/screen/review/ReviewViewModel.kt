package konkuk.link.bokbookbok.screen.review

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import konkuk.link.bokbookbok.data.model.request.ReviewWriteRequest
import konkuk.link.bokbookbok.data.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

sealed class ReviewUiState {
    object Idle : ReviewUiState()

    object Loading : ReviewUiState()

    object Success : ReviewUiState()

    data class Error(
        val message: String,
    ) : ReviewUiState()
}

class ReviewViewModel(
    private val ReviewRepository: ReviewRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val bookId: Int = savedStateHandle.get<Int>("bookId") ?: -1

    private val _uiState = MutableStateFlow<ReviewUiState>(ReviewUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun postReview(content: String) {
        if (bookId == -1) {
            _uiState.value = ReviewUiState.Error("책 정보가 올바르지 않습니다.")
            return
        }

        viewModelScope.launch {
            _uiState.value = ReviewUiState.Loading
            val request = ReviewWriteRequest(bookId = bookId, content = content)
            ReviewRepository
                .postReviewWrite(request)
                .onSuccess {
                    _uiState.value = ReviewUiState.Success
                }.onFailure { error ->
                    _uiState.value = ReviewUiState.Error(error.message ?: "알 수 없는 오류가 발생했습니다.")
                }
        }
    }
}
