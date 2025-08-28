package konkuk.link.bokbookbok.screen.reading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import konkuk.link.bokbookbok.data.model.response.reading.ReadingHomeResponse
import konkuk.link.bokbookbok.data.repository.ReadingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// UI 상태가 이제 HomeData 전체를 관리합니다.
data class ReadingUiState(
    val homeData: ReadingHomeResponse? = null,
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

    // 함수의 이름을 더 명확하게 변경
    private fun fetchReadingHome() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Repository의 함수 이름도 API 스펙에 맞게 변경되었다고 가정합니다.
            readingRepository.getReading()
                .onSuccess { homeResponse ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            homeData = homeResponse, // currentBook 대신 homeData 전체를 저장
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

    // 독서 시작 API를 호출하는 함수
    fun startReading() {
        viewModelScope.launch {
            // TODO: ReadingRepository에 독서 시작 API 호출 함수 구현 필요
            // readingRepository.startReading(bookId).onSuccess {
            // 상태 변경 성공 시, 홈 화면 정보를 다시 불러와 UI를 갱신합니다.
            fetchReadingHome()
            // }.onFailure { ... }
        }
    }

    // 독서 완료 API를 호출하는 함수
    fun completeReading() {
        viewModelScope.launch {
            // TODO: ReadingRepository에 독서 완료 API 호출 함수 구현 필요
            // readingRepository.completeReading(bookId).onSuccess {
            // 상태 변경 성공 시, 홈 화면 정보를 다시 불러와 UI를 갱신합니다.
            fetchReadingHome()
            // }.onFailure { ... }
        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}