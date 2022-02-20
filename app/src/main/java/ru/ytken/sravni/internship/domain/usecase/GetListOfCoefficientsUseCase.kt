package ru.ytken.sravni.internship.domain.usecase

import ru.ytken.sravni.internship.domain.models.CoefficientParam
import ru.ytken.sravni.internship.domain.models.ListCoefficientsParam
import ru.ytken.sravni.internship.domain.repository.ParameterRepository

class GetListOfCoefficientsUseCase(private val parameterRepository: ParameterRepository) {

    fun execute(): ListCoefficientsParam {
        return parameterRepository.getCoefficients()
    }

}