package konkuk.link.bokbookbok.data.model.response.reading

import kotlinx.serialization.Serializable

@Serializable
data class CurrentBookResponse(
    val id: Int,
    val title: String,
    val author: String,
    val imageUrl: String
)