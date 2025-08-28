package konkuk.link.bokbookbok.data.model.response.review

import kotlinx.serialization.Serializable

@Serializable
data class ReviewWriteResponse(
    val reviewId: Int,
    val userId: Int,
    val bookId: Int,
)
