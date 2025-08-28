package konkuk.link.bokbookbok.data.repository

import konkuk.link.bokbookbok.data.model.response.record.RecordHomeResponse
import konkuk.link.bokbookbok.data.remote.ApiService

class RecordRepository(
    private val apiService: ApiService,
) {
    suspend fun getRecords(): Result<RecordHomeResponse> =
        safeApiCall { apiService.getRecordHome() }
}