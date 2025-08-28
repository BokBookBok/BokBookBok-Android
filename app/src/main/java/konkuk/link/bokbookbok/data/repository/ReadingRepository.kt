package konkuk.link.bokbookbok.data.repository

import konkuk.link.bokbookbok.data.model.request.reading.ChangeReadingStatusRequest
import konkuk.link.bokbookbok.data.model.response.reading.ChangeReadingStatusResponse
import konkuk.link.bokbookbok.data.model.response.reading.ReadingApiStatus
import konkuk.link.bokbookbok.data.model.response.reading.ReadingHomeResponse
import konkuk.link.bokbookbok.data.remote.ApiService

class ReadingRepository(
    private val apiService: ApiService,
) {
    suspend fun getReading(): Result<ReadingHomeResponse> =
        safeApiCall {
            apiService.getReadingHome()
        }

    suspend fun patchStatus(
        bookId: Int,
        status: ReadingApiStatus,
    ): Result<ChangeReadingStatusResponse> {
        val request = ChangeReadingStatusRequest(status)
        return safeApiCall { apiService.patchReadingStatus(bookId, request) }
    }
}
