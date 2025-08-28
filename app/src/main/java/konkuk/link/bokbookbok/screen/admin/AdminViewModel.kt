package konkuk.link.bokbookbok.screen.admin

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import konkuk.link.bokbookbok.data.repository.AdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed interface AdminUiState {
    object Idle : AdminUiState

    object Loading : AdminUiState

    data class Success(
        val message: String,
    ) : AdminUiState

    data class Error(
        val message: String,
    ) : AdminUiState
}

class AdminViewModel(
    private val adminRepository: AdminRepository,
) : ViewModel() {
    // 화면의 모든 입력값을 상태로 관리
    var title by mutableStateOf("")
    var author by mutableStateOf("")
    var description by mutableStateOf("")
    var imageUri by mutableStateOf<Uri?>(null)
    var startDate by mutableStateOf<Date?>(null)
    var endDate by mutableStateOf<Date?>(null)
    var isCurrent by mutableStateOf(true)

    private val _uiState = MutableStateFlow<AdminUiState>(AdminUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun onIsCurrentChanged(newStatus: Boolean) {
        isCurrent = newStatus
    }

    fun onTitleChanged(newTitle: String) {
        title = newTitle
    }

    fun onAuthorChanged(newAuthor: String) {
        author = newAuthor
    }

    fun onDescriptionChanged(newDesc: String) {
        description = newDesc
    }

    fun onImageSelected(uri: Uri?) {
        imageUri = uri
    }

    fun onDateRangeSelected(
        start: Date?,
        end: Date?,
    ) {
        startDate = start
        endDate = end
    }

    fun registerBook() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val start = startDate
        val end = endDate
        val uri = imageUri

        // 필수 값 검증
        if (title.isBlank() || author.isBlank() || description.isBlank() || start == null || end == null || uri == null) {
            _uiState.value = AdminUiState.Error("모든 필드를 입력해주세요.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AdminUiState.Loading
            adminRepository
                .registerBook(
                    title = title,
                    description = description,
                    author = author,
                    startDate = dateFormat.format(start),
                    endDate = dateFormat.format(end),
                    imageUri = uri,
                    isCurrent = isCurrent,
                ).onSuccess {
                    _uiState.value = AdminUiState.Success("도서 등록 성공! (ID: ${it.bookId})")
                }.onFailure {
                    _uiState.value = AdminUiState.Error(it.message ?: "등록에 실패했습니다.")
                }
        }
    }
}

// Factory
class AdminViewModelFactory(
    private val adminRepository: AdminRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdminViewModel(adminRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
