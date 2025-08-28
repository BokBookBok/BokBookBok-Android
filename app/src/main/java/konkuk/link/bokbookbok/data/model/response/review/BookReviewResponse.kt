package konkuk.link.bokbookbok.data.model.response.review

import kotlinx.serialization.Serializable

@Serializable
data class BookReviewResponse(
    val myReview: Review?,
    val otherReviews: List<Review>,
)

@Serializable
data class Review(
    val reviewId: Int,
    val userId: Int,
    val nickname: String,
    val content: String,
    val likeCount: Int,
    val liked: Boolean,
)
