package com.example.ukrainianholes.architecture

import android.app.Application
import com.example.ukrainianholes.architecture.di.AuthModule.authModule
import com.example.ukrainianholes.architecture.di.InteractorModule.interactorModule
import com.example.ukrainianholes.architecture.di.NetworkModule.networkModule
import com.example.ukrainianholes.architecture.di.ViewModelModule.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BaseApplication)

            modules(
                authModule,
                viewModelModule,
                interactorModule,
                networkModule
            )
        }
    }
}