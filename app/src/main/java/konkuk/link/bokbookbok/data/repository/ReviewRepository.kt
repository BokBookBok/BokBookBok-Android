package konkuk.link.bokbookbok.data.repository

import konkuk.link.bokbookbok.data.model.request.review.ReviewWriteRequest
import konkuk.link.bokbookbok.data.model.request.review.VoteRequest
import konkuk.link.bokbookbok.data.model.response.ErrorResponse
import konkuk.link.bokbookbok.data.model.response.review.BookReviewResponse
import konkuk.link.bokbookbok.data.model.response.review.CurrentBook
import konkuk.link.bokbookbok.data.model.response.review.LikeResponse
import konkuk.link.bokbookbok.data.model.response.review.ReviewWriteResponse
import konkuk.link.bokbookbok.data.model.response.review.VoteResponse
import konkuk.link.bokbookbok.data.remote.ApiService
import konkuk.link.bokbookbok.screen.review.VoteState
import kotlinx.serialization.json.Json

class ReviewRepository(
    private val apiService: ApiService,
) {
    suspend fun postReviewWrite(request: ReviewWriteRequest): Result<ReviewWriteResponse> = safeApiCall { apiService.writeReview(request) }

    suspend fun getVoteResult(bookId: Int): Result<VoteState> {
        try {
            val response = apiService.getVoteResult(bookId)

            if (response.isSuccessful) {
                val voteData = response.body()?.data
                return if (voteData == null) {
                    Result.failure(Exception("Vote data is null"))
                } else {
                    if (voteData.myVote == null) {
                        Result.success(VoteState.CanVote(voteData))
                    } else {
                        Result.success(VoteState.Voted(voteData))
                    }
                }
            } else if (response.code() == 404) {
                val errorBody = response.errorBody()?.string()
                if (errorBody != null) {
                    val errorResponse = Json.decodeFromString<ErrorResponse>(errorBody)
                    if (errorResponse.code == 4016) {
                        return Result.success(VoteState.NotCreated(errorResponse.msg))
                    }
                }
                return Result.failure(Exception("Not Found (404)"))
            } else {
                return Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun postVote(
        bookId: Int,
        request: VoteRequest,
    ): Result<VoteResponse> =
        safeApiCall {
            apiService.postVote(bookId, request)
        }

    suspend fun getBookReviews(bookId: Int): Result<BookReviewResponse> = safeApiCall { apiService.getBookReviews(bookId) }

    suspend fun postLike(reviewId: Int): Result<LikeResponse> = safeApiCall { apiService.postLike(reviewId) }

    suspend fun getCurrentBook(): Result<CurrentBook> = safeApiCall { apiService.getCurrentBook() }
}
