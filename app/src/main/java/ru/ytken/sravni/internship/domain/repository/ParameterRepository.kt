package ru.ytken.sravni.internship.domain.repository

import kotlinx.coroutines.Deferred
import retrofit2.Response
import ru.ytken.sravni.internship.data.storage.models.ListCoefficientsGet
import ru.ytken.sravni.internship.data.storage.models.ListParametersPost

interface ParameterRepository {

    suspend fun getCoefficientsAsync() : Response<ListCoefficientsGet>

    fun saveParameters(listParametersPost: ListParametersPost): Boolean

}