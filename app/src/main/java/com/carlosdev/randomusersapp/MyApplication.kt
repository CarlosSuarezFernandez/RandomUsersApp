package com.carlosdev.randomusersapp

import android.app.Application
import com.carlosdev.randomusersapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.androix.startup.KoinStartup.onKoinStartup

class RandomUsersApp: Application() {

    init {
        // Use AndroidX Startup for Koin
        onKoinStartup {
            androidContext(this@RandomUsersApp)
            modules(appModule)
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}