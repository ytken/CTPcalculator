package ru.ytken.sravni.internship.data.repository

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.ytken.sravni.internship.R
import ru.ytken.sravni.internship.data.storage.ApiStorage
import ru.ytken.sravni.internship.data.storage.models.ListCoefficientsGet
import ru.ytken.sravni.internship.data.storage.models.ListParametersPost
import ru.ytken.sravni.internship.domain.repository.ParameterRepository

class ParameterRepositoryImpl(val context: Context) :ParameterRepository {

    override suspend fun getCoefficientsAsync() : Response<ListCoefficientsGet> =
        CoroutineScope(Dispatchers.IO).async {
            Log.d(context.getString(R.string.TAG_API), "Calling Api...")
            ApiStorage.api.get()
        }.await()

    override suspend fun saveParameters(listParametersPost: ListParametersPost) : Response<ResponseBody> =
        CoroutineScope(Dispatchers.IO).async {
            ApiStorage.api.send(listParametersPost)
        }.await()
}