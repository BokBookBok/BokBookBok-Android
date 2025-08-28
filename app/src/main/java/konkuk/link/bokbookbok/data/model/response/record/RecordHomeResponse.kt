package konkuk.link.bokbookbok.data.model.response.record

import kotlinx.serialization.Serializable

@Serializable
data class RecordHomeResponse(
    val nickname: String,
    val totalCount: Int,
    val records: List<Record>,
)

@Serializable
data class Record(
    val bookInfoResponse: BookInfoResponse,
    val readDays: Int,
    val startDate: String,
    val endDate: String,
    val weekLabel: String
)

@Serializable
data class BookInfoResponse(
    val id: Int,
    val title: String,
    val author: String,
    val description: String,
    val imageUrl: String
)