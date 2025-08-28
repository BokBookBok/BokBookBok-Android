package konkuk.link.bokbookbok.data.model.response.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LikeResponse(
    @SerialName("reviewId")
    val reviewId: Int,
    @SerialName("liked")
    val liked: Boolean,
    @SerialName("likeCount")
    val likeCount: Int,
)
