package com.carlosdev.randomusersapp.data.remote

import com.carlosdev.randomusersapp.data.model.RandomUserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class RandomUserApi : IRandomUserApi {
    override suspend fun getUsers(): RandomUserResponse {
        val client = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
        val url = BASE_URL
        return client.get(url).body()
    }

    companion object{
        private val BASE_URL = "http://api.randomuser.me/?results=40"
    }
}