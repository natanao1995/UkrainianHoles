package com.example.ukrainianholes.architecture.di

import com.example.ukrainianholes.data.User
import org.koin.dsl.module

object AuthModule {
    val authModule = module {
        single { getUser() }
    }

    private fun getUser(): User {
        return User()
    }
}