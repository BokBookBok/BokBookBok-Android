package konkuk.link.bokbookbok.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val userId: Int,
)
