package ru.ytken.sravni.internship.di

import org.koin.dsl.module
import ru.ytken.sravni.internship.data.repository.ParameterRepositoryImpl
import ru.ytken.sravni.internship.domain.mainactivity.repository.ParameterRepository

val dataModule = module {

    single<ParameterRepository> {
        ParameterRepositoryImpl(context = get())
    }

}