package ru.ytken.sravni.internship.domain.usecase

import kotlinx.coroutines.Deferred
import retrofit2.Response
import ru.ytken.sravni.internship.data.storage.models.ListCoefficientsGet
import ru.ytken.sravni.internship.domain.repository.ParameterRepository

class GetListOfCoefficientsUseCase(private val parameterRepository: ParameterRepository) {

    suspend fun execute(): Response<ListCoefficientsGet> {
        return parameterRepository.getCoefficientsAsync()
    }

}