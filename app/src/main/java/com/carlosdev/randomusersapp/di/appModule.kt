package com.carlosdev.randomusersapp.di

import com.carlosdev.randomusersapp.data.local.RealmDatabase
import com.carlosdev.randomusersapp.data.remote.IRandomUserApi
import com.carlosdev.randomusersapp.data.remote.RandomUserApi
import com.carlosdev.randomusersapp.data.repository.RandomUsersRepository
import com.carlosdev.randomusersapp.domain.repository.IRandomUsersRepository
import com.carlosdev.randomusersapp.domain.usecase.DeleteUserUsecase
import com.carlosdev.randomusersapp.domain.usecase.GetNewRandomUsersUsecase
import com.carlosdev.randomusersapp.domain.usecase.GetRandomUsersUsecase
import com.carlosdev.randomusersapp.domain.usecase.GetUserByIDUsecase
import com.carlosdev.randomusersapp.presentation.UsersViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory {
        GetRandomUsersUsecase(get())
    }

    factory {
        GetNewRandomUsersUsecase(get())
    }

    factory {
        DeleteUserUsecase(get())
    }

    factory {
        GetUserByIDUsecase(get())
    }

    single<IRandomUsersRepository> {
        RandomUsersRepository(get(), get())
    }

    single<IRandomUserApi> {
        RandomUserApi()
    }

    single { RealmDatabase() }

    viewModel { UsersViewModel(get(), get(), get(), get()) }

}