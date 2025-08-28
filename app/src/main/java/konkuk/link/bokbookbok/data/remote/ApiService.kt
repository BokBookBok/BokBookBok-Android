package konkuk.link.bokbookbok.data.remote

import konkuk.link.bokbookbok.data.model.request.ReviewWriteRequest
import konkuk.link.bokbookbok.data.model.request.VoteRequest
import konkuk.link.bokbookbok.data.model.request.login.LoginRequest
import konkuk.link.bokbookbok.data.model.request.register.RegisterEmailRequest
import konkuk.link.bokbookbok.data.model.request.register.RegisterNicknameRequest
import konkuk.link.bokbookbok.data.model.request.register.RegisterRequest
import konkuk.link.bokbookbok.data.model.response.BaseResponse
import konkuk.link.bokbookbok.data.model.response.ReviewWriteResponse
import konkuk.link.bokbookbok.data.model.response.VoteResponse
import konkuk.link.bokbookbok.data.model.response.login.LoginResponse
import konkuk.link.bokbookbok.data.model.response.reading.ReadingHomeResponse
import konkuk.link.bokbookbok.data.model.response.register.RegisterEmailResponse
import konkuk.link.bokbookbok.data.model.response.register.RegisterNicknameResponse
import konkuk.link.bokbookbok.data.model.response.register.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // Auth
    @POST("/api/auth/register/email")
    suspend fun registerEmailDuplicate(
        @Body request: RegisterEmailRequest,
    ): BaseResponse<RegisterEmailResponse>

    @POST("/api/auth/register/nickname")
    suspend fun registerNicknameDuplicate(
        @Body request: RegisterNicknameRequest,
    ): BaseResponse<RegisterNicknameResponse>

    @POST("/api/auth/login")
    suspend fun login(
        @Body request: LoginRequest,
    ): BaseResponse<LoginResponse>

    @POST("/api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest,
    ): BaseResponse<RegisterResponse>

    // Review
    @POST("/api/reviews/write")
    suspend fun writeReview(
        @Body request: ReviewWriteRequest,
    ): BaseResponse<ReviewWriteResponse>

    @GET("/api/books/{bookId}/vote/result")
    suspend fun getVoteResult(
        @Path("bookId") bookId: Int,
    ): BaseResponse<VoteResponse>

    @POST("/api/books/{bookId}/vote")
    suspend fun postVote(
        @Path("bookId") bookId: Int,
        @Body request: VoteRequest,
    ): BaseResponse<VoteResponse>

    // Reading
    @GET("/api/home")
    suspend fun getReadingHome(): BaseResponse<ReadingHomeResponse>
}
