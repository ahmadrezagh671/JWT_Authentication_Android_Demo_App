package com.ahmadrezagh671.plugins

import com.ahmadrezagh671.routes.authenticate
import com.ahmadrezagh671.data.user.UserDataSource
import com.ahmadrezagh671.routes.getAllUsers
import com.ahmadrezagh671.routes.getUserInfo
import com.ahmadrezagh671.security.hashing.HashingService
import com.ahmadrezagh671.security.token.TokenConfig
import com.ahmadrezagh671.security.token.TokenService
import com.ahmadrezagh671.routes.setName
import com.ahmadrezagh671.routes.signIn
import com.ahmadrezagh671.routes.signUp
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    routing {
        // Auth
        signIn(userDataSource, hashingService, tokenService, tokenConfig)
        signUp(hashingService, userDataSource)
        authenticate(userDataSource)

        // User
        getUserInfo(userDataSource)
        setName(userDataSource)

        // Global
        getAllUsers(userDataSource)
    }
}
