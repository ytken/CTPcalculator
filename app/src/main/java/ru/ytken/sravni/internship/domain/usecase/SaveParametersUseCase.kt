package ru.ytken.sravni.internship.domain.usecase

import ru.ytken.sravni.internship.data.storage.models.ListParametersPost
import ru.ytken.sravni.internship.domain.repository.ParameterRepository

class SaveParametersUseCase(private val parameterRepository: ParameterRepository) {

    fun execute(listParameterPost: ListParametersPost): Boolean {
        return parameterRepository.saveParameters(listParametersPost = listParameterPost)
    }

}