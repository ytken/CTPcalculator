package ru.ytken.sravni.internship.data.storage

import ru.ytken.sravni.internship.data.storage.models.Coefficient
import ru.ytken.sravni.internship.data.storage.models.ListCoefficientsGet
import ru.ytken.sravni.internship.data.storage.models.ListParametersPost
import ru.ytken.sravni.internship.data.storage.models.Parameter

interface ParameterStorage {

    fun send(parameterList: ListParametersPost): Boolean

    fun get(): ListCoefficientsGet

}