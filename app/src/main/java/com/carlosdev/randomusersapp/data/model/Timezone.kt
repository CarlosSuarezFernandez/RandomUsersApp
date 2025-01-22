package com.carlosdev.randomusersapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Timezone(
    val offset: String,
    val description: String
)