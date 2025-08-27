package konkuk.link.bokbookbok.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ReviewWriteRequest(
    val bookId: Int,
    val content: String? = "",
)
