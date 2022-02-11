package ru.ytken.sravni.internship.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ytken.sravni.internship.R
import ru.ytken.sravni.internship.domain.models.ListOfCoefficientsParam
import ru.ytken.sravni.internship.domain.models.SaveParametersParam
import ru.ytken.sravni.internship.domain.usecase.GetListOfCoefficientsUseCase
import ru.ytken.sravni.internship.domain.usecase.SaveParametersUseCase

class MainViewModel(val getListOfCoefficientsUseCase: GetListOfCoefficientsUseCase,
                    val saveParametersUseCase: SaveParametersUseCase): ViewModel() {

    private var liveListCoefficient = MutableLiveData<Array<String>>()
    val listCoefficient = liveListCoefficient

    private var liveListParameters = MutableLiveData<Array<String>>()
    val listParameters = liveListParameters

    fun save(userParams: Array<String>) {
        liveListParameters.value = userParams
        val result = sendParametersToRepository(userParams)
    }

    private fun sendParametersToRepository(userParams: Array<String>) : Boolean {
        if(userParams.none { x -> x.isEmpty() } ) {
            val parameters = SaveParametersParam(
                userCity = userParams[0],
                userCapacity = userParams[1],
                userNumber = userParams[2],
                userYoungest = userParams[3],
                userMinExp = userParams[4],
                userNoCrush = userParams[5])
            val result = saveParametersUseCase.execute(parameters)
            return result
        }
        return false
    }

    fun load() {
        val coefficients: ListOfCoefficientsParam = getListOfCoefficientsUseCase.execute()
        liveListCoefficient.value = arrayOf(coefficients.BT, coefficients.KM,
            coefficients.KT, coefficients.KBM, coefficients.KVS, coefficients.KO)
    }

}