package konkuk.link.bokbookbok.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class VoteResult(
    val option: String,
    val text: String,
    val count: Int,
    val percentage: Int,
)

@Serializable
data class VoteResponse(
    val question: String,
    val voteResult: List<VoteResult>,
    val myVote: String?,
)
