package konkuk.link.bokbookbok.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import konkuk.link.bokbookbok.data.model.request.register.RegisterEmailRequest
import konkuk.link.bokbookbok.data.model.request.register.RegisterNicknameRequest
import konkuk.link.bokbookbok.data.model.request.register.RegisterRequest
import konkuk.link.bokbookbok.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.jvm.java

enum class DuplicateCheckState {
    IDLE, // 초기 상태
    SUCCESS, // 사용 가능
    FAILURE, // 중복 또는 사용 불가
}

class RegisterViewModelFactory(
    private val authRepository: AuthRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class RegisterViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _emailCheckState = MutableStateFlow(DuplicateCheckState.IDLE)
    val emailCheckState = _emailCheckState.asStateFlow()

    private val _nicknameCheckState = MutableStateFlow(DuplicateCheckState.IDLE)
    val nicknameCheckState = _nicknameCheckState.asStateFlow()

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            authRepository
                .registerUser(request)
                .onSuccess { response ->
                    // "회원가입 성공! User ID: ${response.userId}"
                }.onFailure { error ->
                    // "회원가입 실패: ${error.message}"
                }
        }
    }

    fun registerEmail(request: RegisterEmailRequest) {
        viewModelScope.launch {
            authRepository
                .registerEmailUser(request)
                .onSuccess { response ->
                    _emailCheckState.value = if (response.duplicated) {
                        DuplicateCheckState.FAILURE
                    } else {
                        DuplicateCheckState.SUCCESS
                    }
                }.onFailure {
                    _emailCheckState.value = DuplicateCheckState.FAILURE
                }
        }
    }

    fun registerNickname(request: RegisterNicknameRequest) {
        viewModelScope.launch {
            authRepository
                .registerNicknameUser(request)
                .onSuccess { response ->
                    _nicknameCheckState.value = if (response.duplicated) {
                        DuplicateCheckState.FAILURE
                    } else {
                        DuplicateCheckState.SUCCESS
                    }
                }.onFailure {
                    _nicknameCheckState.value = DuplicateCheckState.FAILURE
                }
        }
    }

    fun resetEmailCheckState() {
        _emailCheckState.value = DuplicateCheckState.IDLE
    }

    fun resetNicknameCheckState() {
        _nicknameCheckState.value = DuplicateCheckState.IDLE
    }
}
