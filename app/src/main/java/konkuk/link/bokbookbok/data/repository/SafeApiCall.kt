package konkuk.link.bokbookbok.data.repository

import konkuk.link.bokbookbok.data.model.response.BaseResponse

suspend fun <T> safeApiCall(call: suspend () -> BaseResponse<T>): Result<T> =
    try {
        val response = call()
        if (response.code == 200) {
            response.data?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Data is null"))
        } else {
            Result.failure(Exception(response.msg))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
