package com.ahmadrezagh671.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val id: String?,
    val username: String?,
    val subscription: Int?,
    val name: String?,
    val created: Long?
)