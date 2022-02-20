package ru.ytken.sravni.internship.domain.usecase

import ru.ytken.sravni.internship.domain.models.ListParametersParam
import ru.ytken.sravni.internship.domain.models.ParameterParam
import ru.ytken.sravni.internship.domain.repository.ParameterRepository

class SaveParametersUseCase(private val parameterRepository: ParameterRepository) {

    fun execute(listParameterParam: ListParametersParam): Boolean {
        return parameterRepository.saveParameters(listParameterParam = listParameterParam)
    }

}