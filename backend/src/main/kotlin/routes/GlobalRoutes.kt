package com.ahmadrezagh671.routes

import com.ahmadrezagh671.data.user.UserDataSource
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.getAllUsers(userDataSource: UserDataSource) {
    authenticate {
        get("users") {
            //val principal = call.principal<JWTPrincipal>()
            //val userId = principal?.getClaim("userId", String::class)

            val users = userDataSource.getAllUsers()

            call.respond(users)
        }
    }
}