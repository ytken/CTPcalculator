package ru.ytken.sravni.internship.domain.insurersactivity.usecase

import okhttp3.ResponseBody
import retrofit2.Response
import ru.ytken.sravni.internship.data.storage.models.insurersactivity.ListCoefficientsPost
import ru.ytken.sravni.internship.domain.mainactivity.repository.ParameterRepository

class SaveListOfCoefficientsUseCase(private val parameterRepository: ParameterRepository) {

    suspend fun execute(listCoefficientsPost: ListCoefficientsPost): Response<ResponseBody> {
        return parameterRepository.saveCoefficients(listCoefficientsPost = listCoefficientsPost)
    }

}