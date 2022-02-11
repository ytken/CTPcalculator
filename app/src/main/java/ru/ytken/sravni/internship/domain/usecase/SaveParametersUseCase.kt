package ru.ytken.sravni.internship.domain.usecase

import ru.ytken.sravni.internship.domain.models.SaveParametersParam
import ru.ytken.sravni.internship.domain.repository.ParameterRepository

class SaveParametersUseCase(private val parameterRepository: ParameterRepository) {

    fun execute(saveParametersParam: SaveParametersParam): Boolean {
        return parameterRepository.saveParameters(saveParametersParam = saveParametersParam)
    }

}