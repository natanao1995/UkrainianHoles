package com.example.ukrainianholes.architecture.di

import com.example.ukrainianholes.feature.add_hole.add.AddHoleInteractor
import com.example.ukrainianholes.feature.add_hole.map.MapInteractor
import com.example.ukrainianholes.feature.all_holes.AllHolesInteractor
import com.example.ukrainianholes.feature.home.HomeInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object InteractorModule {
    val interactorModule = module {
        factory { HomeInteractor(get()) }
        factory { MapInteractor(androidContext(), get(), get()) }
        factory { AllHolesInteractor(get()) }
        factory { AddHoleInteractor(get()) }
    }
}