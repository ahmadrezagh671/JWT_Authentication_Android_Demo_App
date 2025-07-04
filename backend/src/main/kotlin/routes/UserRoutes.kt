package com.ahmadrezagh671.routes

import com.ahmadrezagh671.data.requests.SetNameRequest
import com.ahmadrezagh671.data.responses.UserInfo
import com.ahmadrezagh671.data.user.UserDataSource
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import org.bson.types.ObjectId

fun Route.getUserInfo(userDataSource: UserDataSource) {
    authenticate {
        get("userinfo") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)

            val user = userDataSource.getUserById(ObjectId(userId))

            val response = UserInfo(userId,user?.username, user?.subscription, user?.name, user?.created)

            call.respond(response)
        }
    }
}

fun Route.setName(userDataSource: UserDataSource) {
    authenticate {
        patch("name") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)

            val name = call.receive<SetNameRequest>().name
            if (name.isNotBlank()){
                val response = userDataSource.setName(ObjectId(userId),name)
                if (response){
                    call.respond(HttpStatusCode.OK)
                }else{
                    call.respond(HttpStatusCode.Conflict)
                }
            }else{
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}