package konkuk.link.bokbookbok.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import konkuk.link.bokbookbok.data.model.request.login.LoginRequest
import konkuk.link.bokbookbok.data.model.response.login.LoginResponse
import konkuk.link.bokbookbok.data.repository.AuthRepository
import konkuk.link.bokbookbok.util.TokenManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModelFactory(
    private val authRepository: AuthRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _emailCheckState = MutableStateFlow(DuplicateCheckState.IDLE)
    val emailCheckState = _emailCheckState.asStateFlow()

    private val _loginErrorMessage = MutableStateFlow<String?>(null)
    val loginErrorMessage = _loginErrorMessage.asStateFlow()

    private val _loginSuccessEvent = MutableSharedFlow<Unit>()
    val loginSuccessEvent = _loginSuccessEvent.asSharedFlow()

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            authRepository
                .loginUser(request)
                .onSuccess { response: LoginResponse ->
                    val accessToken = response.jwtToken.accessToken
                    TokenManager.saveAccessToken(accessToken)
                    _loginErrorMessage.value = null
                    _loginSuccessEvent.emit(Unit)
                }.onFailure { error ->
                    _loginErrorMessage.value = error.message
                }
        }
    }

    fun clearLoginError() {
        _loginErrorMessage.value = null
    }

    fun resetEmailCheckState() {
        _emailCheckState.value = DuplicateCheckState.IDLE
    }
}
