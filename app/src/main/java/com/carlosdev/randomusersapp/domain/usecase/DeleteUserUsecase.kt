package com.carlosdev.randomusersapp.domain.usecase

import com.carlosdev.randomusersapp.data.model.User
import com.carlosdev.randomusersapp.domain.repository.IRandomUsersRepository

class DeleteUserUsecase(private val iRandomUsersRepository: IRandomUsersRepository) {
    suspend fun execute(user: User){
        iRandomUsersRepository.deleteUser(user)
    }
}