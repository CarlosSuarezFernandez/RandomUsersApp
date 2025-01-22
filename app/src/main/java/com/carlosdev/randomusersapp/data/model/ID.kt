package com.carlosdev.randomusersapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ID(
    val name: String?,
    val value: String?
)