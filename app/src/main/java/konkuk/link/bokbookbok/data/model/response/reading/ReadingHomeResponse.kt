package konkuk.link.bokbookbok.data.model.response.reading

import kotlinx.serialization.Serializable

@Serializable
data class ReadingHomeResponse(
    val book: Book,
    val record: Record? = null, // NOT_STARTED 상태일 때 null
    val myReview: Review? = null, // REVIEWED 상태가 아닐 때 null
    val bestReview: Review?, // 베스트 리뷰가 없을 경우를 대비해 nullable로 선언
    val status: ReadingApiStatus, // Enum으로 상태를 관리하여 타입 안정성 확보
)

@Serializable
data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val imageUrl: String,
    val description: String?,
)

@Serializable
data class Record(
    val readingDays: Int,
    val averageDays: Int,
)

@Serializable
data class Review(
    val id: Int,
    val nickname: String,
    val name: String?,
    val content: String,
    val likeCount: Int,
)

@Serializable
enum class ReadingApiStatus {
    NOT_STARTED,
    READING,
    READ_COMPLETED,
    REVIEWED,
}
