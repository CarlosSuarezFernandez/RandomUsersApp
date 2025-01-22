package com.carlosdev.randomusersapp.domain.repository

import com.carlosdev.randomusersapp.data.model.User

interface IRandomUsersRepository {

    suspend fun getUsersFromApi(): List<User>
    suspend fun getUsersFromDatabase(): List<User>
    suspend fun getNewUsers(): List<User>
    suspend fun saveUsers(users: List<User>)
    suspend fun deleteUser(user: User)
    suspend fun getUserById(id: String): User
}