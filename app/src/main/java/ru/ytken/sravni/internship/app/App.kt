package ru.ytken.sravni.internship.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.ytken.sravni.internship.di.appModule
import ru.ytken.sravni.internship.di.dataModule
import ru.ytken.sravni.internship.di.domainModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(
                appModule,
                dataModule,
                domainModule
            ))
        }
    }

}