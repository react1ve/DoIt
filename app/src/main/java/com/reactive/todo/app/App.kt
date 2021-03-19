package com.reactive.todo.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.reactive.todo.BuildConfig
import com.reactive.todo.di.applicationModule
import com.reactive.todo.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        initKoin()

        initLogger()
    }

    private fun initKoin() {

        //Initialising Koin
        startKoin {
            androidContext(this@App)
            modules(listOf(applicationModule, viewModelModule))
        }
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

}