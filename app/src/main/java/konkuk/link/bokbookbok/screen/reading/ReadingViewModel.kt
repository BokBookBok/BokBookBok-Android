package konkuk.link.bokbookbok.screen.reading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import konkuk.link.bokbookbok.data.model.response.reading.ReadingApiStatus
import konkuk.link.bokbookbok.data.model.response.reading.ReadingHomeResponse
import konkuk.link.bokbookbok.data.repository.ReadingRepository
import konkuk.link.bokbookbok.util.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReadingUiState(
    val homeData: ReadingHomeResponse? = null,
    val userNickname: String? = null,
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
        fetchReadingHome()
    }

    private fun fetchReadingHome() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val nickname = UserManager.getNickname()

            readingRepository.getReading()
                .onSuccess { homeResponse ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            homeData = homeResponse,
                            userNickname = nickname,
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

    fun startReading() {
        val currentBookId = _uiState.value.homeData?.book?.id ?: return

        viewModelScope.launch {
            readingRepository.patchStatus(
                bookId = currentBookId,
                status = ReadingApiStatus.READING
            ).onSuccess {
                fetchReadingHome()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(errorMessage = error.message ?: "상태 변경에 실패했습니다.")
                }
            }
        }
    }

    fun completeReading() {
        val currentBookId = _uiState.value.homeData?.book?.id ?: return

        viewModelScope.launch {
            readingRepository.patchStatus(
                bookId = currentBookId,
                status = ReadingApiStatus.READ_COMPLETED
            ).onSuccess {
                fetchReadingHome()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(errorMessage = error.message ?: "상태 변경에 실패했습니다.")
                }
            }
        }
    }
}