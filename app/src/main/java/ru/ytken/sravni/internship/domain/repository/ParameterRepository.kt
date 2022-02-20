package ru.ytken.sravni.internship.domain.repository

import ru.ytken.sravni.internship.domain.models.CoefficientParam
import ru.ytken.sravni.internship.domain.models.ListCoefficientsParam
import ru.ytken.sravni.internship.domain.models.ListParametersParam
import ru.ytken.sravni.internship.domain.models.ParameterParam

interface ParameterRepository {

    fun getCoefficients() : ListCoefficientsParam

    fun saveParameters(listParameterParam: ListParametersParam): Boolean

}