package com.carlosdev.randomusersapp.domain.usecase

import com.carlosdev.randomusersapp.data.model.User
import com.carlosdev.randomusersapp.domain.repository.IRandomUsersRepository

class GetUserByIDUsecase(private val iRandomUsersRepository: IRandomUsersRepository) {
    suspend fun execute(id: String): User {
        return iRandomUsersRepository.getUserById(id)
    }
}