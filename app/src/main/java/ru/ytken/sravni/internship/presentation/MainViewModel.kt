package ru.ytken.sravni.internship.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ytken.sravni.internship.domain.models.CoefficientParam
import ru.ytken.sravni.internship.domain.models.ListCoefficientsParam
import ru.ytken.sravni.internship.domain.models.ListParametersParam
import ru.ytken.sravni.internship.domain.models.ParameterParam
import ru.ytken.sravni.internship.domain.usecase.GetListOfCoefficientsUseCase
import ru.ytken.sravni.internship.domain.usecase.SaveParametersUseCase

class MainViewModel(val getListOfCoefficientsUseCase: GetListOfCoefficientsUseCase,
                    val saveParametersUseCase: SaveParametersUseCase,
                    initListParameters: ListParametersParam,
                    initListCoefficients: ListCoefficientsParam): ViewModel() {

    private var liveListCoefficient = MutableLiveData(initListCoefficients)
    val listCoefficient = liveListCoefficient

    private var liveListParameters = MutableLiveData(initListParameters)
    val listParameters = liveListParameters

    private var liveCurrentFragmentNumber = MutableLiveData<Int>()
    val currentFragmentNumber = liveCurrentFragmentNumber

    fun save(listParametersParam: ListParametersParam) {
        liveListParameters.value = listParametersParam
        val result = saveParametersUseCase.execute(listParametersParam)
    }

    fun load() {
        val coefficients: ListCoefficientsParam = getListOfCoefficientsUseCase.execute()
        liveListCoefficient.value = coefficients
    }

    fun setCurrentFragmentNumber(currentFragmentNumber: Int) {
        liveCurrentFragmentNumber.value = currentFragmentNumber
    }

}