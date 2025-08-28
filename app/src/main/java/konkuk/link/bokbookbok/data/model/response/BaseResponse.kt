package konkuk.link.bokbookbok.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("code")
    val code: Int,
    @SerialName("msg")
    val msg: String,
    @SerialName("data")
    val data: T?,
)

@Serializable
data class ErrorResponse(
    val code: Int,
    val msg: String,
)
