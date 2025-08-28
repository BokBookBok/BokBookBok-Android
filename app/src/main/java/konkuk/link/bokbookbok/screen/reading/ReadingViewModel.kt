package konkuk.link.bokbookbok.screen.reading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import konkuk.link.bokbookbok.data.model.response.reading.CurrentBookResponse
import konkuk.link.bokbookbok.data.repository.ReadingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReadingUiState(
    val currentBook: CurrentBookResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

class ReadingViewModelFactory(
    private val readingRepository: ReadingRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReadingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReadingViewModel(readingRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ReadingViewModel(
    private val readingRepository: ReadingRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReadingUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        fetchCurrentBook()
    }

    private fun fetchCurrentBook() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            readingRepository.getCurrentBook()
                .onSuccess { bookResponse ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            currentBook = bookResponse,
                            errorMessage = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "알 수 없는 오류가 발생했습니다."
                        )
                    }
                }
        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}