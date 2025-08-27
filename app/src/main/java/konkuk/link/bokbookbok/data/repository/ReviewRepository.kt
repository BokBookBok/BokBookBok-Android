package konkuk.link.bokbookbok.data.repository

import konkuk.link.bokbookbok.data.model.request.ReviewWriteRequest
import konkuk.link.bokbookbok.data.model.response.ReviewWriteResponse
import konkuk.link.bokbookbok.data.remote.ApiService

class ReviewRepository(
    private val apiService: ApiService,
) {
    suspend fun postReviewWrite(request: ReviewWriteRequest): Result<ReviewWriteResponse> = safeApiCall { apiService.writeReview(request) }
}
