package konkuk.link.bokbookbok.data.model.response.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val userId: Int,
    val jwtToken: JwtToken
)