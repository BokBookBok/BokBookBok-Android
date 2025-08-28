package konkuk.link.bokbookbok.data.repository

import konkuk.link.bokbookbok.data.model.request.login.LoginRequest
import konkuk.link.bokbookbok.data.model.request.register.RegisterEmailRequest
import konkuk.link.bokbookbok.data.model.request.register.RegisterNicknameRequest
import konkuk.link.bokbookbok.data.model.request.register.RegisterRequest
import konkuk.link.bokbookbok.data.model.response.login.LoginResponse
import konkuk.link.bokbookbok.data.model.response.register.RegisterEmailResponse
import konkuk.link.bokbookbok.data.model.response.register.RegisterNicknameResponse
import konkuk.link.bokbookbok.data.model.response.register.RegisterResponse
import konkuk.link.bokbookbok.data.remote.ApiService

class AuthRepository(
    private val apiService: ApiService,
) {
    suspend fun registerUser(request: RegisterRequest): Result<RegisterResponse> = safeApiCall { apiService.register(request) }

    suspend fun registerEmailUser(request: RegisterEmailRequest): Result<RegisterEmailResponse> = safeApiCall { apiService.registerEmailDuplicate(request) }

    suspend fun registerNicknameUser(request: RegisterNicknameRequest): Result<RegisterNicknameResponse> = safeApiCall { apiService.registerNicknameDuplicate(request) }

    suspend fun loginUser(request: LoginRequest): Result<LoginResponse> = safeApiCall { apiService.login(request) }
}
