package ru.ytken.sravni.internship.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ytken.sravni.internship.data.storage.models.insurersactivity.Coefficient
import ru.ytken.sravni.internship.data.storage.models.insurersactivity.ListCoefficientsPost
import ru.ytken.sravni.internship.data.storage.models.insurersactivity.ListInsurersGet
import ru.ytken.sravni.internship.domain.insurersactivity.models.CoefficientParam
import ru.ytken.sravni.internship.domain.insurersactivity.models.InsurerParam
import ru.ytken.sravni.internship.domain.insurersactivity.usecase.GetListOfInsurersUseCase
import ru.ytken.sravni.internship.domain.insurersactivity.usecase.SaveListOfCoefficientsUseCase

class InsurersViewModel(val saveListOfCoefficientsUseCase: SaveListOfCoefficientsUseCase,
    val getListOfInsurersUseCase: GetListOfInsurersUseCase): ViewModel() {

    private var liveListInsurers = MutableLiveData<List<InsurerParam>>(null)
    val listInsurers = liveListInsurers

    private var liveResponseFromApi = MutableLiveData<Boolean>()
    val responseFromApi = liveResponseFromApi

    private lateinit var listCoefficientsPost: ListCoefficientsPost

    fun save(listCoefficientParam:
             List<CoefficientParam>) =
        viewModelScope.launch {
            listCoefficientsPost = listOfCoefficientsParamToListCoefficientsPost(listCoefficientParam)
            val result = saveListOfCoefficientsUseCase.execute(listCoefficientsPost)
            responseFromApi.value = result.isSuccessful
            if(result.isSuccessful)
                load()
        }

    private fun load() = viewModelScope.launch {
        val response = getListOfInsurersUseCase.execute()
        liveListInsurers.value = response.body()?.let { listInsurersGetToListInsurerParam(it) }
    }

    private fun listOfCoefficientsParamToListCoefficientsPost( listCoefficientParam:
        List<CoefficientParam>)
    : ListCoefficientsPost {
        val initArray = listCoefficientParam.toTypedArray()
        val arrayCoefficient: Array<Coefficient> = Array(initArray.size) {
            i -> Coefficient(initArray[i].title, initArray[i].value)
        }
        return ListCoefficientsPost(arrayCoefficient)
    }

    private fun listInsurersGetToListInsurerParam(listInsurersGet: ListInsurersGet) :
            List<InsurerParam> {
        val initList = listInsurersGet.offers
        val listInsurersParam = List<InsurerParam>(initList.size) { index ->
            val element = initList.get(index)
            InsurerParam(
                element.name,
                element.rating,
                element.price,
                element.branding.fontColor,
                element.branding.backgroundColor,
                element.branding.iconTitle,
                element.branding.bankLogoUrlPDF,
                element.branding.bankLogoUrlSVG
            )
        }
        return listInsurersParam
    }
}