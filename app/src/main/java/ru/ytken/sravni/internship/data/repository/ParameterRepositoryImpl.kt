package ru.ytken.sravni.internship.data.repository

import ru.ytken.sravni.internship.data.storage.models.Parameter
import ru.ytken.sravni.internship.data.storage.ParameterStorage
import ru.ytken.sravni.internship.domain.models.ListOfCoefficientsParam
import ru.ytken.sravni.internship.domain.models.SaveParametersParam
import ru.ytken.sravni.internship.domain.repository.ParameterRepository

class ParameterRepositoryImpl(private val parameterStorage: ParameterStorage) :ParameterRepository {

    override fun getCoefficients() : ListOfCoefficientsParam {
        val result = parameterStorage.get()
        return ListOfCoefficientsParam(result.BT, result.KM,
            result.KT, result.KBM, result.KVS, result.KO)
    }

    override fun saveParameters(saveParametersParam: SaveParametersParam): Boolean {
        return parameterStorage.send(
            Parameter(saveParametersParam.userCity,
            saveParametersParam.userCapacity,saveParametersParam.userNumber,
            saveParametersParam.userYoungest, saveParametersParam.userMinExp,
            saveParametersParam.userNoCrush)
        )
    }

}