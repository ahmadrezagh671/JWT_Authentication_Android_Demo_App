package com.ahmadrezagh671.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)
