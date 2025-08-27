package konkuk.link.bokbookbok.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import konkuk.link.bokbookbok.data.model.request.RegisterRequest
import konkuk.link.bokbookbok.data.repository.AuthRepository
import kotlinx.coroutines.launch
import kotlin.jvm.java

class SignUpViewModelFactory(
    private val authRepository: AuthRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignUpViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class SignUpViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
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
}
