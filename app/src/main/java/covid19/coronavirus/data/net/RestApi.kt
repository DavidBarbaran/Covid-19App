package covid19.coronavirus.data.net

import covid19.coronavirus.BuildConfig
import covid19.coronavirus.model.CountryResponse
import covid19.coronavirus.model.TotalResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface RestApi {

    @GET("v2/countries")
    suspend fun getCurrent(): Response<MutableList<CountryResponse>>

    @GET("v2/all")
    suspend fun getTotal(): Response<TotalResponse>

    companion object {
        private const val TIMEOUT_READ = 40L
        private const val TIMEOUT_WRITE = 40L
        private const val TIMEOUT_CONNECT = 30L

        val okHttpClient: OkHttpClient = OkHttpClient
            .Builder()
            .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS)
            .addInterceptor(getInterceptor())
            .connectTimeout(TIMEOUT_CONNECT, TimeUnit.SECONDS)
            .build()

        private fun getInterceptor(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            return interceptor
        }
    }
}