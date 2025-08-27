package konkuk.link.bokbookbok.data.repository

import konkuk.link.bokbookbok.data.model.request.RegisterRequest
import konkuk.link.bokbookbok.data.model.response.RegisterResponse
import konkuk.link.bokbookbok.data.remote.ApiService

class AuthRepository(
    private val apiService: ApiService,
) {
    suspend fun registerUser(request: RegisterRequest): Result<RegisterResponse> = safeApiCall { apiService.register(request) }
}
