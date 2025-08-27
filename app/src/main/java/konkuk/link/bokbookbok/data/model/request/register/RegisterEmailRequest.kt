package konkuk.link.bokbookbok.data.model.request.register

import kotlinx.serialization.Serializable

@Serializable
data class RegisterEmailRequest(
    val email: String,
)
