package com.ahmadrezagh671.security.hashing

data class SaltedHash(
    val hash: String,
    val salt: String
)
