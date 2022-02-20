package ru.ytken.sravni.internship.data.storage

class ApiHelper(private val retrofitApi: RetrofitApi) {

    suspend fun getCoefficients() = retrofitApi.get()

}