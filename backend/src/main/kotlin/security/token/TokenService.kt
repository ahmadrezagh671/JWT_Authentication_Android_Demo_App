package com.ahmadrezagh671.security.token

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String
}