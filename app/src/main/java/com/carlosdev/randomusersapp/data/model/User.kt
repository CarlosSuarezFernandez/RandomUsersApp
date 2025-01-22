package com.carlosdev.randomusersapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val gender: String,
    val name: Name,
    val location: Location,
    val email: String,
    val login: Login,
    val dob: Dob,
    val registered: Registered,
    val phone: String,
    val cell: String,
    val id: ID?,
    val picture: Picture,
    val nat: String
)