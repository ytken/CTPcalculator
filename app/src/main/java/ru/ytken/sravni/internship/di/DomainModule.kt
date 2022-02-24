package ru.ytken.sravni.internship.di

import org.koin.dsl.module
import ru.ytken.sravni.internship.domain.usecase.GetListOfCoefficientsUseCase
import ru.ytken.sravni.internship.domain.usecase.SaveParametersUseCase

val domainModule = module {

    factory<GetListOfCoefficientsUseCase> {
        GetListOfCoefficientsUseCase(parameterRepository = get())
    }

    factory<SaveParametersUseCase> {
        SaveParametersUseCase(parameterRepository = get())
    }

}