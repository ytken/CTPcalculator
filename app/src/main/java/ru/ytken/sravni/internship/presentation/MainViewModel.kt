package ru.ytken.sravni.internship.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.ytken.sravni.internship.data.storage.models.mainactivity.ListCoefficientsGet
import ru.ytken.sravni.internship.data.storage.models.mainactivity.ListParametersPost
import ru.ytken.sravni.internship.data.storage.models.mainactivity.Parameter
import ru.ytken.sravni.internship.domain.mainactivity.models.CoefficientParamMain
import ru.ytken.sravni.internship.domain.mainactivity.models.ListCoefficientsParam
import ru.ytken.sravni.internship.domain.mainactivity.models.ListParametersParam
import ru.ytken.sravni.internship.domain.mainactivity.usecase.GetListOfCoefficientsUseCase
import ru.ytken.sravni.internship.domain.mainactivity.usecase.SaveParametersUseCase

class MainViewModel(val getListOfCoefficientsUseCase: GetListOfCoefficientsUseCase,
                    val saveParametersUseCase: SaveParametersUseCase
): ViewModel() {

    private var liveListCoefficient = MutableLiveData(ListCoefficientsParam())
    val listCoefficient:LiveData<ListCoefficientsParam> = liveListCoefficient

    private var liveListParameters = MutableLiveData(ListParametersParam())
    val listParameters:LiveData<ListParametersParam> = liveListParameters

    var listExpanded = MutableLiveData<Boolean>(false)

    fun initLists(context: Context) {
        liveListCoefficient.value?.initList(context)
        liveListParameters.value?.initList(context)
    }

    fun saveParameter(numberView: Int, value: String) {
        val list = liveListParameters.value?.list
        if (list != null && numberView < list.size) {
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
            if (result.isSuccessful)
                load()
        }
    }

    private fun load() = viewModelScope.launch {
        val response = getListOfCoefficientsUseCase.execute()
        liveListCoefficient.value = response.body() ?.let { listCoefficientsGetToListCoefficientsParam(it) }
    }

    private fun listCoefficientsGetToListCoefficientsParam(listCoefficientsGet: ListCoefficientsGet): ListCoefficientsParam {
        return ListCoefficientsParam(listCoefficientsGet.factors.map {
            CoefficientParamMain(
                it.title,
                it.headerValue,
                it.name,
                it.detailText,
                it.value
            )
        } as ArrayList<CoefficientParamMain>)
    }

    private fun listParametersParamToListParametersPost(listParameterParam: ListParametersParam) : ListParametersPost {
        return ListParametersPost(listParameterParam.list.map {
            Parameter(it.title, it.value)
        }.toTypedArray())
    }

}