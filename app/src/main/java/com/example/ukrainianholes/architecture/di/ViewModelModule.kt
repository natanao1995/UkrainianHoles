package com.example.ukrainianholes.architecture.di

import com.example.ukrainianholes.feature.home.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModule {
    val viewModelModule = module {
        viewModel { HomeViewModel(get()) }
    }
}