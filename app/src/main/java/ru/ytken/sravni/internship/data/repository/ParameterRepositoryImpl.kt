package ru.ytken.sravni.internship.data.repository

import android.content.Context
import okhttp3.ResponseBody
import retrofit2.Response
import ru.ytken.sravni.internship.data.storage.ApiStorage
import ru.ytken.sravni.internship.data.storage.models.insurersactivity.ListCoefficientsPost
import ru.ytken.sravni.internship.data.storage.models.insurersactivity.ListInsurersGet
import ru.ytken.sravni.internship.data.storage.models.mainactivity.ListCoefficientsGet
import ru.ytken.sravni.internship.data.storage.models.mainactivity.ListParametersPost
import ru.ytken.sravni.internship.domain.mainactivity.repository.ParameterRepository

class ParameterRepositoryImpl(val context: Context) :ParameterRepository {

    override suspend fun getCoefficientsAsync() : Response<ListCoefficientsGet> =
        ApiStorage.api.get()

    override suspend fun getInsurersAsync(): Response<ListInsurersGet> =
        ApiStorage.api.getInsurers()

    override suspend fun saveParameters(listParametersPost: ListParametersPost) : Response<ResponseBody> =
        ApiStorage.api.send(listParametersPost)

    override suspend fun saveCoefficients(listCoefficientsPost: ListCoefficientsPost): Response<ResponseBody> =
        ApiStorage.api.sendCoeffs(listCoefficientsPost)

}