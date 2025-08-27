package konkuk.link.bokbookbok.data.repository

import konkuk.link.bokbookbok.data.model.request.ReviewWriteRequest
import konkuk.link.bokbookbok.data.model.request.VoteRequest
import konkuk.link.bokbookbok.data.model.response.ReviewWriteResponse
import konkuk.link.bokbookbok.data.model.response.VoteResponse
import konkuk.link.bokbookbok.data.remote.ApiService

class ReviewRepository(
    private val apiService: ApiService,
) {
    suspend fun postReviewWrite(request: ReviewWriteRequest): Result<ReviewWriteResponse> = safeApiCall { apiService.writeReview(request) }

    suspend fun getVoteResult(bookId: Int): Result<VoteResponse> =
        safeApiCall {
            apiService.getVoteResult(bookId)
        }

    suspend fun postVote(
        bookId: Int,
        request: VoteRequest,
    ): Result<VoteResponse> =
        safeApiCall {
            apiService.postVote(bookId, request)
        }
}
