package konkuk.link.bokbookbok.data.model.response.login

import kotlinx.serialization.Serializable

@Serializable
data class JwtToken(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
)
