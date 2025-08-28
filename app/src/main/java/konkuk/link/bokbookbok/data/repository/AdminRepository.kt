package konkuk.link.bokbookbok.data.repository

import android.content.Context
import android.net.Uri
import konkuk.link.bokbookbok.data.model.request.admin.AdminBookRegisterResponse
import konkuk.link.bokbookbok.data.remote.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class AdminRepository(
    private val apiService: ApiService,
    private val context: Context,
) {
    suspend fun registerBook(
        title: String,
        description: String,
        author: String,
        startDate: String,
        endDate: String,
        imageUri: Uri,
        isCurrent: Boolean,
    ): Result<AdminBookRegisterResponse> {
        val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val authorBody = author.toRequestBody("text/plain".toMediaTypeOrNull())
        val startDateBody = startDate.toRequestBody("text/plain".toMediaTypeOrNull())
        val endDateBody = endDate.toRequestBody("text/plain".toMediaTypeOrNull())
        val isCurrentBody = isCurrent.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val filePart =
            context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                val fileBytes = inputStream.readBytes()
                val fileRequestBody = fileBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("bookImageUrl", "upload.jpg", fileRequestBody)
            } ?: return Result.failure(Exception("이미지 파일을 읽을 수 없습니다."))

        return safeApiCall {
            apiService.registerBook(
                title = titleBody,
                description = descriptionBody,
                author = authorBody,
                startDate = startDateBody,
                endDate = endDateBody,
                bookImageUrl = filePart,
                isCurrent = isCurrentBody,
            )
        }
    }
}
