package ru.ytken.sravni.internship.data.repository

import ru.ytken.sravni.internship.data.storage.ParameterStorage
import ru.ytken.sravni.internship.data.storage.models.Coefficient
import ru.ytken.sravni.internship.data.storage.models.ListParametersPost
import ru.ytken.sravni.internship.data.storage.models.Parameter
import ru.ytken.sravni.internship.domain.models.CoefficientParam
import ru.ytken.sravni.internship.domain.models.ListCoefficientsParam
import ru.ytken.sravni.internship.domain.models.ListParametersParam
import ru.ytken.sravni.internship.domain.models.ParameterParam
import ru.ytken.sravni.internship.domain.repository.ParameterRepository

class ParameterRepositoryImpl(private val parameterStorage: ParameterStorage) :ParameterRepository {

    override fun getCoefficients() : ListCoefficientsParam {

        val resultRep = parameterStorage.get().factors

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

    private fun findMatchElement(listCoefficients: List<Coefficient>, title: String): List<Coefficient> {
        return listCoefficients.filter { it.title.equals(title) }
    }

    override fun saveParameters(listParameterParam: ListParametersParam): Boolean {
        return parameterStorage.send(
            ListParametersPost(
                List(listParameterParam.getSize())
                {i -> {
                    val parameter = listParameterParam.getElementById(i)
                    Parameter(parameter.title, parameter.value)
                }
                }
            )
        )
    }

}