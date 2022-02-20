package ru.ytken.sravni.internship.domain.repository

import okhttp3.ResponseBody
import retrofit2.Response
import ru.ytken.sravni.internship.data.storage.models.ListCoefficientsGet
import ru.ytken.sravni.internship.data.storage.models.ListParametersPost

interface ParameterRepository {

    suspend fun getCoefficientsAsync() : Response<ListCoefficientsGet>

    suspend fun saveParameters(listParametersPost: ListParametersPost): Response<ResponseBody>

}