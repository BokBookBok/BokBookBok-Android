package konkuk.link.bokbookbok.data.remote

import konkuk.link.bokbookbok.data.model.request.RegisterRequest
import konkuk.link.bokbookbok.data.model.response.BaseResponse
import konkuk.link.bokbookbok.data.model.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest,
    ): BaseResponse<RegisterResponse>
}
