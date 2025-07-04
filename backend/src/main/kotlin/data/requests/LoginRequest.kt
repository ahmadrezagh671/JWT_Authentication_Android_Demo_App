package com.ahmadrezagh671.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val appVersion:Int,
    val device: String
)
