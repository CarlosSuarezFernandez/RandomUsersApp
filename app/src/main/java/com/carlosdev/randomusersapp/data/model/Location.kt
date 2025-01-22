package com.carlosdev.randomusersapp.data.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class Location(
    val street: Street,
    val city: String,
    val state: String,
    val country: String,
    val coordinates: Coordinates,
    val timezone: Timezone
)