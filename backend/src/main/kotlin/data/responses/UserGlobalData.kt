package com.ahmadrezagh671.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserGlobalData(
    val id: String,
    val subscription: Int,
    val username: String,
    val name: String,
    val created: Long,
    val lastLogin:Long
)