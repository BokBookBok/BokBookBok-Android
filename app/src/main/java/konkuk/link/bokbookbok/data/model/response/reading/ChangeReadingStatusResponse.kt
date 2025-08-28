package konkuk.link.bokbookbok.data.model.response.reading

import kotlinx.serialization.Serializable

@Serializable
data class ChangeReadingStatusResponse(
    val bookId: Int,
    val status: ReadingApiStatus
)