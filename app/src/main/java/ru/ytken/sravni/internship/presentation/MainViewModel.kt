package ru.ytken.sravni.internship.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.ytken.sravni.internship.R
import ru.ytken.sravni.internship.data.storage.models.mainactivity.ListCoefficientsGet
import ru.ytken.sravni.internship.data.storage.models.mainactivity.ListParametersPost
import ru.ytken.sravni.internship.data.storage.models.mainactivity.Parameter
import ru.ytken.sravni.internship.domain.mainactivity.models.CoefficientParamMain
import ru.ytken.sravni.internship.domain.mainactivity.models.ListCoefficientsParam
import ru.ytken.sravni.internship.domain.mainactivity.models.ListParametersParam
import ru.ytken.sravni.internship.domain.mainactivity.models.ParameterParamMain
import ru.ytken.sravni.internship.domain.mainactivity.usecase.GetListOfCoefficientsUseCase
import ru.ytken.sravni.internship.domain.mainactivity.usecase.SaveParametersUseCase

class MainViewModel(val getListOfCoefficientsUseCase: GetListOfCoefficientsUseCase,
                    val saveParametersUseCase: SaveParametersUseCase
): ViewModel() {

    private var liveListCoefficient = MutableLiveData(ListCoefficientsParam())
    val listCoefficient = liveListCoefficient

    private var liveListParameters = MutableLiveData(ListParametersParam())
    val listParameters = liveListParameters

    private var liveCurrentFragmentNumber = MutableLiveData<Int>()
    val currentFragmentNumber = liveCurrentFragmentNumber

    private var liveResponseFromApi = MutableLiveData<Boolean>()
    val responseFromApi = liveResponseFromApi

    fun saveParameter(numberView: Int, value: String) {
        val list = liveListParameters.value?.list
        if (list != null) {
            val param = list[numberView]
            param.value = value
            list[numberView] = param
            liveListParameters.value = ListParametersParam(list)
        }
    }

    fun save() = viewModelScope.launch {
        val result = liveListParameters.value?.let {
            listParametersParamToListParametersPost(it)
        }?.let { saveParametersUseCase.execute(it) }
        if (result != null) {
            responseFromApi.value = result.isSuccessful
            if (result.isSuccessful)
                load()
        }
    }

    private fun load() = viewModelScope.launch {
        val response = getListOfCoefficientsUseCase.execute()
        liveListCoefficient.value = response.body() ?.let { listCoefficientsGetToListCoefficientsParam(it) }
    }

    fun setCurrentFragmentNumber(currentFragmentNumber: Int) {
        liveCurrentFragmentNumber.value = currentFragmentNumber
    }

    private fun listCoefficientsGetToListCoefficientsParam(listCoefficientsGet: ListCoefficientsGet): ListCoefficientsParam {
        val resultRep = listCoefficientsGet.factors
        val listToAdd = ArrayList<CoefficientParamMain>()

        resultRep.forEach {
            listToAdd.add(
                CoefficientParamMain(
                    it.title,
                    it.headerValue,
                    it.name,
                    it.detailText,
                    it.value
                )
            )
        }

        return ListCoefficientsParam(listToAdd)
    }

    private fun listParametersParamToListParametersPost(listParameterParam: ListParametersParam) : ListParametersPost {
        val initArray = listParameterParam.list
        val arrayParameter: Array<Parameter> = Array(initArray.size) {
                i -> Parameter(initArray[i].title, initArray[i].value)
        }
        return ListParametersPost(arrayParameter)
    }

}