package com.carlosdev.randomusersapp.domain.usecase

import com.carlosdev.randomusersapp.data.model.User
import com.carlosdev.randomusersapp.domain.repository.IRandomUsersRepository

class GetRandomUsersUsecase(private val iRandomUsersRepository: IRandomUsersRepository) {
    suspend fun execute(): List<User> {
        return iRandomUsersRepository.getUsersFromDatabase()
    }
}