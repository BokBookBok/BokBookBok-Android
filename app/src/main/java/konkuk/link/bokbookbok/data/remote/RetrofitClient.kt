package konkuk.link.bokbookbok.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import konkuk.link.bokbookbok.BuildConfig
import konkuk.link.bokbookbok.util.TokenManager
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitClient {
    private val authOkHttpClient by lazy {
        val authInterceptor =
            Interceptor { chain ->
                val token = TokenManager.getAccessToken()
                val requestBuilder = chain.request().newBuilder()

                token?.let {
                    requestBuilder.addHeader("Authorization", "Bearer $it")
                }

                chain.proceed(requestBuilder.build())
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
