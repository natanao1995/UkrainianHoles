package com.example.ukrainianholes.architecture.di

import com.example.ukrainianholes.feature.add_hole.map.MapViewModel
import com.example.ukrainianholes.feature.all_holes.AllHolesViewModel
import com.example.ukrainianholes.feature.details.HoleDetailsViewModel
import com.example.ukrainianholes.feature.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModule {
    val viewModelModule = module {
        viewModel { HomeViewModel(get()) }
        viewModel { MapViewModel(androidContext(), get()) }
        viewModel { AllHolesViewModel(get()) }
        viewModel { HoleDetailsViewModel(get()) }
    }
}