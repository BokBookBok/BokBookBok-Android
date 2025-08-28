package konkuk.link.bokbookbok.data.model.request.admin

import kotlinx.serialization.Serializable

@Serializable
data class AdminBookRegisterResponse(
    val bookId: Int,
    val title: String,
    val author: String,
    val description: String,
    val startDate: String,
    val endDate: String,
)
