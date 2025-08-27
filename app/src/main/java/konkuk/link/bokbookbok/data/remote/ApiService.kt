package konkuk.link.bokbookbok.data.remote

import konkuk.link.bokbookbok.data.model.request.RegisterRequest
import konkuk.link.bokbookbok.data.model.request.ReviewWriteRequest
import konkuk.link.bokbookbok.data.model.request.VoteRequest
import konkuk.link.bokbookbok.data.model.response.BaseResponse
import konkuk.link.bokbookbok.data.model.response.RegisterResponse
import konkuk.link.bokbookbok.data.model.response.ReviewWriteResponse
import konkuk.link.bokbookbok.data.model.response.VoteResponse
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
}
