package ru.ytken.sravni.internship.domain.usecase

import ru.ytken.sravni.internship.domain.models.ListOfCoefficientsParam
import ru.ytken.sravni.internship.domain.repository.ParameterRepository

class GetListOfCoefficientsUseCase(private val parameterRepository: ParameterRepository) {

    fun execute(): ListOfCoefficientsParam {
        return parameterRepository.getCoefficients()
    }

}