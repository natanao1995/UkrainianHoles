package com.example.ukrainianholes.architecture.di

import com.example.ukrainianholes.feature.home.HomeViewModel
import com.example.ukrainianholes.feature.map.MapViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModule {
    val viewModelModule = module {
        viewModel { HomeViewModel(get()) }
        viewModel { MapViewModel(androidContext(), get()) }
    }
}