package konkuk.link.bokbookbok.screen.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import konkuk.link.bokbookbok.data.model.response.record.RecordHomeResponse
import konkuk.link.bokbookbok.data.repository.RecordRepository
import konkuk.link.bokbookbok.util.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RecordUiState(
    val recordData: RecordHomeResponse? = null,
    val userNickname: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

class RecordViewModelFactory(
    private val recordRepository: RecordRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecordViewModel(recordRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class RecordViewModel(
    private val recordRepository: RecordRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecordUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        fetchRecordHome()
    }

    private fun fetchRecordHome() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val nickname = UserManager.getNickname()

            recordRepository.getRecords()
                .onSuccess { homeResponse ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            recordData = homeResponse,
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
}