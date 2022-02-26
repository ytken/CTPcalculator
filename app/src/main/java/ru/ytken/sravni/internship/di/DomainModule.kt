package ru.ytken.sravni.internship.di

import org.koin.dsl.module
import ru.ytken.sravni.internship.domain.insurersactivity.usecase.GetListOfInsurersUseCase
import ru.ytken.sravni.internship.domain.insurersactivity.usecase.SaveListOfCoefficientsUseCase
import ru.ytken.sravni.internship.domain.mainactivity.usecase.GetListOfCoefficientsUseCase
import ru.ytken.sravni.internship.domain.mainactivity.usecase.SaveParametersUseCase

val domainModule = module {

    factory<GetListOfCoefficientsUseCase> {
        GetListOfCoefficientsUseCase(parameterRepository = get())
    }

    factory<SaveParametersUseCase> {
        SaveParametersUseCase(parameterRepository = get())
    }

    factory<GetListOfInsurersUseCase> {
        GetListOfInsurersUseCase(parameterRepository = get())
    }

    factory<SaveListOfCoefficientsUseCase> {
        SaveListOfCoefficientsUseCase(parameterRepository = get())
    }

}