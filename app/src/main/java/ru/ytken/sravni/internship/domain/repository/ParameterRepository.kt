package ru.ytken.sravni.internship.domain.repository

import ru.ytken.sravni.internship.domain.models.ListOfCoefficientsParam
import ru.ytken.sravni.internship.domain.models.SaveParametersParam

interface ParameterRepository {

    fun getCoefficients() : ListOfCoefficientsParam

    fun saveParameters(saveParametersParam: SaveParametersParam): Boolean

}