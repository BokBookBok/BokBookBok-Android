package konkuk.link.bokbookbok.data.model.response.register

import kotlinx.serialization.Serializable

@Serializable
data class RegisterEmailResponse(
    val duplicated: Boolean,
)
