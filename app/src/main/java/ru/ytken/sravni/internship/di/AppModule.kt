package ru.ytken.sravni.internship.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.ytken.sravni.internship.presentation.InsurersViewModel
import ru.ytken.sravni.internship.presentation.MainViewModel

val appModule = module {

    viewModel<MainViewModel> {
        MainViewModel(
            getListOfCoefficientsUseCase = get(),
            saveParametersUseCase = get()
        )
    }

    viewModel<InsurersViewModel> {
        InsurersViewModel(
            saveListOfCoefficientsUseCase = get(),
            getListOfInsurersUseCase = get()
        )
    }

}