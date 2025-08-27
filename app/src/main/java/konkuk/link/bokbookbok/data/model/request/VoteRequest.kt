package konkuk.link.bokbookbok.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class VoteRequest(
    val option: String,
)
