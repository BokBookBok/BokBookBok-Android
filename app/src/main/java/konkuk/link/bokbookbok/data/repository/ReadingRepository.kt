package konkuk.link.bokbookbok.data.repository

import konkuk.link.bokbookbok.data.model.response.reading.ReadingHomeResponse
import konkuk.link.bokbookbok.data.remote.ApiService

class ReadingRepository(
    private val apiService: ApiService,
) {
    suspend fun getReading(): Result<ReadingHomeResponse> =
        safeApiCall {
            apiService.getReadingHome()
        }
}