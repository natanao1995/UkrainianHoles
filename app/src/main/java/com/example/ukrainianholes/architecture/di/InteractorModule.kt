package com.example.ukrainianholes.architecture.di

import com.example.ukrainianholes.feature.home.HomeInteractor
import org.koin.dsl.module

object InteractorModule {
    val interactorModule = module {
        factory { HomeInteractor(get()) }
    }
}