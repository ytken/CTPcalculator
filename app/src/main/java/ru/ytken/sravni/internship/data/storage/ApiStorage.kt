package ru.ytken.sravni.internship.data.storage

import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import ru.ytken.sravni.internship.data.storage.models.ListCoefficientsGet
import ru.ytken.sravni.internship.data.storage.models.ListParametersPost

interface ApiStorage {

    companion object {
        private const val BASE_URL = "http://mock.sravni-team.ru"

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api: ApiStorage by lazy {
            retrofit.create(ApiStorage::class.java)
        }
    }

    @GET("/mobile/internship/v1/osago/rationDetail")
    suspend fun get() : Response<ListCoefficientsGet>

    fun send(parameterList: ListParametersPost): Boolean

}