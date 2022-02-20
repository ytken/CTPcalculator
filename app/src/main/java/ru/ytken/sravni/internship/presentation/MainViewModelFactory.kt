package ru.ytken.sravni.internship.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.ytken.sravni.internship.data.repository.ParameterRepositoryImpl
import ru.ytken.sravni.internship.data.storage.ServerParameterStorage
import ru.ytken.sravni.internship.domain.models.ListCoefficientsParam
import ru.ytken.sravni.internship.domain.models.ListParametersParam
import ru.ytken.sravni.internship.domain.usecase.GetListOfCoefficientsUseCase
import ru.ytken.sravni.internship.domain.usecase.SaveParametersUseCase

class MainViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val parameterRepository =
        ParameterRepositoryImpl(parameterStorage = ServerParameterStorage(context = context))
    private val getListOfCoefficientsUseCase =
        GetListOfCoefficientsUseCase(parameterRepository = parameterRepository)
    private val saveParametersUseCase =
        SaveParametersUseCase(parameterRepository = parameterRepository)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            getListOfCoefficientsUseCase = getListOfCoefficientsUseCase,
            saveParametersUseCase = saveParametersUseCase,
            initListCoefficients = ListCoefficientsParam(),
            initListParameters = ListParametersParam()
        ) as T
    }
}