package com.ahmadrezagh671.routes

import com.ahmadrezagh671.data.requests.AuthRequest
import com.ahmadrezagh671.data.requests.LoginRequest
import com.ahmadrezagh671.data.responses.AuthResponse
import com.ahmadrezagh671.data.user.UserDataSource
import com.ahmadrezagh671.data.user.UserModel
import com.ahmadrezagh671.security.hashing.HashingService
import com.ahmadrezagh671.security.hashing.SaltedHash
import com.ahmadrezagh671.security.token.TokenClaim
import com.ahmadrezagh671.security.token.TokenConfig
import com.ahmadrezagh671.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.apache.commons.codec.digest.DigestUtils
import org.bson.types.ObjectId
import kotlin.text.lowercase

fun Route.signUp(
    hashingService: HashingService,
    userDataSource: UserDataSource
) {
    post("signup") {
        val request = call.receive<AuthRequest>()


        // Check if username length is not allowed
        val isUsernameTooShort = request.username.length < 4 || request.username.length > 32
        if(isUsernameTooShort) {
            call.respond(HttpStatusCode.Conflict, "Username length is not allowed.")
            return@post
        }

        // Check if username already exists
        val existingUser = userDataSource.getUserByUsername(request.username.lowercase())
        if (existingUser != null) {
            call.respond(HttpStatusCode.Conflict, "Username already taken.")
            return@post
        }

        // Check if fields are blank
        val areFieldsBlank = request.username.isBlank() || request.password.isBlank()
        if(areFieldsBlank) {
            call.respond(HttpStatusCode.Conflict, "Missing username or password.")
            return@post
        }

        // Check if password length is not allowed
        val isPwTooShort = request.password.length < 8 || request.password.length > 32
        if(isPwTooShort) {
            call.respond(HttpStatusCode.Conflict, "Password length should be between 8 and 32 characters.")
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)

        val user = UserModel(
            username = request.username.lowercase(),
            password = saltedHash.hash,
            salt = saltedHash.salt
        )
        val wasAcknowledged = userDataSource.insertUser(user)
        if(!wasAcknowledged)  {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        call.respond(HttpStatusCode.OK)
    }
}

fun Route.signIn(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("signin") {
        val request = call.receive<AuthRequest>()

        val user = userDataSource.getUserByUsername(request.username.lowercase())
        if(user == null) {
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return@post
        }

        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )
        if(!isValidPassword) {
            println("Entered hash: ${DigestUtils.sha256Hex("${user.salt}${request.password}")}, Hashed PW: ${user.password}")
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            ),
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(
                token = token
            )
        )
    }
}

fun Route.authenticate(userDataSource: UserDataSource) {
    authenticate {
        post("authenticate") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)

            val user = userDataSource.getUserById(ObjectId(userId))

            if (user != null){

                if (user.banStatus >= 1){
                    call.respond(HttpStatusCode.Forbidden,"Your account has been banned.")
                }else{
                    val request = call.receive<LoginRequest>()

                    val appVersion = request.appVersion
                    val device = request.device
                    val lastLogin = System.currentTimeMillis()
                    val loginCount = user.loginCount + 1

                    val result = userDataSource.updateUserLoginInfo(ObjectId(userId),appVersion, device,loginCount,lastLogin)

                    if (result){
                        call.respond(HttpStatusCode.OK)
                    }else{
                        call.respond(HttpStatusCode.Conflict)
                    }
                }
            }else{
                call.respond(HttpStatusCode.Conflict)
            }
        }
    }
}