package ru.ytken.sravni.internship.domain.mainactivity.usecase

import retrofit2.Response
import ru.ytken.sravni.internship.data.storage.models.mainactivity.ListCoefficientsGet
import ru.ytken.sravni.internship.domain.mainactivity.repository.ParameterRepository

class GetListOfCoefficientsUseCase(private val parameterRepository: ParameterRepository) {

    suspend fun execute(): Response<ListCoefficientsGet> {
        return parameterRepository.getCoefficientsAsync()
    }

}