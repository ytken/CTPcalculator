package ru.ytken.sravni.internship.domain.mainactivity.repository

import okhttp3.ResponseBody
import retrofit2.Response
import ru.ytken.sravni.internship.data.storage.models.insurersactivity.ListCoefficientsPost
import ru.ytken.sravni.internship.data.storage.models.insurersactivity.ListInsurersGet
import ru.ytken.sravni.internship.data.storage.models.mainactivity.ListCoefficientsGet
import ru.ytken.sravni.internship.data.storage.models.mainactivity.ListParametersPost

interface ParameterRepository {

    suspend fun getCoefficientsAsync() : Response<ListCoefficientsGet>

    suspend fun getInsurersAsync() : Response<ListInsurersGet>

    suspend fun saveParameters(listParametersPost: ListParametersPost): Response<ResponseBody>

    suspend fun saveCoefficients(listCoefficientsPost: ListCoefficientsPost) : Response<ResponseBody>

}