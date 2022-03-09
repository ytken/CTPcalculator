package ru.ytken.sravni.internship.presentation

import androidx.lifecycle.LiveData
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
    val getListOfInsurersUseCase: GetListOfInsurersUseCase
    ): ViewModel() {

    private var liveListInsurers = MutableLiveData<List<InsurerParam>>(null)
    val listInsurers: LiveData<List<InsurerParam>> = liveListInsurers

    var listExpanded = MutableLiveData<Boolean>(false)

    private lateinit var listCoefficientsPost: ListCoefficientsPost

    fun save(listCoefficientParam:
             List<CoefficientParam>) =
        viewModelScope.launch {
            listCoefficientsPost = listOfCoefficientsParamToListCoefficientsPost(listCoefficientParam)
            val result = saveListOfCoefficientsUseCase.execute(listCoefficientsPost)
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
        return ListCoefficientsPost(listCoefficientParam.toTypedArray().map {
            Coefficient(it.title, it.value)
        }.toTypedArray())
    }

    private fun listInsurersGetToListInsurerParam(listInsurersGet: ListInsurersGet) :
            List<InsurerParam> {
        return listInsurersGet.offers.map {
            InsurerParam(
                it.name,
                it.rating,
                it.price,
                it.branding.fontColor,
                it.branding.backgroundColor,
                it.branding.iconTitle,
                it.branding.bankLogoUrlPDF,
                it.branding.bankLogoUrlSVG
            )
        }
    }
}