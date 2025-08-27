package konkuk.link.bokbookbok.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val nickname: String,
    val password: String,
)
