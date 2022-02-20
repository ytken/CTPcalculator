package ru.ytken.sravni.internship.data.storage

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import ru.ytken.sravni.internship.data.storage.models.ListCoefficientsGet
import ru.ytken.sravni.internship.data.storage.models.ListParametersPost

interface ApiStorage {

    companion object {
        private const val BASE_URL = "http://mock.sravni-team.ru"

        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api: ApiStorage by lazy {
            retrofit.create(ApiStorage::class.java)
        }
    }

    @GET("/mobile/internship/v1/osago/rationDetail")
    suspend fun get() : Response<ListCoefficientsGet>

    @POST("/mobile/internship/v1/osago/rationDetail")
    suspend fun send(@Body parameterList: ListParametersPost): Response<ResponseBody>

}