package ru.ytken.sravni.internship.data.storage

import retrofit2.http.GET
import ru.ytken.sravni.internship.data.storage.models.ListCoefficientsGet
import ru.ytken.sravni.internship.data.storage.models.ListParametersPost

interface RetrofitApi: ParameterStorage {

    override fun send(parameterList: ListParametersPost): Boolean

    @GET("mobile/internship/v1/osago/rationDetail")
    override fun get(): ListCoefficientsGet
}