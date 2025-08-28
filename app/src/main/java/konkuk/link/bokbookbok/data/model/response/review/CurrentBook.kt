package konkuk.link.bokbookbok.data.model.response.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentBook(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("author")
    val author: String,
    @SerialName("description")
    val description: String,
    @SerialName("imageUrl")
    val imageUrl: String,
)
