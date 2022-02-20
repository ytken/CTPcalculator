package ru.ytken.sravni.internship.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.ytken.sravni.internship.R
import ru.ytken.sravni.internship.data.storage.models.Coefficient
import ru.ytken.sravni.internship.data.storage.models.ListCoefficientsGet
import ru.ytken.sravni.internship.data.storage.models.ListParametersPost
import ru.ytken.sravni.internship.data.storage.models.Parameter
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
        //val result = saveParametersUseCase.execute(listParametersParamToListParametersPost(listParametersParam))
    }

    fun load() = viewModelScope.launch {
        val response = getListOfCoefficientsUseCase.execute()
        liveListCoefficient.value = response.body() ?.let { listCoefficientsGetToListCoefficientsParam(it) }
    }

    fun setCurrentFragmentNumber(currentFragmentNumber: Int) {
        liveCurrentFragmentNumber.value = currentFragmentNumber
    }

    private fun listCoefficientsGetToListCoefficientsParam(listCoefficientsGet: ListCoefficientsGet): ListCoefficientsParam {
        val resultRep = listCoefficientsGet.factors

        val BTcoeff = findMatchElement(resultRep, "БТ")[0]
        val KMcoeff = findMatchElement(resultRep, "КМ")[0]
        val KTcoeff = findMatchElement(resultRep, "КТ")[0]
        val KBMcoeff = findMatchElement(resultRep, "КБМ")[0]
        val KVScoeff = findMatchElement(resultRep, "КВС")[0]
        val KOcoeff = findMatchElement(resultRep, "КО")[0]

        var result =
            ListCoefficientsParam(
                BT = CoefficientParam(BTcoeff.title, BTcoeff.headerValue,
                    BTcoeff.name, BTcoeff.detailText, BTcoeff.value),
                KM = CoefficientParam(KMcoeff.title, KMcoeff.headerValue,
                    KMcoeff.name, KMcoeff.detailText, KMcoeff.value),
                KT = CoefficientParam(KTcoeff.title, KTcoeff.headerValue,
                    KTcoeff.name, KTcoeff.detailText, KTcoeff.value),
                KBM = CoefficientParam(KBMcoeff.title, KBMcoeff.headerValue,
                    KBMcoeff.name, KBMcoeff.detailText, KBMcoeff.value),
                KVS = CoefficientParam(KVScoeff.title, KVScoeff.headerValue,
                    KVScoeff.name, KVScoeff.detailText, KVScoeff.value),
                KO = CoefficientParam(KOcoeff.title, KOcoeff.headerValue,
                    KOcoeff.name, KOcoeff.detailText, KOcoeff.value)
            )
        List(resultRep.size)
        {i -> {
            val coefficient = resultRep.get(i)
            CoefficientParam(coefficient.title, coefficient.headerValue, coefficient.value, coefficient.name, coefficient.detailText) }
        }
        return result
    }

    private fun findMatchElement(listCoefficients: Array<Coefficient>, title: String): List<Coefficient> {
        return listCoefficients.filter { it.title.equals(title) }
    }

    private fun listParametersParamToListParametersPost(listParameterParam: ListParametersParam) : ListParametersPost {
        return ListParametersPost(
            List(listParameterParam.getSize())
            {i -> {
                val parameter : ParameterParam = listParameterParam.getElementById(i)
                Parameter(parameter.title, parameter.value)
            }
            }
        )
    }

}