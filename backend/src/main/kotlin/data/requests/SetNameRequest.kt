package com.ahmadrezagh671.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class SetNameRequest(
    val name: String
)