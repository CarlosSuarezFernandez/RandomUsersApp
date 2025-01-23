package com.carlosdev.randomusersapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosdev.randomusersapp.data.model.User
import com.carlosdev.randomusersapp.domain.usecase.DeleteUserUsecase
import com.carlosdev.randomusersapp.domain.usecase.GetNewRandomUsersUsecase
import com.carlosdev.randomusersapp.domain.usecase.GetRandomUsersUsecase
import com.carlosdev.randomusersapp.domain.usecase.GetUserByIDUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class UsersViewModel(
    private val getRandomUsersUsecase: GetRandomUsersUsecase,
    private val getNewRandomUsersUsecase: GetNewRandomUsersUsecase,
    private val deleteUserUsecase: DeleteUserUsecase,
    private val getUserByIDUsecase: GetUserByIDUsecase,
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    private val allUsers = mutableListOf<User>()
    val users: Flow<List<User>>
        get() = _users
    private val _user = MutableStateFlow<User?>(null)
    val user: Flow<User?>
        get() = _user

    fun getUsers() {
        viewModelScope.launch {
            val fetchedUsers = getRandomUsersUsecase.execute()
            allUsers.clear()
            allUsers.addAll(fetchedUsers)
            _users.value = fetchedUsers
        }
    }

    fun getMoreUsers() {
        viewModelScope.launch {
            val moreUsers = getNewRandomUsersUsecase.execute()
            allUsers.clear()
            allUsers.addAll(moreUsers)
            _users.value = allUsers.toList()
        }
    }

    fun deleteUser(user: User) {
    viewModelScope.launch {
        try {
            deleteUserUsecase.execute(user)
            allUsers.remove(user)
            _users.value = allUsers.toList()
        } catch (e: Exception) {
            println("Error deleting user: ${e.message}")
        }
    }
}

    fun filterUsers(searchTerm: String) {
        viewModelScope.launch {
            _users.value = if (searchTerm.isEmpty()) {
                allUsers
            } else {
                allUsers.filter { user ->
                    user.name.first.contains(searchTerm, ignoreCase = true) ||
                            user.name.last.contains(searchTerm, ignoreCase = true) ||
                            user.email.contains(searchTerm, ignoreCase = true)
                }
            }
        }

    }

    fun getUserById(userId: String) {
        viewModelScope.launch {
            _user.value = getUserByIDUsecase.execute(userId)
        }
    }
}