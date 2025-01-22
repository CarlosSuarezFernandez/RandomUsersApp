package com.carlosdev.randomusersapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RandomUserResponse(
    val results: List<User>,
    val info: Info
)
