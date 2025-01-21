package com.carlosdev.randomusersapp.data.model

data class User(
    val gender: String,
    val name: Name,
    val location: Location,
    val email: String,
    val login: Login,
    val dob: String,
    val registered: String,
    val phone: String,
    val cell: String,
    val id: ID,
    val picture: Picture,
    val nat: String
)