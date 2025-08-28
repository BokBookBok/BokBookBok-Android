package konkuk.link.bokbookbok.data.model.request.reading

import konkuk.link.bokbookbok.data.model.response.reading.ReadingApiStatus
import kotlinx.serialization.Serializable

@Serializable
data class ChangeReadingStatusRequest(
    val status: ReadingApiStatus,
)
