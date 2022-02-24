package ru.ytken.sravni.internship.data.repository

import android.content.Context
import okhttp3.ResponseBody
import retrofit2.Response
import ru.ytken.sravni.internship.data.storage.ApiStorage
import ru.ytken.sravni.internship.data.storage.models.ListCoefficientsGet
import ru.ytken.sravni.internship.data.storage.models.ListParametersPost
import ru.ytken.sravni.internship.domain.repository.ParameterRepository

class ParameterRepositoryImpl(val context: Context) :ParameterRepository {

    override suspend fun getCoefficientsAsync() : Response<ListCoefficientsGet> =
        ApiStorage.api.get()

    override suspend fun saveParameters(listParametersPost: ListParametersPost) : Response<ResponseBody> =
        ApiStorage.api.send(listParametersPost)
}