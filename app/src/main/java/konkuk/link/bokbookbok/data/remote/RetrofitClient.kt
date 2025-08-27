package konkuk.link.bokbookbok.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import konkuk.link.bokbookbok.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitClient {
    // TODO: 로그인 후 SharedPreferences에 저장된 토큰을 가져오도록 수정해야 함
    private const val ACCESS_TOKEN = "your_access_token_here"

    private val authOkHttpClient by lazy {
        val authInterceptor =
            Interceptor { chain ->
                val request =
                    chain
                        .request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer $ACCESS_TOKEN")
                        .build()
                chain.proceed(request)
            }

        OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    private val publicOkHttpClient by lazy {
        OkHttpClient
            .Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    private val authRetrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(authOkHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    private val publicRetrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(publicOkHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    val authApiService: ApiService by lazy {
        authRetrofit.create(ApiService::class.java)
    }

    val publicApiService: ApiService by lazy {
        publicRetrofit.create(ApiService::class.java)
    }
}
