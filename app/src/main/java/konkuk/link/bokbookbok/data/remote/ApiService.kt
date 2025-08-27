package konkuk.link.bokbookbok.data.remote

import konkuk.link.bokbookbok.data.model.request.register.RegisterEmailRequest
import konkuk.link.bokbookbok.data.model.request.register.RegisterNicknameRequest
import konkuk.link.bokbookbok.data.model.request.register.RegisterRequest
import konkuk.link.bokbookbok.data.model.response.BaseResponse
import konkuk.link.bokbookbok.data.model.response.register.RegisterEmailResponse
import konkuk.link.bokbookbok.data.model.response.register.RegisterNicknameResponse
import konkuk.link.bokbookbok.data.model.response.register.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest,
    ): BaseResponse<RegisterResponse>

    @POST("/api/auth/register/email")
    suspend fun registerEmailDuplicate(
        @Body request: RegisterEmailRequest,
    ): BaseResponse<RegisterEmailResponse>

    @POST("/api/auth/register/nickname")
    suspend fun registerNicknameDuplicate(
        @Body request: RegisterNicknameRequest,
    ): BaseResponse<RegisterNicknameResponse>
}
