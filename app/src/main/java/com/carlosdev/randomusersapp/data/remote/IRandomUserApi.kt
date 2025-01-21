package com.carlosdev.randomusersapp.data.remote

import com.carlosdev.randomusersapp.data.model.RandomUserResponse

interface IRandomUserApi {
    suspend fun getUsers(): RandomUserResponse
}