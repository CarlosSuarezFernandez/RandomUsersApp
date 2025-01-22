package com.carlosdev.randomusersapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Street(
    val number: Int,
    val name: String
)
