package com.carlosdev.randomusersapp.data.repository

import com.carlosdev.randomusersapp.data.local.RealmDatabase
import com.carlosdev.randomusersapp.data.model.User
import com.carlosdev.randomusersapp.data.remote.IRandomUserApi
import com.carlosdev.randomusersapp.domain.repository.IRandomUsersRepository

class RandomUsersRepository(
    private val randomUserApi: IRandomUserApi,
    private val realmDatabase: RealmDatabase
) : IRandomUsersRepository {
    override suspend fun getUsersFromApi(): List<User> {
        val users = randomUserApi.getUsers().results
        saveUsers(users)
        return users
    }

    override suspend fun getUsersFromDatabase(): List<User> {
        if (realmDatabase.getUsers().isEmpty()) {
            getUsersFromApi()
            return realmDatabase.getUsers()
        } else {
            return realmDatabase.getUsers()
        }
    }

    override suspend fun getNewUsers(): List<User> {
        val newUsers = getUsersFromApi()
        saveUsers(newUsers)
        return getUsersFromDatabase()
    }

    override suspend fun saveUsers(users: List<User>) {
        realmDatabase.storeUsers(users)
    }

    override suspend fun deleteUser(user: User) {
        realmDatabase.markUserAsDeleted(user)
    }

    override suspend fun getUserById(id: String): User {
        val user = realmDatabase.getUserById(id)
        if (user != null) {
            return user
        } else {
            throw Exception("User not found")
        }
    }
}