package konkuk.link.bokbookbok.data.repository

import konkuk.link.bokbookbok.data.model.response.reading.CurrentBookResponse
import konkuk.link.bokbookbok.data.remote.ApiService

class ReadingRepository(
    private val apiService: ApiService,
) {
    suspend fun getCurrentBook(): Result<CurrentBookResponse> =
        safeApiCall {
            apiService.getCurrentBook()
        }
}