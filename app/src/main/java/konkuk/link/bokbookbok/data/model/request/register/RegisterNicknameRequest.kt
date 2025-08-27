package konkuk.link.bokbookbok.data.model.request.register

import kotlinx.serialization.Serializable

@Serializable
data class RegisterNicknameRequest(
    val nickname: String,
)
