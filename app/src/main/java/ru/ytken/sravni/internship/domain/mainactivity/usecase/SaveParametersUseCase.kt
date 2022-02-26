package ru.ytken.sravni.internship.domain.mainactivity.usecase

import okhttp3.ResponseBody
import retrofit2.Response
import ru.ytken.sravni.internship.data.storage.models.mainactivity.ListParametersPost
import ru.ytken.sravni.internship.domain.mainactivity.repository.ParameterRepository

class SaveParametersUseCase(private val parameterRepository: ParameterRepository) {

    suspend fun execute(listParameterPost: ListParametersPost): Response<ResponseBody> {
        return parameterRepository.saveParameters(listParametersPost = listParameterPost)
    }

}