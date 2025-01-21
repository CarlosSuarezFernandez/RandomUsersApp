package com.carlosdev.randomusersapp.di

import com.carlosdev.randomusersapp.data.remote.IRandomUserApi
import com.carlosdev.randomusersapp.data.remote.RandomUserApi
import com.carlosdev.randomusersapp.data.repository.RandomUsersRepository
import com.carlosdev.randomusersapp.domain.repository.IRandomUsersRepository
import com.carlosdev.randomusersapp.domain.usecase.GetRandomUsersUsecase
import org.koin.dsl.module

val appModule = module {
    factory {
        GetRandomUsersUsecase(get())
    }
    single<IRandomUsersRepository> {
        RandomUsersRepository(get(), get())
    }
    single<IRandomUserApi> {
        RandomUserApi()
    }

}