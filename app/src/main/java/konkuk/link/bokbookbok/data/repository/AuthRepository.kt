package konkuk.link.bokbookbok.data.repository

import android.net.http.HttpException
import konkuk.link.bokbookbok.data.model.request.login.LoginRequest
import konkuk.link.bokbookbok.data.model.request.register.RegisterEmailRequest
import konkuk.link.bokbookbok.data.model.request.register.RegisterNicknameRequest
import konkuk.link.bokbookbok.data.model.request.register.RegisterRequest
import konkuk.link.bokbookbok.data.model.response.BaseResponse
import konkuk.link.bokbookbok.data.model.response.login.LoginResponse
import konkuk.link.bokbookbok.data.model.response.register.RegisterEmailResponse
import konkuk.link.bokbookbok.data.model.response.register.RegisterNicknameResponse
import konkuk.link.bokbookbok.data.model.response.register.RegisterResponse
import konkuk.link.bokbookbok.data.remote.ApiException
import konkuk.link.bokbookbok.data.remote.ApiService
import kotlinx.serialization.json.Json

class AuthRepository(
    private val apiService: ApiService,
) {
    suspend fun registerUser(request: RegisterRequest): Result<RegisterResponse> = safeApiCall { apiService.register(request) }

    suspend fun registerEmailUser(request: RegisterEmailRequest): Result<RegisterEmailResponse> = safeApiCall { apiService.registerEmailDuplicate(request) }

    suspend fun registerNicknameUser(request: RegisterNicknameRequest): Result<RegisterNicknameResponse> = safeApiCall { apiService.registerNicknameDuplicate(request) }

    suspend fun loginUser(request: LoginRequest): Result<LoginResponse> =
        try {
            val response = apiService.login(request)

            if (response.code == 200 && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(ApiException(response.msg))
            }
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    val errorMsg = parseErrorBody(e)
                    Result.failure(ApiException(errorMsg))
                }
                else -> {
                    Result.failure(e)
                }
            }
        }
}

private fun parseErrorBody(e: HttpException): String =
    try {
        val errorBodyString = e.toString()
        if (errorBodyString != null) {
            val errorResponse = Json.decodeFromString<BaseResponse<Unit>>(errorBodyString)
            errorResponse.msg
        } else {
            "알 수 없는 오류가 발생했습니다."
        }
    } catch (jsonError: Exception) {
        "오류 응답을 처리할 수 없습니다."
    }
