package konkuk.link.bokbookbok.data.remote

import konkuk.link.bokbookbok.data.model.request.login.LoginRequest
import konkuk.link.bokbookbok.data.model.request.register.RegisterEmailRequest
import konkuk.link.bokbookbok.data.model.request.register.RegisterNicknameRequest
import konkuk.link.bokbookbok.data.model.request.register.RegisterRequest
import konkuk.link.bokbookbok.data.model.request.review.ReviewWriteRequest
import konkuk.link.bokbookbok.data.model.request.review.VoteRequest
import konkuk.link.bokbookbok.data.model.response.BaseResponse
import konkuk.link.bokbookbok.data.model.response.login.LoginResponse
import konkuk.link.bokbookbok.data.model.response.register.RegisterEmailResponse
import konkuk.link.bokbookbok.data.model.response.register.RegisterNicknameResponse
import konkuk.link.bokbookbok.data.model.response.register.RegisterResponse
import konkuk.link.bokbookbok.data.model.response.review.BookReviewResponse
import konkuk.link.bokbookbok.data.model.response.review.CurrentBook
import konkuk.link.bokbookbok.data.model.response.review.LikeResponse
import konkuk.link.bokbookbok.data.model.response.review.ReviewWriteResponse
import konkuk.link.bokbookbok.data.model.response.review.VoteResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("/api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest,
    ): BaseResponse<RegisterResponse>

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

    @GET("/api/books/{bookId}")
    suspend fun getBookReviews(
        @Path("bookId") bookId: Int,
    ): BaseResponse<BookReviewResponse>

    @POST("/api/reviews/{reviewId}/likes")
    suspend fun postLike(
        @Path("reviewId") reviewId: Int,
    ): BaseResponse<LikeResponse>

    @GET("/api/books/current")
    suspend fun getCurrentBook(): BaseResponse<CurrentBook>
}
